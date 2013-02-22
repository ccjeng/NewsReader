package com.oddsoft.newsreader;


import java.util.List;
import com.oddsoft.newsreader.rss.RSSItem;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListAdapter extends ArrayAdapter<RSSItem> {
	private final Activity context;
	private final List<RSSItem> items;
	
	public NewsListAdapter(Activity context, int textViewResourceId,
	   List<RSSItem> list) {
		  super(context, textViewResourceId, list);
		  this.context = context;
		  this.items = list;
	 }

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row
		// layout

		 ViewHolder holder;
			// Recycle existing view if passed as parameter
			// This will save memory and time on Android
			// This only works if the base layout for all classes are the same
		 View rowView = convertView;
   
		 if (rowView == null) {
			 LayoutInflater inflater = context.getLayoutInflater();
			 rowView = inflater.inflate(R.layout.list_item, null, true);
			 holder = new ViewHolder();
			 holder.imageView = (ImageView) rowView.findViewById(R.id.icon);
			 holder.textView = (TextView) rowView.findViewById(R.id.row);
			 rowView.setTag(holder);
		 } else {
			 holder = (ViewHolder) rowView.getTag();
		 }
		 //holder.textView.setText(items.get(position).getTitle());
		 holder.textView.setText(Html.fromHtml(items.get(position).getTitle()));
		 
		 if (position%2 == 0){
			 rowView.setBackgroundColor(0xff101010);
			  }
		 else{
			 rowView.setBackgroundColor(0xff080808);
		 }
		 
		 return rowView;
	}
 
}