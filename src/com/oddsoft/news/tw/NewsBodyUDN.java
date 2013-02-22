package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyUDN implements INewsBody {
	private static final String TAG = "NewsBodyUDN";
	String url;
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			
			link = link.replace("http://udn.com/rs.html?url=", "");
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"BIG-5");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			url = link;
			if (url.toString().contains("stars")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("background=\"linedot.gif\"")) {
						beginFind = true;
					} else if ("<!--Bottom Of Content Place-->".equals(s.trim())) {
						break;
					}
					
					if (beginFind) {
						sb.append(s.trim());
					}
				}
			}
			else {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("story_author")) {
						beginFind = true;
			//		} else if ("<style type=\"text/css\">".equals(s.trim())) {
					} else if (s.trim().contains("vote_result.jsp")
							|| s.trim().contains("<!-- chgFontSize javascript -->")) {
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
		//rs = rs.replaceAll(" ", "");
		rs = rs.replace("img ", "img");
		rs = rs.replace("imgsrc=\"../../", "img src=\"http://udn.com/NEWS/");
		if (url.toString().contains("money"))
			rs = rs.replace("/magimages/", "http://money.udn.com/magimages/");
		else if (url.toString().contains("stars"))
		{
			rs = rs.replace("font color", "xxx");
			rs = rs.replace("font", "xxx");
			rs = rs.replace("background=\"linedot.gif\">", "");
			rs = rs.replace("imgsrc=\"photoborder_", "xxx");
			rs = rs.replace("imgsrc=\"images/photoborder_", "xxx");
			rs = rs.replace("IMG SRC=\"photoborder_", "xxx");
			rs = rs.replace("<imgsrc=\"", "<img src=\"" + url.toString());
		}
		else if (url.toString().contains("travel"))  {
			rs = rs.replace("imgsrc=\"/magimages/", "img src=\"http://travel.udn.com/magimages/");
			rs = rs.replace("IMG src='/magimages/", "img src='http://travel.udn.com/magimages/");
		}
		else { 
			rs = rs.replace("imgsrc=\"/magimages/", "img src=\"http://mag.udn.com/magimages/");
			rs = rs.replace("imgsrc=\'/magimages/", "img src=\'http://mag.udn.com/magimages/");
			rs = rs.replace("IMG src=\'/magimages/", "img src=\'http://mag.udn.com/magimages/");
			rs = rs.replace("imgsrc='http://mag.udn.com/html/digital/event", "img src='http://mag.udn.com/html/digital/event");
		}
		
		rs = rs.replace("div ", "xxx");
		rs = rs.replace("IFRAME", "xxx");
		rs = rs.replace("iframe", "xxx");
		rs = rs.replace("a href", "ahref");
		rs = rs.replace("bgcolor=\"#ffffff\"", "");	
		rs = rs.replaceAll("style", "xxx");	
		rs = rs.replaceAll("td ", "xxx");
		rs = rs.replace("</table>", "<p>");
		rs = rs.replaceAll("table ", "xxx");
		rs = rs.replaceAll("span ", "xxx");
		rs = rs.replaceAll("tbody", "xxx");
		rs = rs.replaceAll("<tr", "<xxx");
		rs = rs.replaceAll("</tr>", "</xxx>");
		rs = rs.replace(" class", "xxx");
		rs = rs.replace("align", "xxx");
		rs = rs.replace("<object", "<!--");
		rs = rs.replace("</object>", "-->");
		
		rs = "<big>" + rs + "</big>";
		if (Debug.On) {
			Log.d(TAG, "rs="+rs.toString());
		}
		return rs;
	}
	
}
