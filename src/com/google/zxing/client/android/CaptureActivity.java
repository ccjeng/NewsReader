/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.client.android.camera.CameraManager;
import com.oddsoft.newsreader.R; //change import

/**
 * The barcode reader activity itself. This is loosely based on the CameraPreview example included in the Android SDK.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {
	
	private static final String TAG = CaptureActivity.class.getSimpleName();
	
	private static final long INTENT_RESULT_DURATION = 1500L;
	// private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
	//
	// private static final String PACKAGE_NAME = "com.google.zxing.client.android";
	
	private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES;
	
	static {
		DISPLAYABLE_METADATA_TYPES = new HashSet<ResultMetadataType>(5);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ISSUE_NUMBER);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.SUGGESTED_PRICE);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ERROR_CORRECTION_LEVEL);
		DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.POSSIBLE_COUNTRY);
	}
	
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	private View resultView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	
	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.capture);
		
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		resultView = findViewById(R.id.result_view);
		statusView = (TextView) findViewById(R.id.status_view);
		handler = null;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		resetStatusView();

		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		
		Intent intent = getIntent();
		String action = intent == null ? null : intent.getAction();
		if (intent != null && action != null) {
			// Scan the formats the intent requested, and return the result to the calling activity.
			decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
			if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
				int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
				int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
				if (width > 0 && height > 0) {
					CameraManager.get().setManualFramingRect(width, height);
				}
			}
			
			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
		} else {
			
			decodeFormats = null;
			characterSet = null;
		}
		
		inactivityTimer.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		CameraManager.get().closeDriver();
	}
	
	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
			// Handle these events so they don't launch the Camera app
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	
	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		Log.i("Code Result", rawResult.getText() + "," + barcode);
		inactivityTimer.onActivity();
		
		handleDecodeExternally(rawResult, barcode);
		
	}
	
	// Briefly show the contents of the barcode, then handle the result outside Barcode Scanner.
	private void handleDecodeExternally(Result rawResult, Bitmap barcode) {
		viewfinderView.drawResultBitmap(barcode);
		
		// Since this message will only be shown for a second, just tell the user what kind of
		// barcode was found (e.g. contact info) rather than the full contents, which they won't
		// have time to read.
		statusView.setText("Bingo!!");
		
		// Hand back whatever action they requested - this can be changed to Intents.Scan.ACTION when
		// the deprecated intent is retired.
		Intent intent = new Intent(getIntent().getAction());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
		intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
		byte[] rawBytes = rawResult.getRawBytes();
		if (rawBytes != null && rawBytes.length > 0) {
			intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
		}
		Message message = Message.obtain(handler, R.id.return_scan_result);
		message.obj = intent;
		handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
		
	}
	
	// Add by Carlos, 2011/8/19 銝�10:23:49
	// /**
	// * We want the help screen to be shown automatically the first time a new version of the app is
	// * run. The easiest way to do this is to check android:versionCode from the manifest, and compare
	// * it to a value stored as a preference.
	// */
	// private boolean showHelpOnFirstLaunch() {
	// try {
	// PackageInfo info = getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
	// int currentVersion = info.versionCode;
	// // Since we're paying to talk to the PackageManager anyway, it makes sense to cache the app
	// // version name here for display in the about box later.
	// this.versionName = info.versionName;
	// SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	// int lastVersion = prefs.getInt(PreferencesActivity.KEY_HELP_VERSION_SHOWN, 0);
	// if (currentVersion > lastVersion) {
	// prefs.edit().putInt(PreferencesActivity.KEY_HELP_VERSION_SHOWN, currentVersion).commit();
	// Intent intent = new Intent(this, HelpActivity.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	// // Show the default page on a clean install, and the what's new page on an upgrade.
	// String page = lastVersion == 0 ? HelpActivity.DEFAULT_PAGE : HelpActivity.WHATS_NEW_PAGE;
	// intent.putExtra(HelpActivity.REQUESTED_PAGE_KEY, page);
	// startActivity(intent);
	// return true;
	// }
	// } catch (PackageManager.NameNotFoundException e) {
	// Log.w(TAG, e);
	// }
	// return false;
	// }
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
			}
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializating camera", e);
		}
	}
	
	private void resetStatusView() {
		try{
			resultView.setVisibility(View.GONE);
		} catch (Exception e) {
			Log.e("qrcode", "resultView");
			e.printStackTrace();
		}
		try {
			statusView.setText(R.string.msg_default_status);
			statusView.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			Log.e("qrcode", "statusView");
			e.printStackTrace();
		}	
		try {
			viewfinderView.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			Log.e("qrcode", "viewfinderView");
			e.printStackTrace();
		}
	}
	
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
}
