package com.oddsoft.newsreader.storage;

import android.provider.BaseColumns;

public final class DbSchema {
	
	public static final String DATABASE_NAME = "news.db";
	public static final int DATABASE_VERSION = 2;
	public static final String SORT_ASC = " ASC";
	public static final String SORT_DESC = " DESC";
	public static final String[] ORDERS = {SORT_ASC,SORT_DESC};
	public static final int OFF = 0;
	public static final int ON = 1;

	public static final class BookmarkSchema implements BaseColumns {
		public static final String TABLE_NAME = "bookmark";
		public static final String COLUMN_TAB = "source";
		public static final String COLUMN_ITEM = "item";
		public static final String COLUMN_TITLE = "title";
		public static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ COLUMN_TAB + " TEXT NOT NULL," 
				+ COLUMN_ITEM + " INTEGER," 
				+ COLUMN_TITLE + " TEXT NOT NULL)"; 
		public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
