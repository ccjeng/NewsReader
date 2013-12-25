package com.oddsoft.newsreader.hkapple;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.os.StrictMode;
import com.oddsoft.newsreader.rss.RSSFeed;
import com.oddsoft.newsreader.rss.RSSItem;

public class Feeds {
	private static final String root = "http://hkmagze.serveblog.net/";
	//private static final String root = "http://174.132.129.66/~hkmagz6/";
	
	@SuppressWarnings("unused")
	private static final String TAG = "Feeds";
	Document doc;
	
	public void connect(String link) {
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
              }
			
			doc = Jsoup.connect(link)
						.timeout(10000)
						.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RSSFeed getFeeds(String url) throws URISyntaxException {
        URI uri = new URI(url.toString());
        String request = uri.toASCIIString();
		connect(request);

		RSSFeed rssFeed = new RSSFeed();
		RSSItem item = new RSSItem();

		
		if (doc != null) {
			//Elements ul = doc.getElementsByTag("li");
			//for (Element l : ul) {
				//Elements links = l.getElementsByTag("a");
				Elements links = doc.select("ul li a");

				for (Element k : links) {					
					if (k.attr("href").contains("testart.php")) {
						item = new RSSItem();
						item.setTitle(k.text());
						item.setLink(root + k.attr("href"));
						rssFeed.addItem(item);
					}
				}
			//}
		} else 
			rssFeed = null;
 
		return rssFeed;
	}
	
}
