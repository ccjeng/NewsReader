package com.oddsoft.news.hk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyHKAppleDaily implements INewsBody {
	private static final String TAG = "NewsBodyHKAppleDaily";
	//private static final String root = "http://news.hotpot.hk/fruit/";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
	        URI uri = new URI(link.toString());
	        String encodedIPAddress = uri.toASCIIString();
  
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(encodedIPAddress),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			while (null != (s = br.readLine())) { 
				//beginFind = true;

				/*		
				if (s.trim().contains("返回上一頁")) {
					break;
				}
*/
				if (s.trim().contains("<h1>")) {
					beginFind = true;
				} else if (s.trim().contains("返回上一頁")) {
					break;
				}
			
				
				if (beginFind) {
					sb.append(s.trim());
				}
			}
			br.close();
			if (Debug.On) {
				Log.d(TAG, "link= " + encodedIPAddress.toString());		
			}
		} catch (Exception e) {
            e.printStackTrace();
        }

		
		return findContent(sb.toString());
	}
	
	public String findContent(String html) {

		String rs = new String(html);

		rs = rs.replace("<a href=\"http://static.apple.nextmedia.com/adoinfile/Section_Logo/16443782.gif\" >", "");
		rs = rs.replace("<img src=\"http://static.apple.nextmedia.com/adoinfile/Section_Logo/16443782.gif\" >", "");
		
		
		rs = rs.replaceAll("<style type=\"text/css\">", "<!--");
		rs = rs.replaceAll("<b>生果日報網上版", "-->生果日報網上版");
		
		
		rs = rs.replaceAll("<br /><br />", "");
		rs = rs.replaceAll(" ", "");
		rs = rs.replace("<h1>", "<!--");
		rs = rs.replace("</h1>", "-->");
		rs = rs.replace("<hr>", "<xx>");
		
	
		rs = rs.replace("img.php?server=", "http://");	
		rs = rs.replace("imgsrc", "img src");
		rs = rs.replace("<img", "<img ");
			
	//	rs = rs.replace("jpgalt", "jpg alt");
	//	rs = rs.replace("&type=jpg", "");
	//	rs = rs.replace("gifalt", "gif alt");
	//	rs = rs.replace("&type=gif", "");
		
////		rs = rs.replace("&path=images", "/images");
////		rs = rs.replace("&path=/images", "/images");
////		rs = rs.replace("&path=", "/");
		rs = rs.replace("<p", "<big><p");
		rs = rs.replace("</p>", "</p></big>");
		rs = rs.replace("</em>", "<br>");
		rs = rs.replace("<big>", "<xx>");
		rs = rs.replace("</big>", "</xx>");
		
		
/*		if (rs.contains("jpg")) {
			rs = rs.replaceAll("&code=([0-9a-z]){1,32}&type=jpg","");
		}
		if (rs.contains("gif")) {
			rs = rs.replaceAll("&code=([0-9a-z]){1,32}&type=gif","");
		}
		
		rs = rs.replace("[1]", "");
*/		
		rs = "<big>" + rs + "</big>";
		rs = rs.replace("/small/", "/large/");
		rs = rs.replace("<table", "<xx");	
		rs = rs.replace("<td", "<xx");
		rs = rs.replace("<tr", "<xx");
		rs = rs.replace("<h2>", "<p>");
		rs = rs.replace("</h2>", "<p>");
		
		if (Debug.On) {
			Log.d(TAG, rs.toString());
		}

		return rs;
	}
	
}
