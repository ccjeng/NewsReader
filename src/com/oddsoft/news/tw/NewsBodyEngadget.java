package com.oddsoft.news.tw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyEngadget implements INewsBody {
	private static final String TAG = "NewsBodyEngadget";
	private String plink;
	public String loadHtml(String link) throws IOException {

		plink = link;
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			if (link.toString().contains("android0Bcool3c")) { //Android
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("node-content")
							|| s.trim().contains("<div class=\"content clear-block\">")) {
						beginFind = true;
					} else if (s.trim().contains("comment_forbidden")) {
						break;
					} else if (s.trim().contains("linkwithin")) {
						break;
					} else if (s.trim().contains("<script src=\"http://www.linkwithin.com/widget.js\"></script>")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
			} else if (link.toString().contains("her")      //her.cool3c.com
						|| link.toString().contains("car0Bcool3c")) {   //car
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<div class=\"submitted\">")) {
						beginFind = true;
					} else if (s.trim().contains("http://www.facebook.com/ms.howeasy")) {
						break;	
					} else if (s.trim().contains("comment_forbidden")) {
						break;
					} else if (s.trim().contains("fbconnect_like")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}	
			} else if (link.toString().contains("cool3c")) {
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("<div class=\"submitted\">")) {
						beginFind = true;
					} else if (s.trim().contains("<div class=\"links clearfix\">")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}	
		    } else { //Engadget
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("byline")) {
						beginFind = true;
					} else if (s.trim().contains("posttags")) {
						break;
					}
					if (beginFind) {
						sb.append(s.trim());
					}
				}
				//readercomments
				beginFind = false;
				while (null != (s = br.readLine())) { 
					if (s.trim().contains("readercomments")) {
						beginFind = true;
					} else if (s.trim().contains("function reportComment")) {
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
		rs = rs.replaceAll(" ", "");
		rs = rs.replace("<img", "<img ");
		rs = rs.replace("color:#000000;", "");
		
		if (plink.toString().contains("engadget")) {
			rs = rs.replace("<divclass=\"post\">", "<!--");
			rs = rs.replace("<pclass=\"byline\">", "-->");
		}
		if (plink.toString().contains("engadget")) {
			rs = rs.replace("<img src=\"http://www.blogsmithcdn.com/avatar", "<imgsrc=\"http://www.blogsmithcdn.com/avatar");
			rs = rs.replace("<h3id=\"recentheadlines\">", "<!--");
			rs = rs.replace("<h2id=\"readercomments\">", "-->");
			rs = rs.replace("<img src=\"/media/", "<imgsrc=\"/media/");
			rs = rs.replace("¦^ÂÐ</a>", "");
		}
		
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
