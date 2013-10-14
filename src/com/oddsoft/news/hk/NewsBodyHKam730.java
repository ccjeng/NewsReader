package com.oddsoft.news.hk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyHKam730 implements INewsBody {
	private static final String TAG = "NewsBodyHKam730";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			//image
			/*
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<div id=\"article_img\">")) {
					beginFind = true;
				} else if (s.trim().contains("</li>")) {
					break;
				}
								
				if (beginFind) {
					sb.append(s.trim());
				}					
			}*/
			
			//content
			beginFind = false;
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<div class=\"wordsnap\">")) {
					beginFind = true;
				} else if (s.trim().contains("article_print")) {
					break;
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
		rs = rs.replace(" ", ""); 
		//rs = rs.replace("<img src=\"uploads", "<img src=\"http://www.am730.com.hk/uploads/");	
		rs = rs.replace("<li>", ""); 
		rs = rs.replace("</li>", ""); 
		rs = rs.replace("<imgwidth=", "<img width="); 
		rs = rs.replace("src=\"/uploads/", " src=\"http://www.am730.com.hk/uploads/"); 
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
