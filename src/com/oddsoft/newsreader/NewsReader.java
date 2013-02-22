package com.oddsoft.newsreader;

import java.util.HashMap;
import java.util.Map;

import com.flurry.android.FlurryAgent;
import com.oddsoft.newsreader.storage.DbFeedAdapter;
import com.oddsoft.newsreader.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;

public class NewsReader extends TabActivity implements OnTabChangeListener {
	private static final String TAG = "NewsReader";
	private static final int PREF_ID = Menu.FIRST; 
	private static final int DIALOG_WELCOME = 1;
	private static final int DIALOG_UPDATE = 2;
	private ListView listView;
	private String[] newsSource;
	private String newsSourceIntent="tabTW";
	TabHost tabHost;
	String defaulttabPreference;
	private DbFeedAdapter mDbFeedAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	

        mDbFeedAdapter = new DbFeedAdapter(this);
        mDbFeedAdapter.open();        
        FlurryAgent.onStartSession(this, "NSWBR7L6P6IC5HY3SNSF");
        FlurryAgent.onPageView();
        
        setContentView(R.layout.main);
        listView = (ListView) findViewById(R.id.itemlist);
        getTag();
        getPrefs();
        getDefaultTab();

    	listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		goIntent(position, newsSource[position]);
        	}
        });

    	if (Utils.isNewInstallation(this)) {
   		 	this.showDialog(DIALOG_WELCOME);
    	} else if (Utils.newVersionInstalled(this)) {
            this.showDialog(DIALOG_UPDATE);
    	} 
    	
    	//adWhirl();    	
   }
    
    private void goIntent(int itemnumber, String itemname) {
        Intent intent = new Intent();
        intent.setClass(NewsReader.this, NewsCategory.class);
        Bundle bundle = new Bundle();
        if (Debug.On) {
	        Log.d(TAG, "goIntent-itemnumber: " + Integer.toString(itemnumber));
	        Log.d(TAG, "goIntent-itemname: " + itemname.toString());
	        Log.d(TAG, "goIntent-tab: " + newsSourceIntent.toString());
        }
        if (newsSourceIntent.equals("tabBK")) {
        	bundle.putString("SourceNum", mDbFeedAdapter.getBookmarkItem(itemnumber));
	        bundle.putString("SourceName", itemname.toString());
	        bundle.putString("SourceTab", mDbFeedAdapter.getBookmarkTab(itemnumber));
        } else {
	        bundle.putString("SourceNum", Integer.toString(itemnumber));
	        bundle.putString("SourceName", itemname.toString());
	        bundle.putString("SourceTab", newsSourceIntent.toString());
        }
        intent.putExtras(bundle);
        startActivityForResult(intent,0);
    }

    private void getTag() {
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);
        
        tabHost.addTab(tabHost.newTabSpec("tabTW") 
        		.setIndicator(getResources().getText(R.string.tabTW), getResources().getDrawable(R.drawable.flag_tw_48)) 
        		.setContent(new TabContentFactory() {
        			public View createTabContent(String arg0) {
        				return listView;
        			}
        		}));
        tabHost.addTab(tabHost.newTabSpec("tabHK") 
        		.setIndicator(getResources().getText(R.string.tabHK), getResources().getDrawable(R.drawable.flag_hk_48)) 
        		.setContent(new TabContentFactory() {
        			public View createTabContent(String arg0) {
        				return listView;
        			}
        		}));
        tabHost.addTab(tabHost.newTabSpec("tabCN") 
        		.setIndicator(getResources().getText(R.string.tabCN), getResources().getDrawable(R.drawable.flag_cn_48)) 
        		.setContent(new TabContentFactory() {
        			public View createTabContent(String arg0) {
        				return listView;
        			}
        		}));
        tabHost.addTab(tabHost.newTabSpec("tabSG") 
        		.setIndicator(getResources().getText(R.string.tabSG), getResources().getDrawable(R.drawable.flag_sg_48)) 
        		.setContent(new TabContentFactory() {
        			public View createTabContent(String arg0) {
        				return listView;
        			}
        		})); 
        tabHost.addTab(tabHost.newTabSpec("tabBK") 
        		.setIndicator(getResources().getText(R.string.tabBK), getResources().getDrawable(R.drawable.tab_bookmark)) 
        		.setContent(new TabContentFactory() {
        			public View createTabContent(String arg0) {
        				return listView;
        			}
        		}));
    }
	@Override
	public void onTabChanged(String tabName) {
		if(tabName.equals("tabTW")) {
			 newsSource = getResources().getStringArray(R.array.newsSourceTW);
		 }
		 else if (tabName.equals("tabHK")) {
			 newsSource = getResources().getStringArray(R.array.newsSourceHK);
		 }
		 else if (tabName.equals("tabCN")) {
			 newsSource = getResources().getStringArray(R.array.newsSourceCN);
		 }
		 else if (tabName.equals("tabSG")) {
			 newsSource = getResources().getStringArray(R.array.newsSourceSG);
		 }
		 else if (tabName.equals("tabBK")) {
			 //get detail bookmark
			 newsSource = mDbFeedAdapter.getBookmarkTitle();
		 }
		 newsSourceIntent = tabName;
		 Map<String,String> parameters = new HashMap<String,String>();
		 parameters.put("Label", tabName);
		 FlurryAgent.onEvent("Home_"+tabName,parameters);
		 
//		 listView.setAdapter(new ArrayAdapter<String>
//			(this,android.R.layout.simple_list_item_1 , newsSource));
		 NewsReaderAdapter adapter = new NewsReaderAdapter(this, newsSource);
		 listView.setAdapter(adapter);
		 listView.setSelection(0); 
		 
		 /* Add Context-Menu listener to the ListView. */
		 listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
             @Override
             public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
     			MenuInflater inflater = getMenuInflater();
    			AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
    			
    			if (newsSourceIntent.equals("tabBK"))
    				inflater.inflate(R.menu.menu_nofav, menu);
    			else {
    				if (mDbFeedAdapter.checkBookmark(newsSourceIntent, (int) acmi.id))
    		    		inflater.inflate(R.menu.menu_nofav, menu);
    		    	else
    		    		inflater.inflate(R.menu.menu_fav, menu);
    			}   
             }
     });
	}
	
	// ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
    		final AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
            /* Switch on the ID of the item, to get what the user selected. */
            switch (menuItem.getItemId()) {
                    case R.id.add_fav:
                    	//create bookmark
                    	mDbFeedAdapter.addBookmark(newsSourceIntent.toString()
                    			, (int) menuInfo.id
                    			, newsSource[(int) menuInfo.id]);
                    	onTabChanged(newsSourceIntent);
                            return true; /* true means: "we handled the event". */
                    case R.id.remove_fav:
                    	//delete bookmark
                    	if (newsSourceIntent.equals("tabBK"))
                    		mDbFeedAdapter.deleteBookmark((int) menuInfo.id);
                    	else
                   			mDbFeedAdapter.deleteBookmarkfromTab(newsSourceIntent, (int) menuInfo.id);
                    	//fillData
                    	onTabChanged(newsSourceIntent);
                    		return true;
            }
            return false;
    }
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) { 
	    super.onCreateOptionsMenu(menu); 
	    menu.add(0, PREF_ID, 0, R.string.preference).setIcon(android.R.drawable.ic_menu_preferences); 
	    return true;  
	}
	    
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
	     switch (item.getItemId()) { 
	     	case PREF_ID:
	     		startActivity(new Intent(NewsReader.this, Prefs.class));
      	   		break;
	        } 
	        return super.onOptionsItemSelected(item); 
	}
	
   @Override
   protected void onDestroy() {
      super.onDestroy();
      mDbFeedAdapter.close();
   }
   protected void onStop() {
   	super.onStop();
    // Stop the tracker when it is no longer needed.
   	FlurryAgent.onEndSession(this);
   }
   @Override
	protected void onStart() {
		super.onStart();
		getPrefs();
	}   
   private void getPrefs() {
   	//Set Theme
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		defaulttabPreference = prefs.getString("defaulttab", "TW");
		
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
   
   private void getDefaultTab() {
	   if (defaulttabPreference.equals("TW")) 
		   	onTabChanged("tabTW");
	   else if (defaulttabPreference.equals("HK")) 
			onTabChanged("tabHK");
	   else if (defaulttabPreference.equals("CN")) 
			onTabChanged("tabCN");
	   else if (defaulttabPreference.equals("SG")) 
			onTabChanged("tabSG");
	   else if (defaulttabPreference.equals("BK")) 
			onTabChanged("tabBK");
	   tabHost.setCurrentTabByTag("tab" + defaulttabPreference);
   }

   protected final Dialog onCreateDialog(final int id) {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setIcon(android.R.drawable.ic_dialog_info);
       builder.setCancelable(true);
       builder.setPositiveButton(android.R.string.ok, null);
       
       final Context context = this;
       
       switch (id) {
           case DIALOG_WELCOME:
               builder.setTitle(getResources().getString(R.string.welcome_title));
               builder.setMessage(getResources().getString(R.string.welcome_message));
               builder.setNeutralButton((String) getText(R.string.preference),
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(final DialogInterface d, final int which) {
                               Intent i = new Intent(context, Prefs.class);
                               startActivity(i);
                           }
                       });                    
               break;
           case DIALOG_UPDATE:
               builder.setTitle(getString(R.string.changelog_title));
               final String[] changes = getResources().getStringArray(R.array.updates);
               final StringBuilder buf = new StringBuilder();
               for (int i = 0; i < changes.length; i++) {
                   buf.append("\n\n");
                   buf.append(changes[i]);
               }
               builder.setMessage(buf.toString().trim());
               builder.setNeutralButton((String) getText(R.string.preference),
            		   new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(final DialogInterface d, final int which) {
                       Intent i = new Intent(context, Prefs.class);
                       startActivity(i);
                   }
               });
               break;
       }
       return builder.create();
   }

   public class NewsReaderAdapter extends BaseAdapter {
		public final String TAG = "NewsReaderAdapter";
		private final Activity context;
		private final String[] items;
		
		public NewsReaderAdapter(Activity context, String[] list) {
			  this.context = context;
			  this.items = list;
		 }

		public int getCount() {
	        return items.length;
	    }
		
		public String getItem(int position) {
	        return items[position];
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	    
	    public boolean isBookmark(int position) {
	    	if (mDbFeedAdapter.checkBookmark(newsSourceIntent, position))
	    		return true;
	    	else
	    		return false;
	    }
		// static to save the reference to the outer class and to avoid access to
		// any members of the containing class
	    public class ViewHolder {
			public ImageView btnBookmark;
			public TextView textView;
		}
		 @Override
		 public View getView(int position, View convertView, ViewGroup parent) {
			// ViewHolder will buffer the assess to the individual fields of the row
			// layout

			 ViewHolder holder;
				// Recycle existing view if passed as parameter
				// This will save memory and time on Android
				// This only works if the base layout for all classes are the same
			 View rowView = convertView;

			 if (rowView == null) {
				 LayoutInflater inflater = context.getLayoutInflater();
				 rowView = inflater.inflate(R.layout.main_list_item, null, true);
				 holder = new ViewHolder();
				 holder.textView = (TextView) rowView.findViewById(R.id.newsrow);
				 holder.btnBookmark = (ImageView) rowView.findViewById(R.id.bookmark);
				 rowView.setTag(holder);
			 } else {
				 holder = (ViewHolder) rowView.getTag();
			 }
			 holder.textView.setText(getItem(position));
			 if (newsSourceIntent.equals("tabBK")) {
				 holder.btnBookmark.setImageResource(R.drawable.bookmark_on);
			 } else {
				 holder.btnBookmark.setImageResource(isBookmark(position) ? R.drawable.bookmark_on : R.drawable.bookmark_empty);
			 }
			 
		//	 holder.btnBookmark.setTag(getItem(position));
			 if (position%2 == 0){
				 rowView.setBackgroundColor(0xff101010);
				  }
			 else{
				 rowView.setBackgroundColor(0xff080808);
			 }
			 
			 return rowView;
		}
	}
    
}
