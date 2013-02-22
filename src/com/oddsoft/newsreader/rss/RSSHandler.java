package com.oddsoft.newsreader.rss;

import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler; 

//import android.util.Log;

public class RSSHandler extends DefaultHandler {

	  private boolean in_item = false;
	  private boolean in_title = false;
	  private boolean in_link = false;
	  private boolean in_desc = false;
	  private boolean in_date = false;
	  private RSSFeed feed;
	  private RSSItem item;
	  private String title="";
	  private StringBuffer buf=new StringBuffer();
	  /* 將轉換成List<News>的XML資料回傳 */
	  public RSSFeed getParsedData()
	  { 
	    return feed; 
	  }
	  /* 將解析出的RSS title回傳 */
	  public String getRssTitle()
	  { 
	    return title; 
	  }
	  /* XML文件開始解析時呼叫此method */
	  @Override 
	  public void startDocument() throws SAXException
	  { 
		  feed = new RSSFeed(); 
	  } 
	  /* XML文件結束解析時呼叫此method */
	  @Override 
	  public void endDocument() throws SAXException
	  {
	  } 
	  /* 解析到Element的開頭時呼叫此method */
	  @Override 
	  public void startElement(String namespaceURI, String localName, 
	               String qName, Attributes atts) throws SAXException
	  { 
	    if (localName.equals("item"))
	    { 
	      this.in_item = true;
	      /* 解析到item的開頭時new一個News物件 */
	      item=new RSSItem();
	    }
	    else if (localName.equals("title"))
	    { 
	      if(this.in_item)
	      {
	        this.in_title = true;
	      }
	    }
	    else if (localName.equals("link"))
	    { 
	      if(this.in_item)
	      {
	        this.in_link = true;
	      }
	    }
	    else if (localName.equals("description"))
	    { 
	      if(this.in_item)
	      {
	        this.in_desc = true;
	      }
	    }
	    else if (localName.equals("pubDate"))
	    { 
	      if(this.in_item)
	      {
	        this.in_date = true;
	      }
	    } 
	  }
	  /* 解析到Element的結尾時呼叫此method */
	  @Override 
	  public void endElement(String namespaceURI, String localName,
	                         String qName) throws SAXException
	  { 
	    if (localName.equals("item"))
	    { 
	      this.in_item = false;
	      /* 解析到item的結尾時將News物件寫入List中 */
	      feed.addItem(item);
	    }
	    else if (localName.equals("title"))
	    { 
	      if(this.in_item)
	      {
	        /* 設定News物件的title */
	    	item.setTitle(buf.toString().trim());
	        buf.setLength(0);
	        this.in_title = false;
	      }
	    }
	    else if (localName.equals("link"))
	    { 
	      if(this.in_item)
	      {
	        /* 設定News物件的link */
	    	item.setLink(buf.toString().trim());
	        buf.setLength(0);
	        this.in_link = false;
	      }
	    }
	    else if (localName.equals("description"))
	    { 
	      if(in_item)
	      {
	        /* 設定News物件的description */
	    	item.setDescription(buf.toString().trim());
	        buf.setLength(0);
	        this.in_desc = false;
	      }
	    }
	    else if (localName.equals("pubDate"))
	    { 
	      if(in_item)
	      {
	        /* 設定News物件的pubDate */
	    	item.setPubDate(buf.toString().trim());
	        buf.setLength(0);
	        this.in_date = false;
	      }
	    }
	    else buf.setLength(0);
	    /*
	    else if (localName.equals("guid"))
	    {
	    	buf.setLength(0);
	    }*/	
	  } 
	  /* 取得Element的開頭結尾中間夾的字串 */
	  @Override 
	  public void characters(char ch[], int start, int length)
	  { 
	    if(this.in_item)
	    {
	      /* 將char[]加入StringBuffer */
	      buf.append(ch,start,length);
	    }
	  } 	  
}
