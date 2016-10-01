package com.dreamer.tool.database;

import java.util.HashMap;

import com.dreamer.tool.database.DatabaseInfo.LoveChannels;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DatabaseProvider extends ContentProvider {

	private static HashMap<String, String> lovechannelsMap;
	private static final UriMatcher mUriMatcher;
	private DatabaseHelper mDataBaseHelper;
	private String TAG = "DatabaseProvider";
	private SQLiteDatabase mDatabase;

	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(DatabaseInfo.LOVEAUTHORITY, "lovechannels",
				DatabaseInfo.LOVECHANNELS);
		mUriMatcher.addURI(DatabaseInfo.LOVEAUTHORITY, "lovechannels/#",
				DatabaseInfo.LOVECHANNELS_ID);

		lovechannelsMap = new HashMap<String, String>();
		lovechannelsMap.put(LoveChannels.FREQUENCY, LoveChannels.FREQUENCY);
		lovechannelsMap.put(LoveChannels.LOVE, LoveChannels.LOVE);
		lovechannelsMap.put(LoveChannels.PROGRAM_NUMBER,
				LoveChannels.PROGRAM_NUMBER);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		int count;
		switch (mUriMatcher.match(uri)) {
		case DatabaseInfo.LOVECHANNELS:
			count = getWritableDatabase().delete(DatabaseInfo.LOVE_TABLE_NAME,
					where, whereArgs);
			break;
		case DatabaseInfo.LOVECHANNELS_ID:
			String loveId = uri.getPathSegments().get(1);
			count = getWritableDatabase().delete(
					DatabaseInfo.LOVE_TABLE_NAME,
					LoveChannels._ID
							+ "="
							+ loveId
							+ (!TextUtils.isEmpty(where) ? "AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unkown URI =" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		getWritableDatabase().close();
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (mUriMatcher.match(uri)) {
		case DatabaseInfo.LOVECHANNELS:
			return DatabaseInfo.CONTENT_TYPE;
		case DatabaseInfo.LOVECHANNELS_ID:
			return DatabaseInfo.CONTENT_ITEM_TYPE;
		default:
			throw new SQLException("getType Failed to insert row into " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initValues) {
		// TODO Auto-generated method stub
		if (mUriMatcher.match(uri) != DatabaseInfo.LOVECHANNELS) {
			throw new IllegalArgumentException("Unknown URI =" + uri);
		}
		ContentValues values;
		if (initValues != null) {
			values = new ContentValues(initValues);
		} else {
			values = new ContentValues();
		}
		if (mUriMatcher.match(uri) == DatabaseInfo.LOVECHANNELS) {
			Long rowId = getWritableDatabase().insert(
					DatabaseInfo.LOVE_TABLE_NAME, null, values);
			if (rowId > 0) {
				Uri frequencyUri = ContentUris.withAppendedId(
						DatabaseInfo.LOVE_CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(frequencyUri,
						null);
				return frequencyUri;
			}
			getWritableDatabase().close();
		}
		throw new SQLException("Failed to insert row into " + uri);
	}


	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onCreate");
		mDataBaseHelper = new DatabaseHelper(getContext(),
				DatabaseInfo.DATABASE_NAME, null, DatabaseInfo.DATABASE_VERSION);
		return true;
	}

	public SQLiteDatabase getReadableDatabase() {
		if (mDatabase == null || !mDatabase.isOpen()) {
			mDatabase = mDataBaseHelper.getReadableDatabase();
		}
		return mDatabase;
	}

	public  SQLiteDatabase getWritableDatabase() {
		if (mDatabase == null || !mDatabase.isOpen()) {
			mDatabase = mDataBaseHelper.getWritableDatabase();
		}
		return mDatabase;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (mUriMatcher.match(uri)) {
		case DatabaseInfo.LOVECHANNELS:
			qb.setTables(DatabaseInfo.LOVE_TABLE_NAME);
			qb.setProjectionMap(lovechannelsMap);
			break;
		case DatabaseInfo.LOVECHANNELS_ID:
			qb.setTables(DatabaseInfo.LOVE_TABLE_NAME);
			qb.setProjectionMap(lovechannelsMap);
			qb.appendWhere(DatabaseInfo.LoveChannels._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI=" + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			// orderBy = NetworkDatabase.FREQUENCY_DEFAULT_SORT_ORDER;
			orderBy = null;
		} else {
			orderBy = sortOrder;
		}
		Cursor c = qb.query(getReadableDatabase(), projection, selection,
				selectionArgs, null, null, null);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		int count;
		switch (mUriMatcher.match(uri)) {
		case DatabaseInfo.LOVECHANNELS:
			count = getWritableDatabase().update(DatabaseInfo.LOVE_TABLE_NAME,
					values, where, whereArgs);
			break;
		case DatabaseInfo.LOVECHANNELS_ID:
			String loveId = uri.getPathSegments().get(1);
			count = getWritableDatabase().update(
					DatabaseInfo.LOVE_TABLE_NAME,
					values,
					DatabaseInfo.LoveChannels._ID
							+ "="
							+ loveId
							+ (!TextUtils.isEmpty(where) ? "AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unkown URI =" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		getWritableDatabase().close();
		return count;
	}

}
