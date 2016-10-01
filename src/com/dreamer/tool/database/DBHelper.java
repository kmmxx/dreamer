package com.dreamer.tool.database;

import com.dreamer.tool.log.Mlog;
import com.nearme.statistics.rom.business.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to create and upgrade the database.
 * 
 * @author dingji
 * @data 2013-02-23
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	public static final String COL_ID = "_id";
	public static final String APP_NAME = "appName";
	public static final String APP_PACKAGE = "appPackage";
	public static final String APP_VERSION = "appVersion";
	public static final String DURATION = "duration";
	public static final String EVENT_COUNT = "eventCount";
	public static final String EVENT_TIME = "eventTime";
	public static final String CHANNEL = "channel";
	public static final String NET_TPYE = "netType";
	public static final String ACTION_TYPE = "actionType";
	public static final String INSTALL_FLAG = "installFlag";

	public static final String UPLOAD_RETRY_TIME = "uploadRetryTime";

	public static final String FIRST_INSTALL_TIME = "firstInstallTime";
	public static final String LAST_UPDATE_TIME = "lastUpdateTime";

	public static final String PKG_NAME = "pkgName";
	public static final String LAUNCH_COUNT = "launchCount";
	public static final String USAGE_TIME = "usageTime";

	public static final String TABLE_APP_LOG = "app_log_info";
	public static final String APP_LOG_TYPE = "appLogType";
	public static final String APP_LOG_BODY = "appLogBody";

	public static final String APP_START_BODY = "app_start_body";
	public static final String TABLE_APP_START = "app_start_info";// ��������ͳ��sdk��Ӧ����������

	public static final String TABLE_USER_ACTION = "user_action_info";
	public static final String USER_ACTION_BODY = "user_action_body";

	public static final String TABLE_EXCEPTION = "exception_info";
	public static final String EXCEPTION_BODY = "exception_body";

	public static final String TABLE_COMMON = "common_info";
	public static final String COMMON_BODY = "common_body";

	public static final String TABLE_PAGE_VISIT = "page_visit_info";
	public static final String PAGE_VISIT_BODY = "page_visit_body";
	public static final String PAGE_VISIT_TYPE = "page_visit_type";

	public static final String DB_NAME = "nearme_statistic_rom";
	public static final String TABLE_APP_INSTALL = "install_info";
	public static final String TABLE_APP_LAUNCH = "launch_info";
	public static final String TABLE_LAUNCH_HELP_INFO = "launch_help_info";

	public static final String TABLE_APP_LIST = "app_list_info";

	public static final String APP_ITEM_BODY = "appItemBody";
	public static final String APP_ITEM_EVENTTAG = "eventTag";
	private final String[] tables = new String[] { TABLE_APP_INSTALL,
			TABLE_APP_LAUNCH, TABLE_APP_LIST, TABLE_APP_LOG, TABLE_APP_START,
			TABLE_COMMON, TABLE_EXCEPTION, TABLE_LAUNCH_HELP_INFO,
			TABLE_PAGE_VISIT, TABLE_USER_ACTION };

	public static final int DB_VERSION = 12;

	private static DBHelper mInstance;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * Get the class instance.
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Mlog.d("DBHelper-onUpgrade-oldVersion:" + oldVersion
				+ ",newVersion:" + newVersion + ",tables:" + tables.toString());
		upgradeTables(db, tables);

	}

	private void createTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_APP_INSTALL + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + APP_NAME + " Text,"
				+ APP_PACKAGE + " Text," + APP_VERSION + " Text," + CHANNEL
				+ " Text," + EVENT_TIME + " INTEGER," + NET_TPYE + " Text,"
				+ ACTION_TYPE + " INTEGER," + INSTALL_FLAG + " INTEGER,"
				+ UPLOAD_RETRY_TIME + " INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_APP_LAUNCH + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + APP_NAME + " Text,"
				+ APP_PACKAGE + " Text," + APP_VERSION + " Text," + DURATION
				+ " INTEGER," + EVENT_COUNT + " INTEGER," + EVENT_TIME
				+ " INTEGER," + FIRST_INSTALL_TIME + " INTEGER,"
				+ LAST_UPDATE_TIME + " INTEGER," + UPLOAD_RETRY_TIME
				+ " INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_LAUNCH_HELP_INFO + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + PKG_NAME + " Text,"
				+ LAUNCH_COUNT + " INTEGER," + USAGE_TIME + " INTEGER);");
		db.execSQL("CREATE TABLE " + TABLE_APP_LOG + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + APP_LOG_TYPE
				+ " Text," + APP_LOG_BODY + " Text," + EVENT_TIME + " INTEGER,"
				+ UPLOAD_RETRY_TIME + " INTEGER  NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_APP_LIST + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + APP_ITEM_BODY
				+ " Text," + APP_ITEM_EVENTTAG + " Text," + UPLOAD_RETRY_TIME
				+ " INTEGER  NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_APP_START + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + APP_START_BODY
				+ " Text," + EVENT_TIME + " INTEGER," + UPLOAD_RETRY_TIME
				+ " INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_USER_ACTION + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ACTION_BODY
				+ " Text," + EVENT_TIME + " INTEGER," + UPLOAD_RETRY_TIME
				+ " INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_EXCEPTION + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + EXCEPTION_BODY
				+ " Text," + EVENT_TIME + " INTEGER," + UPLOAD_RETRY_TIME
				+ " INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_PAGE_VISIT + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + PAGE_VISIT_TYPE
				+ " Text," + PAGE_VISIT_BODY + " Text," + EVENT_TIME
				+ " INTEGER," + UPLOAD_RETRY_TIME
				+ " INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE " + TABLE_COMMON + " (" + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COMMON_BODY
				+ " Text," + EVENT_TIME + " INTEGER," + UPLOAD_RETRY_TIME
				+ " INTEGER NOT NULL DEFAULT 0);");

	}

	private void deleteTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_INSTALL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_LAUNCH);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAUNCH_HELP_INFO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_LOG);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_START);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ACTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXCEPTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE_VISIT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMON);
	}

	/**
	 * Upgrade tables. In this method, the sequence is: <b>
	 * <p>
	 * [1] Rename the specified table as a temporary table.
	 * <p>
	 * [2] Create a new table which name is the specified name.
	 * <p>
	 * [3] Insert data into the new created table, data from the temporary
	 * table.
	 * <p>
	 * [4] Drop the temporary table. </b>
	 * 
	 * @param db
	 *            The database.
	 * @param tableName
	 *            The table name.
	 * @param columns
	 *            The columns range, format is "ColA, ColB, ColC, ... ColN";
	 */
	protected void upgradeTables(SQLiteDatabase db, String[] tableNames) {
		try {
			if (tableNames == null || tableNames.length == 0) {
				return;
			}
			db.beginTransaction();
			// 1, Rename table.
			for (int i = 0; i < tableNames.length; i++) {
				String tempTableName = tableNames[i] + "_temp";
				String sql = "ALTER TABLE " + tableNames[i] + " RENAME TO "
						+ tempTableName;
				db.execSQL(sql);
			}

			// 2, Create table.
			createTables(db);
			// 3, Load data
			for (int i = 0; i < tableNames.length; i++) {
				String tempTableName = tableNames[i] + "_temp";
				String sql = "INSERT INTO " + tableNames[i] + " ("
						+ getColumnNames(db, tableNames[i]) + ") " + " SELECT "
						+ getColumnNames(db, tableNames[i]) + " FROM "
						+ tempTableName;
				db.execSQL(sql);
				// 4, Drop the temporary table.
				db.execSQL("DROP TABLE IF EXISTS " + tempTableName);
			}

			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	protected String getColumnNames(SQLiteDatabase db, String tableName) {
		String[] columnNames = null;
		Cursor c = null;

		try {
			c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
			if (null != c) {
				int columnIndex = c.getColumnIndex("name");
				if (-1 == columnIndex) {
					return null;
				}

				int index = 0;
				columnNames = new String[c.getCount()];
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					columnNames[index] = c.getString(columnIndex);
					index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != c && !c.isClosed()) {
				c.close();
			}
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < columnNames.length; i++) {
			if (i == columnNames.length - 1) {
				sb.append(columnNames[i]);
			} else {
				sb.append(columnNames[i] + ",");
			}

		}
		LogUtil.d("DBHelper-getColumnNames:" + sb.toString());
		return sb.toString();
	}

}
