package com.oddsoft.news.hk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyHKheadline implements INewsBody {
	private static final String TAG = "NewsBodyHKheadline";
	private String plink;
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		plink=link;
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"big5");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			if (link.contains("instantnews")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<td class=\"bodytext\">")) {
						beginFind = true;
					} else if (s.trim().contains("/dailynews/images/icon1_01.gif")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
			} else if (link.contains("ent_content")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("</select>")) {
	//				if (s.trim().contains("<td valign=\"top\" class=\"bodytext\">")) {
						beginFind = true;
					} else if (s.trim().contains("/images/icon1_01.gif")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
			} else if (link.contains("travel_content")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("bodytext")) {
						beginFind = true;
					} else if (s.trim().contains("/images/icon1_01.gif")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}	
			} else {  //if (link.contains("dailynews"))
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<!--Start-->")) {
						beginFind = true;
					} else if (s.trim().contains("<option")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
				beginFind = false;
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<div class=\"headlinetitle\"")) {
						beginFind = true;
					} else if (s.trim().contains("/dailynews/images/icon1_01.gif")) {
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
		rs = rs.replace("<li>", ""); 
		rs = rs.replace("</li>", ""); 
		rs = rs.replace("<imgid=", "<img id="); 
		rs = rs.replace("<table", "<xx");	
		rs = rs.replace("<td", "<xx");
		rs = rs.replace("<tr", "<xx");
		rs = rs.replace("</table>", "</xx>");	
		rs = rs.replace("</td>", "</xx>");
		rs = rs.replace("</tr>", "</xx>");
		rs = rs.replace("<divclass=\"headlinetitle\">", "<!--");
		rs = rs.replace("<spanclass=\"newsheadlinetime\">", "--><br>");
		rs = rs.replace("align=\"absmiddle\">", "align=\"absmiddle\"><br><br>");
		rs = rs.replace("</font>", ""); 
		rs = rs.replace("varv2_section=true;", ""); 
		
		if (plink.contains("ent_content")) {
			rs = rs.replace("<imgsrc=\"http://img.hkheadline.com/", "<img src=\"http://img.hkheadline.com/"); 
			rs = rs.replace("<imgsrc=\"http://img2.hkheadline.com/headline/", "<img src=\"http://img2.hkheadline.com/headline/"); 
			rs = rs.replace("<imgsrc=\"/phototext/images/", "<img src=\"http://www.hkheadline.com/phototext/images/");
		}
		if (plink.contains("travel_content")) {
			rs = rs.replace("<imgsrc=\"/gcmt_images/", "<img src=\"http://www.hkheadline.com/gcmt_images/"); 
		}
		
	    rs = "<big>" + rs + "</big>";
	    if (Debug.On) {
	    	Log.d(TAG, rs.toString());
	    }

		return rs;
	}
	
}
