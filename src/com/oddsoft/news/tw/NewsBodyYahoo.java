package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyYahoo implements INewsBody {
	private static final String TAG = "NewsBodyYahoo";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 			
				if (s.trim().contains("<span class=\"provider org\">")) {
//				if (s.trim().contains("<h1 class=\"headline\">")) {
					beginFind = true;
				} else if (s.trim().contains("<div class=\"yom-mod yom-follow\"")
						|| s.trim().contains("<!-- END article -->")) {
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
		rs = rs.replace("<a href", "<ahref");   
		rs = rs.replace("<table", "<xx");	
		rs = rs.replace("<td", "<xx");
		rs = rs.replace("<tr", "<xx");
		rs = rs.replace("<h1 class=", "<!--<h1 class=");
		rs = rs.replace("class=\"logo\">", "class=\"logo\">-->");
		rs = rs.replace("<div class=\"yom-mod yom-art-related", "<!--<div class=\"yom-mod yom-art-related");
		rs = rs.replace("<li class=\"photo first last\">", "<li class=\"photo first last\">-->");

		rs = rs.replace("http://l.yimg.com/os/mit/media/m/base/images/transparent-95031.png\" style=\"background-image:url('"
				, "");
		rs = rs.replace("');\" width=", "\" width=");		
		
		rs = "<big>" + rs + "</big>";
		if (Debug.On) {
			Log.d(TAG, rs.toString());
		}

		return rs;
	}
	
}
