package com.oddsoft.news.hk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;
/*
 * change to ­»´ä¹q¥x
 * 
 * */
public class NewsBodyHKMetro implements INewsBody {
	private static final String TAG = "NewsBodyHKMetro";

	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"BIG-5");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<!-- content -->")) {
					beginFind = true;
				} else if (s.trim().contains("<!-- content end -->")) {
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
		rs = rs.replaceAll("<span", "<xxx");
		rs = rs.replaceAll("<td>", "<xxx>");
		rs = rs.replaceAll("</td>", "</xxx>");
		rs = rs.replaceAll("<tr>", "<xxx>");
		rs = rs.replaceAll("</tr>", "</xxx>");
		rs = rs.replaceAll("<table", "<xxx");
		rs = "<big>" + rs + "</big>";
		if (Debug.On) {
			Log.d(TAG, rs.toString());
		}

		return rs;
	}
	
}
