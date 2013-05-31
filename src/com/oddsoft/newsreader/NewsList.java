package com.oddsoft.newsreader;

import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
import com.oddsoft.newsreader.hkapple.Category;
import com.oddsoft.newsreader.hkapple.Feeds;
import com.oddsoft.newsreader.rss.Feed;
import com.oddsoft.newsreader.rss.XPPHandler;
import com.oddsoft.newsreader.rss.RSSFeed;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class NewsList extends Activity implements OnClickListener, AdWhirlInterface {
	public final String TAG = "NewsList";
	private static final int GONE = 8;
	private RSSFeed feed = null;
	private String[] feedURL;
	private int sourceNumber;
	private String CategoryName;
	private String TabName;
	private ArrayList<Feed> info;
	private ProgressDialog dialog = null;
	//static final int PROGRESS_DIALOG = 0;
	private String RSSFEEDOFCHOICE = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_layout);
        
        //for long press
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutList1);
        this.registerForContextMenu(linearLayout);
        
        //get intent values
		Bundle bunde = this.getIntent().getExtras();
        int itemNumber = Integer.parseInt(bunde.getString("CategoryNum"));
        CategoryName = bunde.getString("CategoryName");
        sourceNumber = Integer.parseInt(bunde.getString("SourceNum"));
        TabName = bunde.getString("SourceTab");
        

        getPrefs();
        
        if(TabName.equals("tabTW")) {
	        switch (sourceNumber) {
	    	case 0:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsYahoo);
	        	break;
	    	case 1:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsUDN);
	        	break;
	    	case 2:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsPCHome);
	        	break;
	    	case 3:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsChinaTimes);
	        	break;
	    	case 4:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsNOW);
	        	break;
	    	case 5:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsEngadget);
	        	break;
	    	case 6:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsBNext);	
	        	break;
	    	case 7:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsEttoday);	
	        	break;
	    	case 8:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsCNYes);	
	        	break;
	    	case 9:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsNewsTalk);	
	        	break;	
	    	case 10:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsLibertyTimes);	
	        	break;
   	    /*	case 11:
	    		feedURL = getResources().getStringArray(R.array.newsfeedsAppDaily);
	        	break; */		        	
	        }
        } else if (TabName.equals("tabHK")) {
        	switch (sourceNumber) {
        	case 0:
        		//feedURL = getResources().getStringArray(R.array.newsfeedsHKAppleDaily);
        		Category appCategory = new Category();
        		feedURL = appCategory.getURL();
	        	break;	
        	case 1:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKOrientalDaily);
	        	break;
        	case 2:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKYahoo);
	        	break;
        	case 3:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKYahooStGlobal);
	        	break;	
        	case 4:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKMing);
	        	break;
        	case 5:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKYahooMing);
	        	break;
        	case 6:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKEJ);
	        	break;
        	case 7:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKMetro);
	        	break;
        	case 8:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKsun);
	        	break;
        	case 9:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKam730);
	        	break;
        	case 10:
        		feedURL = getResources().getStringArray(R.array.newsfeedsHKheadline);
	        	break;	
        	}
        } else if (TabName.equals("tabCN")) {
        	switch (sourceNumber) {
        	case 0:
        		feedURL = getResources().getStringArray(R.array.newsfeedsCNinfzm);
	        	break;
        	case 1:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSinaNews);
	        	break;
        	case 2:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSinaSports);
	        	break;
        	case 3:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSinaTech);
	        	break;
        	case 4:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSinaFinance);
	        	break;
        	case 5:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSinaEnt);
	        	break;
        	case 6:
        		feedURL = getResources().getStringArray(R.array.newsfeedsCNifeng);
	        	break;
        	case 7:
        		feedURL = getResources().getStringArray(R.array.newsfeedsCNdayoo);
	        	break;
            case 8:
            	feedURL = getResources().getStringArray(R.array.newsfeedsCN163news);
    	       	break;   	
            case 9:
            	feedURL = getResources().getStringArray(R.array.newsfeedsCN163ent);
    	       	break;
            case 10:
            	feedURL = getResources().getStringArray(R.array.newsfeedsCN163tech);
    	       	break;
            case 11:
            	feedURL = getResources().getStringArray(R.array.newsfeedsCN163money);
    	       	break; 	
        	}
        } else if (TabName.equals("tabSG")) {
        	switch (sourceNumber) {
        	case 0:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSGZaobao);
	        	break;
        	case 1:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSGNanYang);
	        	break;
        	case 2:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSGsinchew);
	        	break;	
        	case 3:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSGKW);
	        	break;
        	case 4:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSGtherock);
	        	break;
        	case 5:
        		feedURL = getResources().getStringArray(R.array.newsfeedsSGmerdekareview);
	        	break;
        	}
        }
        
        if (feedURL != null)       	
        	RSSFEEDOFCHOICE = feedURL[itemNumber];
        if (RSSFEEDOFCHOICE == null) {
        	showError();
        	return;
        }
        if (Debug.On) {
        	Log.d(TAG, "RSSFEEDOFCHOICE= " + RSSFEEDOFCHOICE);
        }
     
        
      try {        	
        	if (isNetworkAvailable()) {
    	    	if (TabName.equals("tabHK") & sourceNumber == 0) {
    	    		Feeds appFeeds = new Feeds();
				    feed = appFeeds.getFeeds(RSSFEEDOFCHOICE);
    	    	} else
    	    		feed = getFeed(RSSFEEDOFCHOICE);
        	}
        	else {
        		//network is not available
        		showError();
            	return;
        	}

		} catch (IOException e) {
			Log.d(TAG, e.toString());
		} catch (XmlPullParserException e) {
			Log.d(TAG, e.toString());
		} catch (URISyntaxException e) {
			Log.d(TAG, e.toString());
		}     	      
       
		UpdateDisplay();
        adWhirl();
  	}

    
    private RSSFeed getFeed(String urlString) throws IOException, XmlPullParserException {
    	final URL url = new URL(urlString);
		RSSFeed rssFeed = XPPHandler.parseRSS(url);	
		return rssFeed;
	}
    
	private void UpdateDisplay() {
        TextView feedtitle = (TextView) findViewById(R.id.feedtitle);
        TextView feedpubdate = (TextView) findViewById(R.id.feedpubdate);
        ListView itemlist = (ListView) findViewById(R.id.itemlist);  
    	
        if (feed != null) {
	        feedtitle.setText(CategoryName.toString()); 

	        if (feed.getPubdate() != null) {
	        	feedpubdate.setText(getString(R.string.updatetime) + feed.getPubdate().toString());
	        } else feedpubdate.setVisibility(GONE);
	        
	        NewsListAdapter adapter = new NewsListAdapter(this, R.layout.list_item
	                		, feed.getAllItems());
	        		
	        itemlist.setAdapter(adapter);
	        itemlist.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {             
	                dialog = ProgressDialog.show(NewsList.this, "", getString(R.string.loading), true, true);
	                new Thread() {
	                	public void run() {
	                		try {
	                			showDetail(position);
	                			
	                		} catch(Exception e) {
	                			e.printStackTrace();
	                		} finally {
	                			dialog.dismiss();
	                		}	
	                	}
	                }.start();
	        	}
	        });
	        if (itemlist.getCount() == 0) {
	        	showError();
	        } else {
	        	itemlist.setSelection(0);
	        }
	        
    	 //insert all feeds into arraylist
    	 info = new ArrayList<Feed>(); 
    	 Feed feedItem;
    	 for(int i=0;i<feed.getItemCount();i++){  
    		 feedItem = new Feed(feed.getItem(i).getTitle(),feed.getItem(i).getLink()); 
    	     info.add(feedItem);    
         }  

        } else {	        
        	showError();
        	return;
        }
    }
    
     private void showDetail(int position) {    	 
    	 Intent itemintent = new Intent(this,NewsView.class);
    	 Bundle b = new Bundle();
    	 b.putString("SourceNum", Integer.toString(sourceNumber));
    	 b.putString("SourceTab", TabName.toString());    	 
    	 b.putString("position", Integer.toString(position));
    	 
    	 itemintent.putExtras(b);
    	 itemintent.putExtra("feed", info);
    	 startActivityForResult(itemintent,0);
     }


	@Override
	public void onClick(View arg0) {
	}
	
    @Override
    protected void onDestroy() {
       super.onDestroy();
       // Stop the tracker when it is no longer needed.
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null)
			dialog.dismiss();
	}
	
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case 0:
		    	super.onBackPressed();
		    	break; 
		    case 1:
		    	startActivity(new Intent(NewsList.this, Prefs.class)); 
		    	break; 
	    }
       return super.onContextItemSelected(item);
    }
 
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        contextMenu.add(0, 0, 0, getString(R.string.back_to_previous_page));
        contextMenu.add(0, 1, 1, getString(R.string.preference));
    }
    
    private boolean isNetworkAvailable() {  	
    	final ConnectivityManager connMgr = (ConnectivityManager)
    	this.getSystemService(Context.CONNECTIVITY_SERVICE);

    	final android.net.NetworkInfo wifi =
    		connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    	final android.net.NetworkInfo mobile =
    		connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

    	if( wifi.isAvailable() ){
    	 	return true;
    	} else if( mobile.isAvailable() ){
    		return true;
    	} else {	
    		return false;
    	}
    }
    
    private void showError() {
    	new AlertDialog.Builder(this)
	     	.setTitle(R.string.app_name)
	       	.setMessage(R.string.error)
	       	.setPositiveButton(R.string.ok_label,
	       			new DialogInterface.OnClickListener(){
	               		public void onClick(
	               				DialogInterface dialoginterface, int i){
	               				//empty	
	               			}
	       		})
	       	.show();
    }
    
    private void getPrefs() {
   		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

   		//Set Orientation
    	String orientationPreference = prefs.getString("orientation", "NO");
    	if (orientationPreference.equals("LANDSCAPE")) 
    		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	else if (orientationPreference.equals("PORTRAIT")) 
    		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	boolean fullscreenPreference = prefs.getBoolean("fullscreen", false);
    	if (fullscreenPreference) {
    		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	}
    	/*
		String themePreference = prefs.getString("theme", "WHITE");   
		if (themePreference.equals("WHITE")) {
			setTheme(R.style.Theme_White);
		} else {
			setTheme(R.style.Theme_Black);
		}*/
     }    

	private void adWhirl() {
		//AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "bcd6d779dfd5495aaf159406de9e3181");
		AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "42b9769f81ec41ec814ddd8323ba3358");
	    
    	LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutList1);
    	RelativeLayout.LayoutParams layoutParams = new
        RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT);
    			
    	//int diWidth = 320;
    	int diHeight = 52;
    	float density = getResources().getDisplayMetrics().density;
    
    	//adWhirlLayout.setAdWhirlInterface((AdWhirlInterface) this);
    	//adWhirlLayout.setMaxWidth((int)(vWidth * density));
    	adWhirlLayout.setMaxHeight((int)(diHeight * density));
    
    	layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    	linearLayout.addView(adWhirlLayout, layoutParams);
    	adWhirlLayout.setAdWhirlInterface(new MobfoxCustomEvents(adWhirlLayout, this, getApplicationContext()));
    	linearLayout.invalidate();
    	


	}

	@Override
	public void adWhirlGeneric() {
		// TODO Auto-generated method stub
		
	}

}