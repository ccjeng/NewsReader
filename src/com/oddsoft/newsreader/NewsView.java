package com.oddsoft.newsreader;

import java.lang.Thread.State;
import java.util.ArrayList;

import com.oddsoft.news.INewsBody;
import com.oddsoft.news.cn.NewsBodyCN163;
import com.oddsoft.news.cn.NewsBodyCNSina;
import com.oddsoft.news.cn.NewsBodyCNdayoo;
import com.oddsoft.news.cn.NewsBodyCNifeng;
import com.oddsoft.news.cn.NewsBodyCNinfzm;
import com.oddsoft.news.hk.NewsBodyHKAppleDaily;
import com.oddsoft.news.hk.NewsBodyHKEJ;
import com.oddsoft.news.hk.NewsBodyHKMetro;
import com.oddsoft.news.hk.NewsBodyHKMing;
import com.oddsoft.news.hk.NewsBodyHKOrientalDaily;
import com.oddsoft.news.hk.NewsBodyHKYahoo;
import com.oddsoft.news.hk.NewsBodyHKam730;
import com.oddsoft.news.hk.NewsBodyHKheadline;
import com.oddsoft.news.hk.NewsBodyHKsun;
import com.oddsoft.news.sg.NewsBodySGKW;
import com.oddsoft.news.sg.NewsBodySGNanYang;
import com.oddsoft.news.sg.NewsBodySGZaoBao;
import com.oddsoft.news.sg.NewsBodySGmerdekareview;
import com.oddsoft.news.sg.NewsBodySGsinchew;
import com.oddsoft.news.sg.NewsBodySGtherock;
import com.oddsoft.news.tw.NewsBodyAppDaily;
import com.oddsoft.news.tw.NewsBodyBNext;
import com.oddsoft.news.tw.NewsBodyCNYes;
import com.oddsoft.news.tw.NewsBodyChinaTimes;
import com.oddsoft.news.tw.NewsBodyETToday;
import com.oddsoft.news.tw.NewsBodyEngadget;
import com.oddsoft.news.tw.NewsBodyLibertyTimes;
import com.oddsoft.news.tw.NewsBodyNOW;
import com.oddsoft.news.tw.NewsBodyNewsTalk;
import com.oddsoft.news.tw.NewsBodyPCHome;
import com.oddsoft.news.tw.NewsBodyUDN;
import com.oddsoft.news.tw.NewsBodyYahoo;
import com.oddsoft.newsreader.rss.Feed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NewsView extends Activity implements OnGestureListener  {
	private static final String TAG = "NewsView"; 
	private static final int PREF_ID = Menu.FIRST; 
	private static final int BROWSE_ID = Menu.FIRST + 1; 
	private static final int SHARE_ID = Menu.FIRST + 2;
	private static final int REFRESH_ID = Menu.FIRST + 3;
	final String mimeType = "text/html";
    final String encoding = "utf-8";
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private String shareContent;
    private int vWidth;
    private String htmlTagStart, htmlTagEnd;
    private ArrayList<Feed> info = new ArrayList<Feed>();
	private int position;
	private WebView wv;
	private ViewFlipper flipper;
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    
    private INewsBody getNewsBody = null;
    private boolean noimagePreference;
    private String volumnkeyPreference;
    private boolean gesturePreference;
    private String fontsizePreference;
    private boolean smartsavingPreference;
	private String html;
	
	private GestureDetector gd;
	private ProgressDialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Bundle bundle = this.getIntent().getExtras();
		int sourceNumber = Integer.parseInt(bundle.getString("SourceNum"));
		String TabName = bundle.getString("SourceTab");
		
		//get feed arrayList
		Intent intent = getIntent();
		info = intent.getParcelableArrayListExtra("feed");  
		position = Integer.parseInt(bundle.getString("position"));
		
		super.onCreate(savedInstanceState);
		// Adds Progrss bar Support
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		getPrefs();
		setContentView(R.layout.news_webview);
		
		 //for long press
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutView1);
        this.registerForContextMenu(linearLayout);
        
		// Makes Progress bar Visible
		getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
		gd = new GestureDetector(this);
		wv = (WebView) findViewById(R.id.news_view_body);
		// let webview fit screen
		wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Configuration config = getResources().getConfiguration();  
		
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
			vWidth = dm.heightPixels;
		else
			vWidth = dm.widthPixels;
	  
		if (TabName.equals("tabTW")) {
			switch (sourceNumber) {
	    	case 0:
	    		getNewsBody = new NewsBodyYahoo();
	        	break;
	    	case 1:
	    		getNewsBody = new NewsBodyUDN();
	        	break;
	    	case 2:
	    		getNewsBody = new NewsBodyPCHome();
	        	break;
	    	case 3:
	    		getNewsBody = new NewsBodyChinaTimes();
	        	break;
	    	case 4:
	    		getNewsBody = new NewsBodyNOW();
	        	break;
	    	case 5:
	    		getNewsBody = new NewsBodyEngadget();
	        	break;
	    	case 6:
	    		getNewsBody = new NewsBodyBNext();
	        	break;
	    	case 7:
	    		getNewsBody = new NewsBodyETToday();
	        	break;
	    	case 8:
	    		getNewsBody = new NewsBodyCNYes();
	        	break;
	    	case 9:
	    		getNewsBody = new NewsBodyNewsTalk();
	        	break;
	    	case 10:
	    		getNewsBody = new NewsBodyLibertyTimes();
	        	break;	
		    /*case 11:
	    		getNewsBody = new NewsBodyAppDaily();
	        	break;
	        	*/
	        }
		} else if (TabName.equals("tabHK")) {
			switch (sourceNumber) {
			case 0:
	    		getNewsBody = new NewsBodyHKAppleDaily();
	        	break;	
	    	case 1:
	    		getNewsBody = new NewsBodyHKOrientalDaily();
	        	break;	
	    	case 2:
	    	case 3: //YahooStGlobal
	    		getNewsBody = new NewsBodyHKYahoo();
	        	break;
	    	case 4:
	    	case 5: //YahooMing
	    		getNewsBody = new NewsBodyHKMing();
	        	break;	
	    	case 6:
	    		getNewsBody = new NewsBodyHKEJ();
	        	break;
	    	case 7:
	    		getNewsBody = new NewsBodyHKMetro();
	        	break;
	    	case 8:
	    		getNewsBody = new NewsBodyHKsun();
	        	break;
	    	case 9:
	    		getNewsBody = new NewsBodyHKam730();
	        	break;
	    	case 10:
	    		getNewsBody = new NewsBodyHKheadline();
	        	break;	
			}
		} else if (TabName.equals("tabCN")) {
			switch (sourceNumber) {
			case 0:
	    		getNewsBody = new NewsBodyCNinfzm();
	        	break;
	    	case 1:
	    	case 2:
	    	case 3:
	    	case 4:
	    	case 5:
	    		getNewsBody = new NewsBodyCNSina();
	        	break;
	    	case 6:
	    		getNewsBody = new NewsBodyCNifeng();
	        	break;
	    	case 7:
	    		getNewsBody = new NewsBodyCNdayoo();
	        	break;	
	    	case 8:
	    	case 9:
	    	case 10:
	    	case 11:
	    		getNewsBody = new NewsBodyCN163();
	        	break;	
			}
		} else if (TabName.equals("tabSG")) {
			switch (sourceNumber) {
	    	case 0:
	    		getNewsBody = new NewsBodySGZaoBao();
	        	break;
	    	case 1:
	    		getNewsBody = new NewsBodySGNanYang();
	        	break;
	    	case 2:
	    		getNewsBody = new NewsBodySGsinchew();
	        	break;
	    	case 3:
	    		getNewsBody = new NewsBodySGKW();
	        	break;	
	    	case 4:
	    		getNewsBody = new NewsBodySGtherock();
	        	break;
	    	case 5:
	    		getNewsBody = new NewsBodySGmerdekareview();
	        	break;
			}
		}
		
		flipper = (ViewFlipper) findViewById(R.id.view_flipper);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.right_out);
		showContent(info.get(position).getTitle()
				, info.get(position).getLink()
				, getString(R.string.loading));
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, PREF_ID, 0, R.string.preference).setIcon(android.R.drawable.ic_menu_preferences); 
    	menu.add(0, BROWSE_ID, 1, R.string.browser).setIcon(R.drawable.browser);
    	menu.add(0, SHARE_ID, 2, R.string.share_title).setIcon(android.R.drawable.ic_menu_share);
    	menu.add(0, REFRESH_ID, 3, R.string.refresh).setIcon(R.drawable.refresh);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
        case PREF_ID:
     		startActivity(new Intent(NewsView.this, Prefs.class));
  	   		break;
        case BROWSE_ID:      	
        	Uri uri = Uri.parse(info.get(position).getLink());
        	startActivity( new Intent(Intent.ACTION_VIEW, uri));
            break;
        case SHARE_ID:
        	final Intent intent = new Intent(Intent.ACTION_SEND);
	     	intent.setType("text/plain");
	     	intent.putExtra(Intent.EXTRA_SUBJECT, "");
	     	intent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());
	     	startActivity(Intent.createChooser(intent, "Select an action for sharing"));
        	break;
        case REFRESH_ID:      	
        	showContent(info.get(position).getTitle()
        			, info.get(position).getLink()
        			, getString(R.string.loading));
        	break;	
        }
        return super.onOptionsItemSelected(item); 
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case 0:
		    	super.onBackPressed();
		    	break; 
		    case 1:
		    	startActivity(new Intent(NewsView.this, Prefs.class));
	  	   		break; 
	        case 2:      	
	        	Uri uri = Uri.parse(info.get(position).getLink());
	        	startActivity( new Intent(Intent.ACTION_VIEW, uri));
	            break;
	        case 3:
	        	final Intent intent = new Intent(Intent.ACTION_SEND);
		     	intent.setType("text/plain");
		     	intent.putExtra(Intent.EXTRA_SUBJECT, "");
		     	intent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());
		     	startActivity(Intent.createChooser(intent, "Select an action for sharing"));
	        	break;
	        case 4:      	
	        	showContent(info.get(position).getTitle()
	        			, info.get(position).getLink()
	        			, getString(R.string.loading));
	        	break;		  	   		
	    }
       return super.onContextItemSelected(item);
    }
 
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        contextMenu.add(0, 0, 0, getString(R.string.back_to_previous_page));
        contextMenu.add(0, 1, 1, getString(R.string.preference));
        contextMenu.add(0, 2, 2, getString(R.string.browser));
        contextMenu.add(0, 3, 3, getString(R.string.share_title));
        contextMenu.add(0, 4, 4, getString(R.string.refresh));
    }
	
    private void getPrefs() {
    	//Set Theme
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		//Set Orientation
		String orientationPreference = prefs.getString("orientation", "NO");
		if (orientationPreference.equals("LANDSCAPE")) 
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else if (orientationPreference.equals("PORTRAIT")) 
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		String themePreference = prefs.getString("theme", "WHITE");   
		if (themePreference.equals("WHITE")) {
			//setTheme(R.style.Theme_White);
			htmlTagStart = "<body bgcolor=\"WHITE\"><font color=\"BLACK\">";
		} else if (themePreference.equals("BLACK")){
			//setTheme(R.style.Theme_Black);
			htmlTagStart = "<body bgcolor=\"BLACK\"><font color=\"WHITE\">";
		} else if (themePreference.equals("GRAY")){
			htmlTagStart = "<body bgcolor=\"#EEEEFF\"><font color=\"BLACK\">";
		}
		htmlTagEnd = "</font></body>";
		
		boolean fullscreenPreference = prefs.getBoolean("fullscreen", false);
		if (fullscreenPreference) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		noimagePreference = prefs.getBoolean("noimage", false);
		volumnkeyPreference = prefs.getString("volumekey", "SCROLL");
		gesturePreference = prefs.getBoolean("gesture", true);
		fontsizePreference = prefs.getString("fontsize", "NORMAL");
		smartsavingPreference = prefs.getBoolean("smartsaving", false);
    }

    @Override
	protected void onStart() {
		super.onStart();
		getPrefs();
	}
    
    
    private Runnable showUpdate = new Runnable(){  
        public void run(){  
        	try {    		
	        	//Log.d(TAG, "run="+info.get(position).getLink());	            
	            html = getNewsBody.loadHtml(info.get(position).getLink());  
	            Boolean ignoreImage = false;

	            if (noimagePreference) {
	            	ignoreImage = true;
	            }
	            
	            if (smartsavingPreference) {
	            	if (!isWifiAvailable())
	            		ignoreImage = true;
	            }
	            
	        	if (ignoreImage) {
					//ignore image
					wv.loadDataWithBaseURL(null, htmlTagStart + "<h2>" + info.get(position).getTitle().trim() 
							+ "</h2><hr><br>" + html.replace("<img ", "<xxx") + htmlTagEnd
							, mimeType, encoding, "about:blank");
				} else {
					wv.loadDataWithBaseURL(null, htmlTagStart + "<h2>" + info.get(position).getTitle().trim() 
							+ "</h2><hr><br>" + html + htmlTagEnd
							, mimeType, encoding, "about:blank");
				} 
        	 } catch (Exception e) {  
         		e.printStackTrace();
             } finally {
     			dialog.dismiss();
     		}	
        }  
    };	
    
    private Thread checkUpdate = new Thread(showUpdate);  
    
    private void showContent(String title, String link, String msg) {
    	shareContent = title.trim() + " " + link.toString();
		//news body
		try {
			wv.getSettings().setJavaScriptEnabled(true);
			wv.getSettings().setSupportZoom(true);     
			wv.getSettings().setBuiltInZoomControls(true); 
			//wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
			wv.getSettings().setCacheMode(2); //LOAD_NO_CACHE
			
			if (fontsizePreference.equals("LARGER")) {
				wv.getSettings().setTextSize(WebSettings.TextSize.LARGER);
			} else if (fontsizePreference.equals("LARGEST")) {
				wv.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
			} else if (fontsizePreference.equals("NORMAL")) {
				wv.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			} else if (fontsizePreference.equals("SMALLER")) {
				wv.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
			} else if (fontsizePreference.equals("SMALLEST")) {
				wv.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
			} else {
				wv.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			} 
			wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	
			final String js;
			js= "javascript:(function () { " +
				  " var w = " + vWidth + ";" +
				  " for( var i = 0; i < document.images.length; i++ ) {" +
				  " var img = document.images[i]; " +
				  "   img.height = Math.round( img.height * ( w/img.width ) ); " +
				  "   img.width = w; " +
				  " }" +
				  " })();";

			wv.setWebViewClient( new WebViewClient(){
				  @Override
				  public void onPageFinished(WebView view, String url) {
				    wv.loadUrl( js );
				  }
				});	
			
			//wv.setPadding(0, 0, 0, 0);

    		dialog = ProgressDialog.show(NewsView.this, "", msg, true, true);
			if(checkUpdate.getState()==State.TERMINATED){
				checkUpdate = new Thread(showUpdate);
			}
	     	checkUpdate.start();
	        
	     	wv.pageUp(true);
			//progress bar
			final Activity wvActivity = this;
			wv.setWebChromeClient(new WebChromeClient() {
	            public void onProgressChanged(WebView view, int progress) {
	            	wvActivity.setProgress(progress * 100); //Make the bar disappear after URL is loaded
	             }
	            });
			 if (Debug.On) {
				 Log.d(TAG,"link=" + link.toString());
			 }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }

    private void previousNews() {
    	if (position>0) {
        	position = position - 1;
        	showContent(info.get(position).getTitle()
        			, info.get(position).getLink()
        			,getString(R.string.load_previous_news));
        	flipper.setInAnimation(slideRightIn);
            flipper.setOutAnimation(slideRightOut);
            flipper.setFlipInterval(2000); 
            flipper.showPrevious();
    	}else {
    		Toast.makeText(getBaseContext(), getString(R.string.last_item)
    				, Toast.LENGTH_SHORT).show();
    	}	
    }
    private void nextNews() {
    	if (info.size()>position+1) {
    		position = position + 1;
    		showContent(info.get(position).getTitle()
    				, info.get(position).getLink()
    				, getString(R.string.load_next_news));
    		flipper.setInAnimation(slideLeftIn);
            flipper.setOutAnimation(slideLeftOut);
            flipper.setFlipInterval(2000); 
            flipper.showNext();
    	} else {
    		Toast.makeText(getBaseContext(), getString(R.string.last_item)
    				, Toast.LENGTH_SHORT).show();
    	}
    }
    
    private Boolean isWifiAvailable() {
    	final ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	if (connMgr.getActiveNetworkInfo().isAvailable())
    		return true;
    	else	
    		return false;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
    	if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) { 
            // pressed the volume down key 
    		if (volumnkeyPreference.equals("SCROLL")) {
    			if (wv!=null)
        			wv.pageDown(false);
    		}
    		if (volumnkeyPreference.equals("NEXTPREVIOUS")) {
    			nextNews();
    		}
            return true; 
	    } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) { 
	        // pressed the volume up key 
	    	if (volumnkeyPreference.equals("SCROLL")) {
		    	if (wv!=null)
	    			wv.pageUp(false);
	    	}
	    	if (volumnkeyPreference.equals("NEXTPREVIOUS")) {
	    		previousNews();
	    	}
	        return true; 
	    } else { 
            return super.onKeyDown(keyCode, event); 
	    } 
    }

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}
	@Override
    public boolean dispatchTouchEvent(MotionEvent event){
        super.dispatchTouchEvent(event);
        return gd.onTouchEvent(event);
    }
	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
			float velocityY) {
		try {
			if (gesturePreference) {		
			    if (Math.abs(event1.getY() - event2.getY()) > SWIPE_MAX_OFF_PATH)
		            return false;
		        // right to left swipe
		        if(event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE 
		        		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		        	nextNews();
		        }  else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE 
		        		&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		        	previousNews();
		        }
			}   
		} catch (Exception e) {
	           // nothing
	    }
 	  return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	} 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
