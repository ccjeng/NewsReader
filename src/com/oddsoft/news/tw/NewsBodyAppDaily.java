package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyAppDaily implements INewsBody {
	private static final String TAG = "NewsBodyAppDaily";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
		
			while (null != (s = br.readLine())) { 
				
				if (s.trim().contains("<div class=\"articulum\">")) {
					beginFind = true;
				} else if (s.trim().contains("<span class=\"clearman\">") ||
						s.trim().contains("<iframe src=") ||
						s.trim().contains("<div class=\"urcc\">")) {
					beginFind = false;
				}
				if (beginFind) {
					sb.append(s.trim());
				}
				/*
				if (beginFind1) {
					sb.append(s.trim()+"<p>");
				}*/
			}

			br.close();
		} catch (Exception e) {
            e.printStackTrace();
		}
		if (Debug.On) {
			Log.d(TAG, "link= " + link.toString());		
		}
		return findContent(sb.toString(), link.toString());
	}
	
	public String findContent(String html, String url) {

		String rs = new String(html);
		rs = rs.replace("a href", "ahref");
		rs = rs.replace("<h1", "<!--");	
		rs = rs.replace("</h1>", "-->");
		rs = rs.replace("<h2", "<p");	
		rs = rs.replace("</h2>", "</p>");
		rs = rs.replace("<hr ", "<xx ");
		
		if (!url.contains("realtimenews"))
			rs = rs.replace("<img src=", "<xx");
		
		rs = rs.replace("<a title=", "<!--");
		rs = rs.replace("href=\"", "--> <img src=\"");
		
	    rs = rs.replace("<img src=\"http://www.emailcash.com.tw/apple", "<!--");	    
	    rs = rs.replace("<table", "<xx");
		rs = rs.replace("<tr", "<xx");

		rs = rs.replace("style=\"padding: 0px 10px;\"", "");
		rs = rs.replace("<div class", "<xx");
		rs = rs.replace("<div id", "<xx");
		rs = rs.replace("<section class", "<xx");
		rs = rs.replace("<iframe", "<!--");
		rs = rs.replace("</iframe>", "-->");

		
		//rs = rs.replace("※關心體育盛事", "<!--");
		rs = rs.replace("<section id=\"articlelast\">", "<!--");
		rs = rs.replace("googletag.display('articlelast');", "");
		//rs = rs.replace("<img src=\"http://www.appledaily.com.tw/olympic2012", "<!--");
		rs = rs.replace("<img src=\"https://plus.google.com/", "<!--");
		
	    rs = "<big>" + rs + "</big>";
	    if (Debug.On) {
	    	Log.d(TAG, rs.toString());
	    }
		return rs;
	}
	
}
