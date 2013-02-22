package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyLibertyTimes implements INewsBody {
	private static final String TAG = "NewsBodyLibertyTimes";

	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		InputStreamReader isr;
		StringBuilder sb = new StringBuilder();
		try {
			isr = new InputStreamReader(con.getInputStreamFromUrl(link),"BIG-5");
				
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<span class=\"insubject1\" id=\"newtitle\">")) {
					beginFind = true;
				} else if (s.trim().contains("<table align=\"center\" cellpadding=\"10\" class=\"rate\">")) {
					break;
				} else if (s.trim().contains("<!-- µû¤À start -->")) {
					break;
				} else if (s.trim().contains("<div class=\"rate\">")) {
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
		rs = rs.replace("<img src", "<imgsrc"); 
		rs = rs.replace("<a href=\"http://iservice.libertytimes.com.tw/IService3/newspic.php?pic=", 
				"<img src=\"");
		rs = rs.replace("bigPic/", "bigPic/600_"); 
		rs = rs.replace("target=\"_blank\"", ""); 
		rs = rs.replace("<a href", "<ahref"); 
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
