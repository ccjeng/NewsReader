package com.oddsoft.news.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyCNSina implements INewsBody {
	private static final String TAG = "NewsBodyCNSina";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"GB2312");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) {
				if (s.trim().contains("<span id=\"pub_date\">")) {
					beginFind = true;
				} else if (s.trim().contains("<!-- google_ad_section_end -->") 
						|| s.trim().contains("<!-- publish_helper_end -->")) {
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
		rs = rs.replace("<a target=\"_blank\" href=\"http://ent.sina.com.cn/f/v/waptuiguang.html\">", "<!--");
		rs = rs.replace("<a style=\"font-size: 13px;\" target=\"_blank\" href=\"http://ent.sina.com.cn/f/v/waptuiguang.html\">", "<!--");

		rs = rs.replace("a href", "ahref");
		rs = rs.replace("vspace=", "xxx=");
		rs = rs.replace("hspace=", "xxx=");
		rs = rs.replace("border=", "xxx=");
		rs = rs.replace("http://www.sina.com.cn", "");
		rs = rs.replace("http://sports.sina.com.cn", "");
		rs = rs.replace("<a class", "<xxx");
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
