package com.oddsoft.newsreader.rss;

import android.os.Parcel;
import android.os.Parcelable; 

public class Feed implements Parcelable{

	private String title = null; 
	private String link = null; 
	
	public Feed() {  
    }  
	public Feed(String t,String l) {  
        title=t;  
        link=l;   
    } 
	public String getTitle() {
		return title;
	}
	public String getLink() {
		return link;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	public static final Parcelable.Creator<Feed> CREATOR = new Creator<Feed>(){  
		  
        @Override  
        public Feed createFromParcel(Parcel source) {  
            // TODO Auto-generated method stub  
        	Feed cus = new Feed();    
                cus.title = source.readString();    
                cus.link = source.readString();      
                return cus;    
        }  
  
        @Override  
        public Feed[] newArray(int size) {   
            return new Feed[size];  
        }    
          
    }; 
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(title); 
		dest.writeString(link);  
	}

}
