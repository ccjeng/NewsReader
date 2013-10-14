package com.oddsoft.newsreader;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

/*
import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
import com.mobfox.sdk.MobFoxView;
import com.mobfox.sdk.Mode;
import com.mobfox.sdk.RequestException;
*/
public class MobfoxCustomEvents /* implements AdWhirlInterface*/ {
/*
		private AdWhirlLayout mAdWhirlLayout;
		private Activity mActivity;
		private Context mContext;
		
		public MobfoxCustomEvents(AdWhirlLayout mAdWhirlLayout, Activity mActivity,
				Context mContext) {
			super();
			this.mAdWhirlLayout = mAdWhirlLayout;
			this.mActivity = mActivity;
			this.mContext = mContext;
			
		}

		public void adWhirlGeneric() {
			// TODO Auto-generated method stub
			
		}
		
		
		//To add Custom Events in your app on AdWhirl, you should first open your app and
		//then click on Add Custom Event. Use this function name while adding the Custom Event on AdWhirl. 
		public void mobfoxBanner() {
		
			String publisherId = "81b459201f3c03e7ed01e87996fdeef8";
			boolean includeLocation = true;
			Mode mode = Mode.LIVE;
			boolean animation = true;
			final MobFoxView mBanner = new MobFoxView(mActivity, publisherId, mode, includeLocation, animation);
			mBanner.setInternalBrowser(true);

			//Setting a listener to find out whether ad is received successfully or not
			mBanner.setBannerListener(new com.mobfox.sdk.BannerListener() {
				
				public void noAdFound() {
					// TODO Auto-generated method stub
					Log.v("Mobfox Banner Listener", "No Ad Found.");				
					//AdWhirl will now load from another ad network you have added
					//mAdWhirlLayout.setVisibility(View.GONE);
					mAdWhirlLayout.rollover();	
					//mAdWhirlLayout.setVisibility(View.VISIBLE);
				}
				
				public void bannerLoadSucceeded() {
					// TODO Auto-generated method stub
					Log.v("Mobfox Banner Listener", "Ad Received.");
					//AdWhirl will wait for 30 seconds or so before it will start requesting ads
					//mAdWhirlLayout.setVisibility(View.VISIBLE);
					mAdWhirlLayout.adWhirlManager.resetRollover();
					mAdWhirlLayout.rotateThreadedDelayed();
					
				}
				
				public void bannerLoadFailed(RequestException arg0) {
					// TODO Auto-generated method stub
					Log.v("Mobfox Banner Listener", "Ad Failed.");
					//AdWhirl will now load from another ad network you have added
					//mAdWhirlLayout.setVisibility(View.GONE);
					mAdWhirlLayout.rollover();
				}

				@Override
				public void adClicked() {
					// TODO Auto-generated method stub
					
				}
			});
		
			mAdWhirlLayout.handler.post(new AdWhirlLayout.ViewAdRunnable(
					mAdWhirlLayout, mBanner));
		
			
		}

*/

	}

