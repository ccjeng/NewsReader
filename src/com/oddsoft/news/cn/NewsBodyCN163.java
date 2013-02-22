package com.oddsoft.news.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;
import android.util.Log;

public class NewsBodyCN163 implements INewsBody {
	private static final String TAG = "NewsBodyCN163";
	
	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(con.getInputStreamFromUrl(link),"GB2312");
			BufferedReader br = new BufferedReader(isr);
			boolean beginFind = false;
			String s;
			
			while (null != (s = br.readLine())) {
				if (s.trim().contains("<!-- 新闻内容区图片 -->") ||
						s.trim().contains("<div class=\"endContent")) {
					beginFind = true;
				} else if (s.trim().contains("<!-- 分页 -->")) {
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
		rs = rs.replace("跟贴 <span id=\"replycounttop\">0</span> 条</a> <a href=\"http://help.3g.163.com/\">手机看新闻</a>", "");
		rs = rs.replace("a href", "ahref"); 
		rs = rs.replace("<img src=\"http://img1.cache.netease.com/cnews/img07/end_i.gif\"", "<xxx=\"\"");
		rs = rs.replace("<h1 id=\"h1title\">", "<!--");
		rs = rs.replace("</h1>", "-->");
		rs = rs.replace("<iframe", "<!--");
		rs = rs.replace("<img src=\"http://img1.cache.netease.com/", "<xxx=\"");
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
