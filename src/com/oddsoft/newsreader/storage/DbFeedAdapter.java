package com.oddsoft.newsreader.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbFeedAdapter {
	
	private static final String LOG_TAG = "DbFeedAdapter";
	
	private final Context mCtx;
	private DbHelper mDbHelper;
	private SQLiteDatabase mDb;
    
	private static class DbHelper extends SQLiteOpenHelper {
		@SuppressWarnings("unused")
		private static final String LOG_TAG = "DbHelper";
    	@SuppressWarnings("unused")
		private DbFeedAdapter mDbfa;
        
        DbHelper(DbFeedAdapter dbfa) {
          super(dbfa.mCtx, DbSchema.DATABASE_NAME, null, DbSchema.DATABASE_VERSION);
          mDbfa = dbfa;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DbSchema.BookmarkSchema.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	db.execSQL("DROP TABLE IF EXISTS "+ DbSchema.BookmarkSchema.TABLE_NAME);
			onCreate(db);
        }        
    }
    
	public DbFeedAdapter(Context ctx) {
	    this.mCtx = ctx;
	}

    public DbFeedAdapter open() throws SQLException {
        mDbHelper = new DbHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    	    
    public void close() {
        mDbHelper.close();
    }
    
    public static final String KEY_ROWID = "_id";
	public static final String COLUMN_TAB = "source";
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_TITLE = "title";

	//create
    public long addBookmark(String source, int position, String title) {
    	ContentValues args = new ContentValues();
		args.put(COLUMN_TAB, source);
		args.put(COLUMN_ITEM, position);
		args.put(COLUMN_TITLE, title);
		
		return mDb.insert(DbSchema.BookmarkSchema.TABLE_NAME, null, args);
    }
    
	//update
	public boolean updateBookmark(long rowId, String source, int position, String title) {
		ContentValues args = new ContentValues();
		args.put(COLUMN_TAB, source);
		args.put(COLUMN_ITEM, position);
		args.put(COLUMN_TITLE, title);
		
		return mDb.update(DbSchema.BookmarkSchema.TABLE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	//delete
	public boolean deleteBookmark(int position) {		
		return mDb.delete(DbSchema.BookmarkSchema.TABLE_NAME
				, "_id=" + getBookmarkId(position)
				, null) > 0;
	}
	public boolean deleteBookmarkfromTab(String source, int position) {		
		return mDb.delete(DbSchema.BookmarkSchema.TABLE_NAME
				, "source=? AND item=?"
				, new String[]{source, Long.toString(position)}) > 0;
	}
	
	//retrieve
	public Cursor getBookmarkByTab(String source) {
		try {
			Cursor mCursor = mDb.rawQuery("SELECT title FROM " + DbSchema.BookmarkSchema.TABLE_NAME 
					+ " WHERE source=? ORDER BY _id", new String[]{source});
				        
			if (mCursor != null) {
				if (!mCursor.moveToFirst()){
					return null;
				}
			}
			return mCursor;
		}
		catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public Cursor getBookmark() {
		try {
			Cursor mCursor = mDb.rawQuery("SELECT " + COLUMN_TITLE +" FROM " + DbSchema.BookmarkSchema.TABLE_NAME, null);

			if (mCursor != null) {
				if (!mCursor.moveToFirst()){
					return null;
				}
			}
			return mCursor;
		}
		catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			return null;
		}
	}

	public int getBookmarkId(int position) {
		Cursor cursor = null;
		int i = 0;
		cursor = mDb.rawQuery("SELECT * FROM " + DbSchema.BookmarkSchema.TABLE_NAME, null);
		int id = 0;
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				if (position == i) {
					id = cursor.getInt(cursor.getColumnIndex("_id"));
				}
				cursor.moveToNext();
				i = i + 1;
			}
		} else {
		}

		if (cursor != null)
			cursor.close();

		return id;
	}
	
	public String getBookmarkTab(int position) {		
		Cursor cursor = null;
		String tab = null;
		cursor = mDb.rawQuery("SELECT * FROM " + DbSchema.BookmarkSchema.TABLE_NAME + " WHERE _id=" + getBookmarkId(position), null);
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				tab = cursor.getString(cursor.getColumnIndex("source"));
				cursor.moveToNext();
			}
		} else {
		}

		if (cursor != null)
			cursor.close();

		return tab;
	}
	
	public String getBookmarkItem(int position) {		
		Cursor cursor = null;
		String item = null;
		cursor = mDb.rawQuery("SELECT * FROM " + DbSchema.BookmarkSchema.TABLE_NAME + " WHERE _id=" + getBookmarkId(position), null);
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				item = cursor.getString(cursor.getColumnIndex("item"));
				cursor.moveToNext();
			}
		} else {
		}

		if (cursor != null)
			cursor.close();

		return item;
	}
	public String[] getBookmarkTitle() {		
		Cursor cursor = null;
		int i = 0;
		cursor = mDb.rawQuery("SELECT * FROM " + DbSchema.BookmarkSchema.TABLE_NAME, null);
		String[] title = new String[cursor.getCount()];
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				title[i] = cursor.getString(cursor.getColumnIndex(DbSchema.BookmarkSchema.COLUMN_TITLE));
				cursor.moveToNext();
				i = i + 1;
			}
		} else {
		}

		if (cursor != null)
			cursor.close();
		
		return title;
	}
	
	public boolean checkBookmark(String source, int position) {
		Cursor cursor = null;
		String id = null;
		cursor = mDb.rawQuery("SELECT * FROM " + DbSchema.BookmarkSchema.TABLE_NAME 
				+ " WHERE source=? AND item=?", new String[]{source, Long.toString(position)});
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				id = cursor.getString(cursor.getColumnIndex("_id"));
				cursor.moveToNext();
			}
		} else {
		}
		if (cursor != null)
			cursor.close();

		if (id != null)
			return true;
		else
			return false;
	}
}