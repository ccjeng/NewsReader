package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyNOW implements INewsBody {
	private static final String TAG = "NewsBodyNOW";
	private String plink;
	public String loadHtml(String link) throws IOException {
		plink = link;
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			
			//link = link.substring(0, 34).toString() + "pda-91" 
			//     + link.substring(link.length()-12, link.length());
			
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			/*
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<span class=\"LinkForm002\">")) {
					beginFind = true;
				} else if (s.trim().contains("<span class=\"LinkForm003\">")) {
					break;
				}
				if (beginFind) {
					sb.append(s.trim());
				}
			}*/
			
			if (plink.toString().contains("beauty")) {
				/*while (null != (s = br.readLine())) { 
					if (s.trim().contains("<div id=\"news_container\">")) {
						beginFind = true;
					} else if (s.trim().contains("<div class=\"star_adbanner\">")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}*/
			} else {
				beginFind = false;
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<div class=\"story_photo\">")
							|| s.trim().contains("<div class=\"story_content\">")
                            || s.trim().contains("<div id=\"news_container\">")) {
						beginFind = true;
					} else if (s.trim().replace(" ", "").contains("<div class=\"googlad_top\">")
							|| s.trim().contains("<div class=\"more_news\">")
							|| s.trim().contains("<div class=\"fb_like_button\">")
							|| s.trim().contains("<p class=\"bzkeyword\">")
							|| s.trim().contains("<!--內文 結束-->")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
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
		//rs = rs.replace("a target=\"_blank\" href", "");
		rs = rs.replace(" ", "");
		rs = rs.replace("color=\"#0042FF\"", "color=\"\"");
		rs = rs.replace("<h1>", "<!--");
		rs = rs.replace("</h1>", "-->");
		
		rs = rs.replace("<em>", "-->");
		rs = rs.replace("<h3", "<p");
		rs = rs.replace("h3>", "p>");
		rs = rs.replace("<h2>", "");
		rs = rs.replace("</h2>", "");
		rs = rs.replace("rel=\"", "src=\"");
		rs = rs.replace("img", "img ");
		rs = rs.replace("img /", "img/");
		//rs = rs.replace("<font color", "<xx");
		
		rs = rs.replace("<img src=\"/newspic/", "<img src=\"http://www.nownews.com/newspic/");
		rs = rs.replace("<img class=\"reporter-thumb\"src=", "<imgsrc=");
		
		rs = rs.replace("<li>", "");
		rs = rs.replace("</li>", "");
		rs = rs.replace("<ul>", "");
		rs = rs.replace("</ul>", "");
		rs = rs.replace("<!--推文開始-->", "<!--");
		rs = rs.replace("<!--推文結束-->", "-->");
		rs = rs.replace("</font>", "</xxx>");
		rs = rs.replace("<table", "<xx");	
		rs = rs.replace("<td", "<xx");
		rs = rs.replace("<tr", "<xx");
		//美人幫
		if (plink.toString().contains("beauty")) {
			rs = rs.replace("<div id=\"google_ad", "<!--");
			rs = rs.replace("<div class=\"star_adbanner", "<!--");
			rs = rs.replace("color: black;", "");
			rs = rs.replace("</font>", "</xxx>");
			rs = rs.replace("</span>", "</xxx>");
			
		}
	    rs = "<big>" + rs + "</big>";
	    if (Debug.On) {
	    	Log.d(TAG, rs.toString());
	    }

		return rs;
	}
	
}
