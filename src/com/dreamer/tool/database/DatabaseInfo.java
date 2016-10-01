package com.dreamer.tool.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseInfo {

	public static final String AUTHORITY = "com.dreamer.tool.database";
	public static final String LOVEAUTHORITY = "com.dreamer.tool.database.DatabaseProvider";
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.dreamer.app";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.dreamer.app";
	// database name
	public static final String DATABASE_NAME = "dreamer.db";
	public static String DATABASE_PATH = "/data/data/com.dreamer/";
	public static final int DATABASE_VERSION = 1;
	// table content uri
	public static Uri LOVE_CONTENT_URI = Uri.parse("content://" + LOVEAUTHORITY
			+ "/lovechannels");
	// table name
	public static final String LOVE_TABLE_NAME = "lovechannels";
	public static final int LOVECHANNELS = 11;
	public static final int LOVECHANNELS_ID = 12;
	// create tables string
	public static final String CREATE_LOVECHANNELS_TABLE = "CREATE TABLE "
			+ DatabaseInfo.LOVE_TABLE_NAME + " (" + LoveChannels._ID
			+ " INTEGER PRIMARY KEY," + LoveChannels.FREQUENCY + " TEXT,"
			+ LoveChannels.PROGRAM_NUMBER + " TEXT," + LoveChannels.LOVE
			+ " TEXT);";

	public static final String FREQUENCY_DEFAULT_SORT_ORDER = "frequency ASC";// DESC
	public static final String FREQUENCY_CREATEDDATE = "created";
	public static final String FREQUENCY_MODIFIEDDATE = "modified";

	protected interface LoveColumns {
		public static final String FREQUENCY = "frequency";
		public static final String PROGRAM_NUMBER = "program_number";
		public static final String LOVE = "love_status";
	}

	/**
	 * LoveChannels
	 */
	public static final class LoveChannels implements BaseColumns, LoveColumns {
		public static final String TABLE_NAME = "lovechannels";
	}

}
