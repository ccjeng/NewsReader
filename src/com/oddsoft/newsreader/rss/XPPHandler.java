package com.oddsoft.newsreader.rss;

//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


public class XPPHandler {

	public static RSSFeed parseRSS(URL url) throws IOException {
    	RSSFeed rssFeed = new RSSFeed();
		//RSSItem item = new RSSItem();
		HTTPStream con = new HTTPStream();
		
		InputStreamReader streamReader = null;
		// perform the synchronous parse 
		try {
		    if (url.toString().contains("stgloballink") 
		       || url.toString().contains("nownews")
		       || url.toString().contains("hkheadline")
		       || url.toString().contains("rthk.hk")
		       || url.toString().contains("mingpao")
		    		) {
		   		   streamReader = new InputStreamReader(con.getInputStreamFromUrl(url.toString()),"big5");  
		    } else if (url.toString().contains("ent.163.com") 
		    		|| url.toString().contains("tech.163.com/")
		    		|| url.toString().contains("money.163.com")) {
		   		   streamReader = new InputStreamReader(con.getInputStreamFromUrl(url.toString()),"GB2312"); 
		    } else {
		   		   streamReader = new InputStreamReader(con.getInputStreamFromUrl(url.toString()),"utf-8");  
		    }

		      /* ����SAXParser���� */ 
		      SAXParserFactory spf = SAXParserFactory.newInstance(); 
		      SAXParser sp = spf.newSAXParser(); 
		      /* ����XMLReader���� */ 
		      XMLReader xr = sp.getXMLReader(); 
		      /* �]�w�۩w�q��MyHandler��XMLReader */ 
		      RSSHandler mRSSHandler = new RSSHandler(); 
		      xr.setContentHandler(mRSSHandler);     
		      /* �ѪRXML */
		      xr.parse(new InputSource(streamReader));
		      /* ���oRSS���D�P���e�C�� */
		      rssFeed =mRSSHandler.getParsedData(); 

		}
		catch (Exception e) {
            e.printStackTrace();
		}

        return rssFeed;
    }
		
}