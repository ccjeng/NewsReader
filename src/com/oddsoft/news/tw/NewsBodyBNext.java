package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;

import android.util.Log;

public class NewsBodyBNext implements INewsBody {
	private static final String TAG = "NewsBodyBNext";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<div class=\"ArticleTool\">")) {
					sb.append(s.trim()+"<br>");
					break;
				} 
			}
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<div class=\"Article\">")) {
					beginFind = true;
				} else if (s.trim().contains("<div class=\"PushTool\">")) {
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
		rs = rs.replaceAll(" ", "");
		rs = rs.replace("imgsrc", "img src");
		rs = rs.replace("imgalt", "img alt");
		rs = rs.replace("img src=\"/", "img src=\"http://www.bnext.com.tw/");
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