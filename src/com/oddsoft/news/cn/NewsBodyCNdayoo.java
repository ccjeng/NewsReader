package com.oddsoft.news.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyCNdayoo implements INewsBody {
	private static final String TAG = "NewsBodyCNdayoo";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) {
				if (s.trim().contains("<!--NewsContent-->")) {
					beginFind = true;
				} else if (s.trim().contains("<!--/enpcontent-->")) {
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
		rs = rs.replace("http://www.dayoo.com/", ""); 
		rs = rs.replace("蝵�霂捏&#160;(<em id=\"CommentCount_Item\" name=\"CommentCount_Item\">0</em>)", ""); 
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
