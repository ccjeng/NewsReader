package com.oddsoft.news.cn;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;

import android.util.Log;

public class NewsBodyCNSohu implements INewsBody {
	private static final String TAG = "NewsBodyCNSohu";

	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		//Document doc = Jsoup.parse(con.getInputStreamFromUrl(link), "GB2312", link);
		
		Document doc = null;
		try {
			doc = Jsoup.parse(con.getInputStreamFromUrl(link), "GB2312", link);
			/*
			doc = Jsoup.connect(link)
						.timeout(5000)
						.get();*/
		}
		catch (Exception e) {
            e.printStackTrace();
		} 				
		
		StringBuilder sb = new StringBuilder();
	
		if (doc != null) {
			Element sourceTime = doc.select("div.date span.c").first();
			if (sourceTime != null) {
				sb.append(sourceTime.html());
				sb.append("<br>");
			}
			sourceTime = doc.select("div.sourceTime").first();
			if (sourceTime != null) {
				sb.append(sourceTime.html());
				sb.append("<br>");
			}
			
			Element sohu_content = doc.select("div#sohu_content").first();
			if (sohu_content != null) {
				sb.append(sohu_content.html());
			}
			sohu_content = doc.select("div#contentText").first();
			if (sohu_content != null) {
				sb.append(sohu_content.html());
				sb.append("<br>");
			}
		}

		if (Debug.On) {
			Log.d(TAG, "link= " + link.toString());
		}
		return findContent(sb.toString());
	}
	
	public String findContent(String html) {
		String rs = new String(html);
		rs = rs.replaceAll(" ", "");
		rs = rs.replace("img", "img ");
		rs = rs.replace("<script", "<!--");
		rs = rs.replace("</script>", "-->");
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
