package com.dreamer.tool.database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

/**
 * @author kemm
 * 
 */
public class DatabaseManager {

	private ContentResolver mContentResolver = null;

	public static enum DatabaseType {
		READABLE, WRITEABLE,
	};

	public static final int READABLE = 0;
	public static final int WRITEABLE = 1;
	private String TAG = "DatabaseOperation";
	private boolean isPrepare = false;
	private DatabaseHelper mDataBaseHelper;
	private SQLiteDatabase mDatabase;
	private Context mContext;
	private static DatabaseManager mDatabaseManager;
	
	private AtomicInteger mOpenCounter = new AtomicInteger(0);

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance1() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
            if( Build.VERSION.SDK_INT >= 11){
                mDatabase.enableWriteAheadLogging();   
            }
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            if (null != mDatabase && mDatabase.isOpen()) {
                mDatabase.close();
            }
        }
    }

	public static DatabaseManager getInstance() {
		if (mDatabaseManager == null) {
			mDatabaseManager = new DatabaseManager();
		}
		return mDatabaseManager;
	}

	public void prepare(Context mContext) {
		if (isPrepare) {
			return;
		}
		isPrepare = true;
		this.mContext = mContext;
		mContentResolver = mContext.getContentResolver();
	}

	public void initDatabaseHelper(DatabaseType type) {
		if (mDatabase == null || !mDatabase.isOpen()) {
			mDataBaseHelper = new DatabaseHelper(mContext,
					DatabaseInfo.DATABASE_NAME, null,
					DatabaseInfo.DATABASE_VERSION);
			if (type == DatabaseType.READABLE) {
				mDatabase = mDataBaseHelper.getReadableDatabase();
			} else {
				mDatabase = mDataBaseHelper.getWritableDatabase();
			}
		}
	}

	private DatabaseManager() {

	}

	public void setDatabaseHelper(DatabaseHelper mDataBaseHelper) {
		// TODO Auto-generated method stub
		this.mDataBaseHelper = mDataBaseHelper;
	}

	public SQLiteDatabase getSqliteDatabase() {
		return mDatabase;
	}

	// 调用这个可以使大数据的写入更加快速
	public boolean beginTransaction() {
		try {
			mDatabase.beginTransaction();
			return true;
		} catch (Exception e) {
			Log.e(TAG, "error Exception = " + e);
		}
		return false;
	}

	// 调用beginBatabase以后必须调用这个函数才能完成数据的保存
	public boolean endTransaction() {
		boolean ret = false;
		try {
			mDatabase.setTransactionSuccessful();
			ret = true;
		} catch (Exception e) {
			Log.e(TAG, "error Exception = " + e);
		}
		mDatabase.endTransaction();
		return ret;
	}

	public void execSQL(String sql) {
		mDatabase.execSQL(sql);
	}

	public Cursor rawQuery(String sql) {
		return mDatabase.rawQuery(sql, null);
	}

	public void close() {
		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
		}
	}

	public synchronized List<LoveChannelData> queryLoveChannelsData() {
		String columns[] = new String[] {
				// NetworkDatabase.Channels._ID,
				DatabaseInfo.LoveChannels.FREQUENCY,
				DatabaseInfo.LoveChannels.PROGRAM_NUMBER,
				DatabaseInfo.LoveChannels.LOVE };
		Uri myUri = DatabaseInfo.LOVE_CONTENT_URI;
		Cursor cur = mContentResolver.query(myUri, columns, null, null, null);
		List<LoveChannelData> mLoveChannelDatas = new ArrayList<LoveChannelData>();
		if (cur == null)
			return mLoveChannelDatas;
		Log.e("DatabaseOperation", "cur:" + cur.getCount());
		if (cur.moveToFirst()) {
			do {
				LoveChannelData mLoveChannelData = new LoveChannelData();
				mLoveChannelData.setFrequency(cur.getLong(cur
						.getColumnIndex(DatabaseInfo.LoveChannels.FREQUENCY)));
				mLoveChannelData
						.setProgram_number(cur.getInt(cur
								.getColumnIndex(DatabaseInfo.LoveChannels.PROGRAM_NUMBER)));
				mLoveChannelData.setLove_status(cur.getInt(cur
						.getColumnIndex(DatabaseInfo.LoveChannels.LOVE)));
				mLoveChannelDatas.add(mLoveChannelData);
			} while (cur.moveToNext());
		}
		cur.close();
		return mLoveChannelDatas;
	}

	public void insertLoveChannelData(LoveChannelData mLoveChannelData) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DatabaseInfo.LoveChannels.FREQUENCY,
				mLoveChannelData.getFrequency());
		values.put(DatabaseInfo.LoveChannels.PROGRAM_NUMBER,
				mLoveChannelData.getProgram_number());
		values.put(DatabaseInfo.LoveChannels.LOVE,
				mLoveChannelData.getLove_status());
		mContentResolver.insert(DatabaseInfo.LOVE_CONTENT_URI, values);
		values.clear();
	}

	public void deleteLoveChannelData(LoveChannelData mLoveChannelData) {
		mContentResolver.delete(
				DatabaseInfo.LOVE_CONTENT_URI,
				"frequency=" + "'" + mLoveChannelData.getFrequency() + "'"
						+ " AND " + "program_number=" + "'"
						+ mLoveChannelData.getProgram_number() + "'", null);
	}

	public void deleteAllDatabaseData() {
		mContentResolver.delete(DatabaseInfo.LOVE_CONTENT_URI, null, null);
	}

}
