package com.oddsoft.newsreader;

import java.util.HashMap;
import java.util.Map;

import com.flurry.android.FlurryAgent;
//import com.oddsoft.newsreader.hkapple.Category;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsCategory extends Activity  {
	private static final String TAG = "NewsCategory";
	private ListView listView;
	private TextView titleTextView;
	private String[] category;
	private ProgressDialog dialog = null;
	private int sourceNumber;
	private String TabName;
	private LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "NSWBR7L6P6IC5HY3SNSF");
        setContentView(R.layout.news_category_layout);
        listView = (ListView) findViewById(R.id.categorylist);
        
        //for long press
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutCat1);
        this.registerForContextMenu(linearLayout);
        
        getPrefs();
        
        //get intent values
		Bundle bunde = this.getIntent().getExtras();
        sourceNumber = Integer.parseInt(bunde.getString("SourceNum"));
        String CategoryName = bunde.getString("SourceName");
        TabName = bunde.getString("SourceTab");
        
        titleTextView = (TextView) findViewById(R.id.categorytitle);
        titleTextView.setText(CategoryName);
		Map<String,String> parameters = new HashMap<String,String>();
		parameters.put("Label", CategoryName);
		FlurryAgent.onEvent("Category_"+CategoryName,parameters);
		 
        if(TabName.equals("tabTW")) {
	        switch (sourceNumber) {
	        	case 0:
		        	category = getResources().getStringArray(R.array.newscatsYahoo);
		        	break;
	        	case 1:
		        	category = getResources().getStringArray(R.array.newscatsUDN);
		        	break;
	        	case 2:
		        	category = getResources().getStringArray(R.array.newscatsPCHome);
		        	break;
	        	case 3:
		        	category = getResources().getStringArray(R.array.newscatsChinaTimes);
		        	break;
	        	case 4:
		        	category = getResources().getStringArray(R.array.newscatsNOW);
		        	break;
	        	case 5:
		        	category = getResources().getStringArray(R.array.newscatsEngadget);
		        	break;
	        	case 6:
		        	category = getResources().getStringArray(R.array.newscatsBNext);
		        	break;
	        	case 7:
		        	category = getResources().getStringArray(R.array.newscatsEttoday);
		        	break;
	        	case 8:
		        	category = getResources().getStringArray(R.array.newscatsCNYes);
		        	break;
	        	case 9:
		        	category = getResources().getStringArray(R.array.newscatsNewsTalk);
		        	break;
	        	case 10:
		        	category = getResources().getStringArray(R.array.newscatsLibertyTimes);
		        	break;
        	    case 11:
		        	category = getResources().getStringArray(R.array.newscatsAppDaily);
		        	break;		        	
	        }
        } else if (TabName.equals("tabHK")) {
        	switch (sourceNumber) {
	        	case 0:
	        		category = getResources().getStringArray(R.array.newscatsHKAppleDaily);
	        		/*Category appCategory = new Category();
	        		category = appCategory.getCategory();*/
		        	break;	
	        	case 1:
		        	category = getResources().getStringArray(R.array.newscatsHKOrientalDaily);
		        	break;
	        	case 2:
		        	category = getResources().getStringArray(R.array.newscatsHKYahoo);
		        	break;
	        	case 3:
		        	category = getResources().getStringArray(R.array.newscatsHKYahooStGlobal);
		        	break;	
	        	case 4:
		        	category = getResources().getStringArray(R.array.newscatsHKMing);
		        	break;
	        	case 5:
		        	category = getResources().getStringArray(R.array.newscatsHKYahooMing);
		        	break;  
	        	case 6:
		        	category = getResources().getStringArray(R.array.newscatsHKEJ);
		        	break;
	        	case 7:
		        	category = getResources().getStringArray(R.array.newscatsHKMetro);
		        	break;
	        	case 8:
		        	category = getResources().getStringArray(R.array.newscatsHKsun);
		        	break;
	        	case 9:
		        	category = getResources().getStringArray(R.array.newscatsHKam730);
		        	break;
	        	case 10:
		        	category = getResources().getStringArray(R.array.newscatsHKheadline);
		        	break;	
        	}
        } else if (TabName.equals("tabCN")) {
            switch (sourceNumber) {
	            case 0:
	    	       	category = getResources().getStringArray(R.array.newscatsCNinfzm);
	    	       	break;
            	case 1:
	    	       	category = getResources().getStringArray(R.array.newscatsSinaNews);
	    	       	break;
	            case 2:
	    	       	category = getResources().getStringArray(R.array.newscatsSinaSports);
	    	       	break;
	            case 3:
	    	       	category = getResources().getStringArray(R.array.newscatsSinaTech);
	    	       	break;
	            case 4:
	    	       	category = getResources().getStringArray(R.array.newscatsSinaFinance);
	    	       	break;
	            case 5:
	    	       	category = getResources().getStringArray(R.array.newscatsSinaEnt);
	    	       	break;   
	            case 6:
	    	       	category = getResources().getStringArray(R.array.newscatsCNifeng);
	    	       	break;
	            case 7:
	    	       	category = getResources().getStringArray(R.array.newscatsCNdayoo);
	    	       	break;
	            case 8:
	    	       	category = getResources().getStringArray(R.array.newscatsCN163news);
	    	       	break;   	
	            case 9:
	    	       	category = getResources().getStringArray(R.array.newscatsCN163ent);
	    	       	break;
	            case 10:
	    	       	category = getResources().getStringArray(R.array.newscatsCN163tech);
	    	       	break;
	            case 11:
	    	       	category = getResources().getStringArray(R.array.newscatsCN163money);
	    	       	break;   	
            }
        } else if (TabName.equals("tabSG")) {
                switch (sourceNumber) {
    	        case 0:
    	    	   	category = getResources().getStringArray(R.array.newscatsSGZaobao);
    	    	   	break;
    	        case 1:
    	    	   	category = getResources().getStringArray(R.array.newscatsSGNanYang);
    	    	   	break;
    	        case 2:
    	    	   	category = getResources().getStringArray(R.array.newscatsSGsinchew);
    	    	   	break;   	
    	        case 3:
    	    	   	category = getResources().getStringArray(R.array.newscatsSGKW);
    	    	   	break;
    	        case 4:
    	    	   	category = getResources().getStringArray(R.array.newscatsSGtherock);
    	    	   	break;
    	        case 5:
    	    	   	category = getResources().getStringArray(R.array.newscatsSGmerdekareview);
    	    	   	break;   	
                }
            }
       
        if (category != null) {
	    	listView.setAdapter(new ArrayAdapter<String>
	    		(this,android.R.layout.simple_list_item_1 , category));
	
	    	listView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {        		
	                dialog = ProgressDialog.show(NewsCategory.this, "", getString(R.string.loading), true, true);
	                new Thread() {
	                	public void run() {
	                		try {
	                			goIntent(position, category[position]);
	                		} catch(Exception e) {
	                			e.printStackTrace();
	                		} finally {
	                			dialog.dismiss();
	                		}	
	                	}
	                }.start();	                
	        	}
	        });
	    	listView.setSelection(0);
	    	adWhirl();
        }
   }
    
    private void goIntent(int itemnumber, String itemname) {
        Intent intent = new Intent();
        intent.setClass(NewsCategory.this, NewsList.class);
        Bundle bundle = new Bundle();
        if (Debug.On) {
        	Log.d(TAG, "goIntent-itemnumber: " + Integer.toString(itemnumber));
        }
        bundle.putString("CategoryNum", Integer.toString(itemnumber));
        bundle.putString("CategoryName", titleTextView.getText().toString() + " - " + itemname.toString());
        bundle.putString("SourceNum", Integer.toString(sourceNumber));
        bundle.putString("SourceTab", TabName.toString());
        intent.putExtras(bundle);
        startActivity(intent);
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
    
    @Override
    protected void onDestroy() {
       super.onDestroy();
    }
    protected void onStop() {
       	super.onStop();
        // Stop the tracker when it is no longer needed.
       	FlurryAgent.onEndSession(this);
       }
    @Override
	protected void onStart() {
		super.onStart();
	}     
	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null)
			dialog.dismiss();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	} 
	
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		    case 0:
		    	super.onBackPressed();
		    	break; 
		    case 1:
		    	startActivity(new Intent(NewsCategory.this, Prefs.class)); 
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
    
    
	private void adWhirl() {
		//AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "bcd6d779dfd5495aaf159406de9e3181");
	/*	AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "42b9769f81ec41ec814ddd8323ba3358");
    
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutCat1);
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
*/    	
	}




}
