package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyChinaTimes implements INewsBody {
	private static final String TAG = "NewsBodyChinaTimes";

	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		InputStreamReader isr;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			//isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			
			if (link.contains("news.chinatimes.com/") || 
					link.contains("life.chinatimes.com/") ||
							link.contains("showbiz.chinatimes.com/"))
				isr = new InputStreamReader(con.getInputStreamFromUrl(link.replace("#comment", "")),"BIG-5");
				
			else
				isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
				
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			if (link.contains("/showbiz/")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<!--authorname begin-->")) {
						beginFind = true;
					} else if (s.trim().contains("<!--content end-->")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
			} else {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<div class=\"article_info clear-fix\">") ||
							s.trim().contains("<ul class=\"inline-list\">")) {
						beginFind = true;
					} else if (s.trim().contains("</article>") ||
							s.trim().contains("<!--content end-->")) {
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
		rs = rs.replace("<ul class=\"inline-list\">", "");
		rs = rs.replace("</ul>", "");
		rs = rs.replace("<div class=\"article_star clear-fix\">", "<!--");
		rs = rs.replace("<article class=\"clear-fix\">", "-->");
		rs = rs.replace("</li>", "");
		rs = rs.replace("class=", "xxx");
		rs = rs.replace("<td", "<xxx");
		rs = rs.replace("</td>", "</xxx>");
		rs = rs.replace("<tr", "<xxx");
		rs = rs.replace("</tr>", "</xxx>");
		rs = rs.replace("a href", "ahref");
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
