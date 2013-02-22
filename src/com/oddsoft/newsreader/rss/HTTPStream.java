package com.oddsoft.newsreader.rss;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.StrictMode;

public class HTTPStream {

	  public InputStream getInputStreamFromUrl(String url) {  
	         try {                        
	            HttpGet httpGet = new HttpGet(url);
	            HttpParams httpParameters = new BasicHttpParams();
	            // Set the timeout in milliseconds until a connection is established.
	            int timeout = 50000;
	            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
	            HttpConnectionParams.setSoTimeout(httpParameters, timeout);
	            
	            if (android.os.Build.VERSION.SDK_INT > 9) {
	                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	                StrictMode.setThreadPolicy(policy);
	              }

	            
	            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	            HttpResponse response = httpClient.execute(httpGet);
	            return response.getEntity().getContent();
	         } 
	         catch (Exception e) {
	                e.printStackTrace();
	         }
	         return null;
	   }
}
