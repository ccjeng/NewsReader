package com.oddsoft.news.hk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;

import android.util.Log;

public class NewsBodyHKYahoo implements INewsBody {
	private static final String TAG = "NewsBodyHKYahoo";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<!-- START article -->")) {
					beginFind = true;
				} else if (s.trim().contains("<div class=\"yom-mod yom-follow") || s.trim().contains("<!-- END article -->")) {
					break;
				}
				if (beginFind) {
					sb.append(s.trim());
				}
			}
			/*
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<div class=\"info\">")) {
					beginFind = true;
				} else if (s.trim().contains("article-focus")) {
					break;
				} else if (s.trim().contains("<!--Vendor")) {
					break;
				} else if (s.trim().contains("<div id=\"article-utility\">")) {
					break;
				}
				if (beginFind) {
					sb.append(s.trim());
				}
			}
			//image	
			beginFind = false;
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("itemContainer")) {
					beginFind = true;
				} else if (s.trim().contains("com-feature-recomm")) {
					break;
				}
				if (beginFind) {
					sb.append(s.trim());
				}
			}*/
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
		//rs = rs.replace("<a class=\"photo-zoom\" href=\"#\">", "<!--");
		rs = rs.replace("a href", "ahref");
		rs = rs.replace("a class", "aclass");
		rs = rs.replace("<img src=\"http://l.yimg.com/mq/i/nws/partner", "<imgsrc=\"");
		rs = rs.replace("<img src=\" http://l.yimg.com/mq/i/nws/partner", "<imgsrc=\"");
		rs = rs.replace("<img src=\"http://d.yimg.com/a/i/hk/nws/p", "<imgsrc=\"");
		rs = rs.replace("©ñ¤j", "");	
		rs = rs.replace("<div", "<xxx");
		rs = rs.replace("</div>", "</xxx>");
		rs = rs.replace("<li", "<xx");
		rs = rs.replace("</li>", "</xxx>");
		rs = rs.replace("<span>", "<xxx>");
		rs = rs.replace("</span>", "</xxx>");
		rs = rs.replace("<ul", "<xx");
		rs = rs.replace("</ul>", "</xxx>");
		rs = rs.replace("<table", "<xx");	
		rs = rs.replace("<td", "<xx");
		rs = rs.replace("<tr", "<xx");
		//rs = rs.replace(";\" width=", ";background-repeat:no-repeat;\" width=");
		
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
