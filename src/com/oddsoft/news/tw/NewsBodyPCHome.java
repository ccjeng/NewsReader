package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;

import android.util.Log;

public class NewsBodyPCHome implements INewsBody {
	private static final String TAG = "NewsBodyPCHome";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<div class=\"content\">")
						|| s.trim().contains("<div id=\"news_content\">")) {
					beginFind = true;
				} else if (s.trim().contains("span id=\"similar_news\">")) {
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
		rs = rs.replace("a href", "ahref");
		rs = rs.replace("hspace", "xxx");
		rs = rs.replace("vspace", "xxx");
		rs = rs.replace("<table", "<xxx");
		rs = rs.replace("<div", "<xxx");
		rs = rs.replace("<td", "<xxx");
		rs = rs.replace("<tr", "<xxx");

		rs = rs.replace("<li class", "<liclass");
		rs = rs.replace("<ul class", "<ulclass");
		rs = rs.replace("href=", "xxx=");
		rs = rs.replace("<ul class=\"photo_arrow both\">", "<!--");
		rs = rs.replace("點選放大</a></li>", "-->");
		
		rs = "<big>" + rs + "</big>";
		if (Debug.On) {
			Log.d(TAG, rs.toString());
		}

		return rs;
	}
	
}
