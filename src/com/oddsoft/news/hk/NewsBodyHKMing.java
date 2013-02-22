package com.oddsoft.news.hk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyHKMing implements INewsBody {
	private static final String TAG = "NewsBodyHKMing";
	private String plink;
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		plink = link;
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"BIG-5");
			BufferedReader br = new BufferedReader(isr);
		
			boolean beginFind = false;
			String s;
			
			if (link.contains("star5")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("img src='../ftp/")) {
						sb.append(s.trim().replace("<img src='../ftp/", "<img SRC='http://ol.mingpao.com/ftp/"));
						break;
					}
				}
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<div id='newscontent'>")) {
						beginFind = true;
					} else if (s.trim().contains("http://ol.mingpao.com/css/content.css")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
			}else {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("content_medium")) {
						beginFind = true;
					} else if (s.trim().contains("supportblock02")) {
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
			Log.d(TAG, "link=" + link.toString());		
		}
		return findContent(sb.toString());
	}
	
	public String findContent(String html) {

		String rs = new String(html);
		rs = rs.replace("<div ", "<xxx");
		rs = rs.replace("</div>", "</xxx>");
		rs = rs.replace("<table", "<xxx");	
		rs = rs.replace("<td", "<xx");
		rs = rs.replace("<tr", "<xx");
		
		if (plink.contains("star5")) {
			rs = rs.replace("<font style='font-size:12pt' style='line-height:160%;'>", "");
			rs = rs.replace("<font ", "<xxx");
			rs = rs.replace("</font>", "");
			rs = rs.replace("<img src", "<xx");
			rs = rs.replace(">©ñ¤j", "><!--");
			rs = rs.replace("±i)<", "--><");
			rs = rs.replace("a href", "ahref");
		}
		
		rs = "<big>" + rs + "</big>";
		if (Debug.On) {
			Log.d(TAG, "rs="+rs.toString());
		}

		return rs;
	}
	
}
