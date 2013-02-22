package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyCNYes implements INewsBody {
	private static final String TAG = "NewsBodyCNYes";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) { 
				if (s.trim().contains("<span class=\"info\">")) {
					beginFind = true;
				} else if (s.trim().contains("<div class=\"gadstxtmcont2\"")) {
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
		
		rs = rs.replace("網友評論", "<p><p>").replace("條", "").replace("我來說兩句", "");	
		rs = rs.replace("<script language=\"javascript\" type=\"text/javascript\">", "<!--");
		rs = rs.replace("</script>", "-->");
		rs = rs.replaceAll(" ", "");
		rs = rs.replace("imgsrc", "img src");
		rs = rs.replace("<span", "<xxx");
		rs = rs.replace("</span>", "</xxx>");
		//rs = rs.replace("a href", "ahref");
		rs = rs.replace("<em ", "<xxx");
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
