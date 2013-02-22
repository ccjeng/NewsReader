package com.oddsoft.news.sg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodySGNanYang implements INewsBody {
	private static final String TAG = "NewsBodySGNanYang";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 
				
				if (link.contains("news.nanyangpost.com")) {
					if (s.trim().contains("<div class=\"separator\"")) {
						beginFind = true;
					} else if (s.trim().contains("<g:plusone annotation='none' size='tall'>")) {
						break;
					}
				}
				else {
					if (s.trim().contains("<div class=\"separator\"") 
					 || s.trim().contains("<div class='post-body entry-content'>")) {	
						beginFind = true;
					} else if (s.trim().contains("<div class='postmeta'>")) {
						break;
					}
				}
				
				if (beginFind) {
					sb.append(s.trim());
				}
			}
			br.close();
		} catch (Exception e) {
            e.printStackTrace();
		}

		sb.append("<br><br>");
		if (Debug.On) {
			Log.d(TAG, "link= " + link.toString());		
		}
		return findContent(sb.toString());
	}
	
	public String findContent(String html) {

		String rs = new String(html);
		rs = rs.replace("<font color=\"#000000\">", "<font>");
		rs = rs.replace("<font size=\"3\">", "<font>"); 
		rs = rs.replace("<div class=\"font_size\">", "<!--");
		rs = rs.replace("<div id=\"article_body\">", "-->");
		rs = rs.replace("<table", "<xx");	
		rs = rs.replace("<td", "<xx");
		rs = rs.replace("<tr", "<xx");
	    rs = "<big>" + rs + "</big>";
	    if (Debug.On) {
	    	Log.d(TAG, rs.toString());
	    }

		return rs;
	}
	
}
