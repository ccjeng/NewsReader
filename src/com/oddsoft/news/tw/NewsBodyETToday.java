package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyETToday implements INewsBody {
	private static final String TAG = "NewsBodyETToday";

	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		InputStreamReader isr;
		StringBuilder sb = new StringBuilder();
		try {
			isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
				
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<!--本文 開始-->")) {
					beginFind = true;
				} else if (s.trim().contains("<!--本文 結束-->")) {
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
		rs = rs.replace("<p class=\"msg-amount\"><a href=\"#fbComments\">", "<!--"); 
		rs = rs.replace("<iframe", "<!--"); 
	    rs = "<big>" + rs + "</big>";
	    if (Debug.On) {
	    	Log.d(TAG, rs.toString());
	    }

		return rs;
	}
	
}
