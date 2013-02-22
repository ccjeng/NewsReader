package com.oddsoft.newsreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

public class Prefs extends PreferenceActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
	      Preference aboutPref = (Preference) findPreference("about");
	      aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                       public boolean onPreferenceClick(Preference preference) {
	                    	   startActivity(new Intent(Prefs.this, AboutActivity.class));
	                                 return true;
	                              }
	                      });
	      Preference ratingPref = (Preference) findPreference("rating");
	      ratingPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                       public boolean onPreferenceClick(Preference preference) {
	                    	   		linkMarket();
	                                 return true;
	                              }
	                      });

	}

	private void linkMarket() {
		Uri uri = Uri.parse("market://details?id=com.oddsoft.newsreader");
		startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
	}
}
