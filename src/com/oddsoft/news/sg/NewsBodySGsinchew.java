package com.oddsoft.news.sg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodySGsinchew implements INewsBody {
	private static final String TAG = "NewsBodySGsinchew";
	String plink;
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		plink = link;
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			if (link.contains("ent.sinchew-i")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<ul class=\"links inline\"")) {
						beginFind = true;
					} else if (s.trim().contains("class=\"fivestar-widget") 
							|| s.trim().contains("fivestar-feedback-enabled") ) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
			}else if (link.contains("www.sinchew-i")) { //real-time news
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<ul class=\"links inline\">")) {
						beginFind = true;
					} else if (s.trim().contains("<!-- Bottom_News_Banner_Sci -->")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}	
			   }
			}else {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<ul class=\"links inline\"")) {
						beginFind = true;
					} else if (s.trim().contains("<!-- Bottom_News_Banner_Sci -->") 
							|| s.trim().contains("<!--<span class=\"field-item\"></span><br><br>-->")) {
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
		rs = rs.replace(" ", "");	
		rs = rs.replace("<a href", "<ahref"); 
		rs = rs.replace("<a id", "<xx"); 
		rs = rs.replace("<img", "<img "); 
		if (plink.contains("biz.sinchew-i.com")) {
			rs = rs.replace("<img src=\"/files/preview/"
					, "<img src=\"http://biz.sinchew-i.com/files/preview/");
			rs = rs.replace(".biz", ".biz "); 
		}
		else if (plink.contains("ent.sinchew-i")) {
			rs = rs.replace("<img src=\"/files/preview/"
					, "<img src=\"http://ent.sinchew-i.com/files/preview/");
			//rs = rs.replace("</script>", "-->"); 
		}	
		else if (plink.contains("mykampung.sinchew")) {
				rs = rs.replace("<img src=\"/files/preview/"
						, "<img src=\"http://mykampung.sinchew.com.my/files/preview/");
				//rs = rs.replace("</script>", "-->");	
		} else {
			rs = rs.replace("<img src=\"/files/preview/"
					, "<img src=\"http://www.sinchew.com.my/files/preview/");
			
		}
		rs = rs.replace("<scripttype=\"text/javascript\">", "<!--");
		rs = rs.replace("<scripttype='text/javascript'>", "<!--");		
		rs = rs.replace("<scriptlanguage=\"javascript\">", "<!--"); 
		rs = rs.replace("</script>", "-->"); 
		
		rs = rs.replace("</font>", "</xx>"); 
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
