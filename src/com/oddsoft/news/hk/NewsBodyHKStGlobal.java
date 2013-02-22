package com.oddsoft.news.hk;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oddsoft.news.INewsBody;
import com.oddsoft.newsreader.Debug;
import com.oddsoft.newsreader.rss.HTTPStream;

import android.util.Log;

public class NewsBodyHKStGlobal implements INewsBody {
	private static final String TAG = "NewsBodyHKStGlobal";

	public String loadHtml(String link) throws IOException {
		HTTPStream con = new HTTPStream();
		Document doc = null;
		try {
			doc = Jsoup.parse(con.getInputStreamFromUrl(link), "UTF-8", link);
		}
		catch (Exception e) {
            e.printStackTrace();
		} 
		
		StringBuilder sb = new StringBuilder();

		if (doc != null) {
			Element date = doc.select("span.font_hui.float_l").first();
			if (date != null) {
				sb.append(date.text());
				sb.append("<br>");
			}
			Elements paragraph = doc.select("div#content_zoom > p");
			for (Element cont : paragraph) {
				sb.append(cont.html());
				sb.append("<br><br>");
			}
		}
		if (Debug.On) {
			Log.d(TAG, "link= " + link.toString());
		}
		
		return findContent(sb.toString());
	}
	
	public String findContent(String html) {

		String rs = new String(html);
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
