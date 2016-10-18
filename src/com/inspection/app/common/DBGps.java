package com.inspection.app.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.os.Environment;
import java.io.File;

/**
 * GPS本地数据
 * @author liuyx
 * @created by 2014/10/14
 */
public class DBGps {

	// private static final String dbname = "gpsdata.db";
	private final Context ct;
	private static final String dbpath = Environment
			.getExternalStorageDirectory().getPath() + "/gpsdata.db";
	private SQLiteDatabase db;

	/*
	 * private SQLiteDB sdb;
	 * 
	 * private static class SQLiteDB extends SQLiteOpenHelper { public
	 * SQLiteDB(Context context) { super(context, dbname, null, 1); }
	 * 
	 * @Override public void onCreate(SQLiteDatabase sdb) { //建表 sdb.execSQL(
	 * "create table tab_gps (infotype integer,latitude integer,longitude integer,high double,direct double,speed double,gpstime date);"
	 * ); }
	 * 
	 * @Override public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int
	 * newVersion) { sdb.execSQL("drop table if exists tab_gps"); onCreate(sdb);
	 * } }
	 */

	// 初始化数据库
	public DBGps(Context context) {
		ct = context;
		createDB();
		// sdb = new SQLiteDB(ct);
	}

	private void createDB() {
		File f = new File(dbpath);
		if (!f.exists()) {
			db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
			db.execSQL("create table tab_gps (infotype integer,latitude integer,longitude integer,high double,direct double,speed double,gpstime date);");
			db.execSQL("create table tab_gpsstatus (time date,status integer);");
		} else
			db = SQLiteDatabase.openDatabase(dbpath, null,
					SQLiteDatabase.OPEN_READWRITE);
	}

	// 打开数据库
	/*
	 * public void openDB() { db = sdb.getWritableDatabase(); }
	 */

	public void closeDB() {
		// sdb.close();
		db.close();
	}

	public boolean addGpsData(GpsData cdata) {
		boolean result = true;
		try {
			String StrSql = String
					.format("insert into tab_gps (infotype,latitude,longitude,high,direct,speed,gpstime) values (%d,%d,%d,%.1f,%.1f,%.1f,'%s')",
							cdata.InfoType, cdata.Latitude, cdata.Longitude,
							cdata.High, cdata.Direct, cdata.Speed,
							cdata.GpsTime);
			db.execSQL(StrSql);
			result = true;
		} catch (Exception e) {
			result = false;
			Toast.makeText(ct, "保存GPS数据失败:" + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
		return result;
	}

	public boolean addGpsStatusData(String gpstime, int status) {
		boolean result = true;
		try {
			String StrSql = String.format(
					"insert into tab_gpsstatus (time,status) values ('%s',%d)",
					gpstime, status);
			db.execSQL(StrSql);
			result = true;
		} catch (Exception e) {
			result = false;
			Toast.makeText(ct, "保存GPS状态失败:" + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
		return result;
	}
}
