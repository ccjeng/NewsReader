package com.oddsoft.newsreader.hkapple;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oddsoft.newsreader.Debug;

import android.os.StrictMode;
import android.util.Log;

public class Category {
	//private static final String link = "http://174.132.129.66/~hkmagz6/index.php";
	//private static final String root = "http://174.132.129.66/~hkmagz6/";
	private static final String link = "http://hkmagze.serveblog.net/";
	private static final String root = "http://hkmagze.serveblog.net/";	
	Document doc;
	
	public void connect() {
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
              }
			
			doc = Jsoup.connect(link)
						.timeout(10000)
						.get();
		} catch (IOException e) {
			if (Debug.On) {
				Log.d("Category", e.toString());
			}
			e.printStackTrace();
		}
	}

	public String[] getCategory() {
		connect();
		String[] category = new String[14];
		int index = 0;
		if (doc != null) {
			try {
				Elements appcat = doc.select("li a");
				for (Element cat : appcat) {
					category[index] = cat.text();
					index = index + 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else 
			category = null;
		
		return category;
	}
	
	public String[] getURL() {
		connect();
		String[] url = new String[14];
		int index = 0;
		
		if (doc != null) {
//			Element content = doc.getElementsByTag("ul").first();
			//Elements links = doc.getElementsByTag("a");
			
			try {
				Elements links = doc.select("li a");
				for (Element l : links) {
					url[index] = root + l.attr("href");
					index = index + 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		} else 
			url = null;
		return url;
	}
}
