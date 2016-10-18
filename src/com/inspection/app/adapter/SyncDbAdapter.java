package com.inspection.app.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.inspection.app.bean.BugPatrol;
import com.inspection.app.bean.Param;
import com.inspection.app.bean.PatrolBugPhoto;
import com.inspection.app.bean.PatrolPersonPath;
import com.inspection.app.bean.PatrolTaskPoint;
import com.inspection.app.bean.PipemodeCenterLineStake;
import com.inspection.app.bean.Subline;
import com.inspection.app.bean.User;
import com.inspection.app.common.AppContext;


/**
 * 同步本地数据库
 * @author liuyx
 * @created 2014/11/5
 */
public class SyncDbAdapter {
	
	private static final String DB_NAME = "INSPEC_GPS.db";//表空间
	private static final String DB_TABLE_USER = "USER_INFO";//用户信息
	private static final String DB_TABLE_PATROL_PARAM = "PATROL_PARAM";//获取巡检参数
	private static final String DB_TABLE_PATROL_BUG = "PATROL_BUG";//获取缺陷报告类型
	private static final String DB_TABLE_PATROL_SUBLINE = "PATROL_SUBLINE";//管线表
	private static final String DB_TABLE_PATROL_PERSON_PATH = "PATROL_PERSON_PATH";//巡检表
	private static final String DB_TABLE_PILE_POINT = "PILE_POINT";//桩点
	private static final String DB_TABLE_TASK_POINT = "TASK_POINT";//任务作业点
	private static final String DB_TABLE_PATROL_BUGPHOTO = "PATROL_BUGPHOTO";//上传照片表
	
	//用户表
	public static final String KEY_ID = "_id";
	public static final String NAME = "NAME";//用户名
	public static final String PASSWORD = "PASSWORD";//密码
	
	//巡检参数表
	public static final String PARAMNAME = "PARAMNAME";//参数编码
	public static final String PARAMVALUE = "PARAMVALUE";//参数值
	public static final String CHNAME = "CHNAME";//参数名称
	public static final String REMARK = "REMARK";//参数备注
	
	//管线
	public static final String LINE_ID = "line_id";
	public static final String BEGIN = "begin";
	public static final String END = "end";
	
	//巡检表
	private static final String PERSON_ID = "PERSON_ID";//当前巡检人
	private static final String LONGITUDE = "LONGITUDE";//经度
	private static final String LATITUDE = "LATITUDE";//纬度
	private static final String HEIGHT = "HEIGHT";//海拔
	private static final String PATH_POINT_TIME = "PATH_POINT_TIME";//上传时间
	private static final String PRINCIPAL_TYPE = "PRINCIPAL_TYPE";//是否是作业点（路径点和作业点）
	private static final String PRINCIPAL_ID = "PRINCIPAL_ID";//作业点的ID
	private static final String BUG_ID = "BUG_ID";//缺陷报告ID
	private static final String IS_SUCCSEND = "IS_SUCCSEND";//是否上传成功(和服务端进行交互)
	private static final String SPEED = "SPEED";//巡检的速度
	private static final String PIPEDEVIATION = "PIPEDEVIATION";//距离最近作业点的距离
	private static final String PHOTO_NAME = "PHOTO_NAME";//上传照片的名称
	
	//管线桩点
	private static final String SUBLINE_ID = "SUBLINE_ID";
	private static final String BID = "BID";
	private static final String SERIAL_NO = "SERIAL_NO";
	private static final String STAKE_NO = "STAKE_NO";
	private static final String X = "X";
	private static final String Y = "Y";
	
	//巡检作业点
	private static final String TASK_ID = "TASK_ID";
	
	//缺陷报告
	private static final String BUGID = "BUGID";
	
	//上传照片
	private static final String BUG_REPORT_ID = "BUG_REPORT_ID";//关联report表的字段
	private static final String CONTENT = "CONTENT";//拍照缺陷内容项
	private static final String TIME = "TIME";//拍照时间
	private static final String ISUPLOAD = "ISUPLOAD";//是否上传
	private static final String STATUS = "STATUS";//状态
	
	
	private static final int DB_VERSION = 1;
	// 设置全局变量
	AppContext appContext;
	public SQLiteDatabase db;
	public Context context;
	public DBOpenHelper dbOpenHelper;
	
	public SyncDbAdapter(Context _context) {
		context = _context;
		appContext = ((AppContext) context.getApplicationContext());
	}
	
	/** 关闭数据库 Close the database */
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	/** 打开数据库 Open the database */
	public void open() throws SQLiteException {
		dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getWritableDatabase();
		} finally{
			db = dbOpenHelper.getWritableDatabase();
		}
	}
	
	/**
	 * 添加用户信息
	 * @param user
	 * @return
	 */
	public int insertUser(User user) {
		ContentValues newValues = new ContentValues();
		newValues.put(NAME, user.name);
		newValues.put(PASSWORD, user.password);
		return Integer.valueOf((int) db.insert(DB_TABLE_USER, null, newValues));
	}
	/**
	 * 查询用户信息
	 * @return
	 */
	public User[] queryUser() {
		User[] user = null;
		Cursor results = null;
		String table = DB_TABLE_USER;
		try {
			/*String sql  = "select NAME,PASSWORD from " + table; 
	        results = db.rawQuery(sql, null); */
	        results = db.query(table, new String[] { KEY_ID, NAME, PASSWORD }, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			user = ConvertToUser(results);
			return user;
		}
	}
	/**
	 * 根据用户名和密码查询用户信息
	 * @return
	 */
	public User[] queryUserByUserNameAndPsd(String name,String password) {
		User[] user = null;
		Cursor results = null;
		String table = DB_TABLE_USER;
		try {
			/*String sql  = "select NAME,PASSWORD from " + table; 
	        results = db.rawQuery(sql, null); */
	        results = db.query(table, new String[] { KEY_ID, NAME, PASSWORD }, "NAME=? and PASSWORD=?", new String[]{name,password}, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			user = ConvertToUser(results);
			return user;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private User[] ConvertToUser(Cursor cursor) {
		User[] user = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					user = new User[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						user[i] = new User();
						user[i].name = cursor.getString(cursor.getColumnIndex(NAME));
						user[i].password = cursor.getString(cursor.getColumnIndex(PASSWORD));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return user;
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 删除用户数据
	 * @return
	 */
	public long deleteUserData() {
		long flags = 0;
		flags = db.delete(DB_TABLE_USER, null, null);
		return flags;
	}
	
	
	/**
	 *  添加巡检参数
	 * @param param
	 * @return
	 */
	public int insertParam(Param param) {
		ContentValues newValues = new ContentValues();
		newValues.put(PARAMNAME, param.paramName);
		newValues.put(PARAMVALUE, param.paramValue);
		newValues.put(CHNAME, param.chName);
		newValues.put(REMARK, param.remark);
		return Integer.valueOf((int) db.insert(DB_TABLE_PATROL_PARAM, null, newValues));
	}
	/**
	 * 查询巡检参数
	 * @return
	 */
	public Param[] queryParam() {
		Param[] param = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_PARAM;
		try {
	        results = db.query(table, new String[] { KEY_ID, PARAMNAME, PARAMVALUE, CHNAME, REMARK }, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			param = ConvertToParam(results);
			return param;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private Param[] ConvertToParam(Cursor cursor) {
		Param[] param = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					param = new Param[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						param[i] = new Param();
						param[i].paramName = cursor.getString(cursor.getColumnIndex(PARAMNAME));
						param[i].paramValue = cursor.getString(cursor.getColumnIndex(PARAMVALUE));
						param[i].chName = cursor.getString(cursor.getColumnIndex(CHNAME));
						param[i].remark = cursor.getString(cursor.getColumnIndex(REMARK));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return param;
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 删除巡检参数数据
	 * @return
	 */
	public long deleteParamData() {
		long flags = 0;
		flags = db.delete(DB_TABLE_PATROL_PARAM, null, null);
		return flags;
	}
	
	/**
	 *  添加管线subline_id表
	 * @param param
	 * @return
	 */
	public int insertSubline(Subline bean) {
		ContentValues newValues = new ContentValues();
		newValues.put(NAME, bean.name);
		newValues.put(LINE_ID, bean.line_id);
		newValues.put(BEGIN, bean.begin);
		newValues.put(END, bean.end);
		return Integer.valueOf((int) db.insert(DB_TABLE_PATROL_SUBLINE, null, newValues));
	}
	/**
	 * 查询管线subline_id表
	 * @return
	 */
	public Subline[] querySubline() {
		Subline[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_SUBLINE;
		try {
	        results = db.query(table, new String[] { KEY_ID, NAME, LINE_ID, BEGIN, END }, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToSubline(results);
			return bean;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private Subline[] ConvertToSubline(Cursor cursor) {
		Subline[] bean = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					bean = new Subline[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						bean[i] = new Subline();
						bean[i].name = cursor.getString(cursor.getColumnIndex(NAME));
						bean[i].line_id = cursor.getString(cursor.getColumnIndex(LINE_ID));
						bean[i].begin = cursor.getString(cursor.getColumnIndex(BEGIN));
						bean[i].end = cursor.getString(cursor.getColumnIndex(END));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return bean;
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 删除管线subline_id表
	 * @return
	 */
	public long deleteSublineData() {
		long flags = 0;
		flags = db.delete(DB_TABLE_PATROL_SUBLINE, null, null);
		return flags;
	}
	
	/**
	 *  添加巡检表数据
	 * @param param
	 * @return
	 */
	public int insertPatrolPersonPath(PatrolPersonPath bean) {
		ContentValues newValues = new ContentValues();
		newValues.put(PERSON_ID, bean.PERSON_ID);
		newValues.put(LONGITUDE, bean.LONGITUDE);
		newValues.put(LATITUDE, bean.LATITUDE);
		newValues.put(HEIGHT, bean.HEIGHT);
		newValues.put(PATH_POINT_TIME, bean.PATH_POINT_TIME);
		newValues.put(PRINCIPAL_TYPE, bean.PRINCIPAL_TYPE);
		newValues.put(PRINCIPAL_ID, bean.PRINCIPAL_ID);
		newValues.put(BUG_ID, bean.BUG_ID);
		newValues.put(IS_SUCCSEND, bean.IS_SUCCSEND);
		newValues.put(SPEED, bean.SPEED);
		newValues.put(PIPEDEVIATION, bean.PIPEDEVIATION);
		newValues.put(PHOTO_NAME, bean.PHOTO_NAME);
		return Integer.valueOf((int) db.insert(DB_TABLE_PATROL_PERSON_PATH, null, newValues));
	}
	/**
	 * 查询巡检表
	 * @return
	 */
	public PatrolPersonPath[] queryPatrolPersonPath() {
		PatrolPersonPath[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_PERSON_PATH;
		try {
	        results = db.query(table, new String[] { KEY_ID, PERSON_ID, LONGITUDE, LATITUDE, HEIGHT, 
	        		PATH_POINT_TIME, PRINCIPAL_TYPE, PRINCIPAL_ID, BUG_ID, 
	        		IS_SUCCSEND, SPEED, PIPEDEVIATION, PHOTO_NAME }, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPatrolPersonPath(results);
			return bean;
		}
	}
	/**
	 * 查询已巡检标记的巡检作业点
	 * @return
	 */
	public PatrolPersonPath[] queryPatrolPersonPathTaskPointByCurrentTaskPoint(String personId,String taskId,String today) {
		PatrolPersonPath[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_PERSON_PATH;
		try {
	        results = db.query(table, new String[] { KEY_ID, PERSON_ID, LONGITUDE, LATITUDE, HEIGHT, 
	        		PATH_POINT_TIME, PRINCIPAL_TYPE, PRINCIPAL_ID, BUG_ID, 
	        		IS_SUCCSEND, SPEED, PIPEDEVIATION, PHOTO_NAME }, " PRINCIPAL_TYPE='1' and PERSON_ID=? and PRINCIPAL_ID=? and PATH_POINT_TIME like ? ", new String[]{ personId,taskId,"%"+today+"%"}, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPatrolPersonPath(results);
			return bean;
		}
	}
	/**
	 * 查询巡检表中巡检作业点
	 * @return
	 */
	public PatrolPersonPath[] queryPatrolPersonPathTaskPoint(String personId,String taskId,String today) {
		PatrolPersonPath[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_PERSON_PATH;
		try {
			/*results = db.query(table, new String[] { KEY_ID, PERSON_ID, LONGITUDE, LATITUDE, HEIGHT, 
    		PATH_POINT_TIME, PRINCIPAL_TYPE, PRINCIPAL_ID, BUG_ID, 
    		IS_SUCCSEND, SPEED, PIPEDEVIATION, PHOTO_NAME }, " PRINCIPAL_TYPE='1' and PERSON_ID=? and PRINCIPAL_ID in ? and PATH_POINT_TIME like ? ", new String[]{ personId,"("+taskId+")","%"+today+"%"}, null, null, null);*/
			
			String sql  = "select t.PERSON_ID,t.LONGITUDE,t.LATITUDE,t.HEIGHT,t.PATH_POINT_TIME,t.PRINCIPAL_TYPE,t.PRINCIPAL_ID,t.BUG_ID,t.IS_SUCCSEND,t.SPEED,t.PIPEDEVIATION,t.PHOTO_NAME from " 
					+ table + " t where t.PRINCIPAL_TYPE='1' and t.PRINCIPAL_ID in ("+taskId+") and t.PERSON_ID=? and t.PATH_POINT_TIME like ? "; 
					String [] selectionArgs  = new String[]{personId,"%"+today+"%"}; 
			        results = db.rawQuery(sql, selectionArgs); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPatrolPersonPath(results);
			return bean;
		}
	}
	//查询当前未上传数据
	public PatrolPersonPath[] queryPatrolPersonPathByPersonId(String personId) {
		PatrolPersonPath[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_PERSON_PATH;
		try {
	        results = db.query(table, new String[] { KEY_ID, PERSON_ID, LONGITUDE, LATITUDE, HEIGHT, 
	        		PATH_POINT_TIME, PRINCIPAL_TYPE, PRINCIPAL_ID, BUG_ID, 
	        		IS_SUCCSEND, SPEED, PIPEDEVIATION, PHOTO_NAME }, " IS_SUCCSEND='0' or IS_SUCCSEND='2' and PERSON_ID=? ", new String[]{personId}, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPatrolPersonPath(results);
			return bean;
		}
	}
	//查询当前已上传数据
	public PatrolPersonPath[] querySuccessPatrolPersonPathByPersonId(String personId) {
		PatrolPersonPath[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_PERSON_PATH;
		try {
	        results = db.query(table, new String[] { KEY_ID, PERSON_ID, LONGITUDE, LATITUDE, HEIGHT, 
	        		PATH_POINT_TIME, PRINCIPAL_TYPE, PRINCIPAL_ID, BUG_ID, 
	        		IS_SUCCSEND, SPEED, PIPEDEVIATION, PHOTO_NAME }, " IS_SUCCSEND='1' and PERSON_ID=? ", new String[]{personId}, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPatrolPersonPath(results);
			return bean;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private PatrolPersonPath[] ConvertToPatrolPersonPath(Cursor cursor) {
		PatrolPersonPath[] bean = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					bean = new PatrolPersonPath[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						bean[i] = new PatrolPersonPath();
						bean[i].PERSON_ID = cursor.getString(cursor.getColumnIndex(PERSON_ID));
						bean[i].LONGITUDE = cursor.getString(cursor.getColumnIndex(LONGITUDE));
						bean[i].LATITUDE = cursor.getString(cursor.getColumnIndex(LATITUDE));
						bean[i].HEIGHT = cursor.getString(cursor.getColumnIndex(HEIGHT));
						bean[i].PATH_POINT_TIME = cursor.getString(cursor.getColumnIndex(PATH_POINT_TIME));
						bean[i].PRINCIPAL_TYPE = cursor.getString(cursor.getColumnIndex(PRINCIPAL_TYPE));
						bean[i].PRINCIPAL_ID = cursor.getString(cursor.getColumnIndex(PRINCIPAL_ID));
						bean[i].BUG_ID = cursor.getString(cursor.getColumnIndex(BUG_ID));
						bean[i].IS_SUCCSEND = cursor.getString(cursor.getColumnIndex(IS_SUCCSEND));
						bean[i].SPEED = cursor.getString(cursor.getColumnIndex(SPEED));
						bean[i].PIPEDEVIATION = cursor.getString(cursor.getColumnIndex(PIPEDEVIATION));
						bean[i].PHOTO_NAME = cursor.getString(cursor.getColumnIndex(PHOTO_NAME));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return bean;
				}
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 锁定本地巡检表正在上传的数据状态
	 * @param personId
	 * @param pathPointTime
	 */
	public void lockPatrolPersonPathByPersonId(String personId,String pathPointTime){
		String table = DB_TABLE_PATROL_PERSON_PATH;
		//sql更新
		String UPDATE_DATA="update "+table+" set IS_SUCCSEND='2' where PERSON_ID='"+personId+"' and PATH_POINT_TIME='"+pathPointTime+"' ";
		db.execSQL(UPDATE_DATA);
	}
	/**
	 * 更新本地巡检表已上传的数据状态
	 * @param personId
	 * @param pathPointTime
	 */
	public void updatePatrolPersonPathByPersonId(String personId,String pathPointTime){
		String table = DB_TABLE_PATROL_PERSON_PATH;
		/*
		ContentValues cv = new ContentValues();
		cv.put(PERSON_ID, personId);
		cv.put(PATH_POINT_TIME, pathPointTime);
		db.update(table,cv,"PERSON_ID=? and PATH_POINT_TIME=? ",new String[]{personId,pathPointTime});*/
		
		//sql更新
		String UPDATE_DATA="update "+table+" set IS_SUCCSEND='1' where PERSON_ID='"+personId+"' and PATH_POINT_TIME='"+pathPointTime+"' ";
		db.execSQL(UPDATE_DATA);
	}
	/**
	 * 重置本地巡检表上传未成功的数据状态
	 * @param personId
	 * @param pathPointTime
	 */
	public void initPatrolPersonPathByPersonId(String personId,String pathPointTime){
		String table = DB_TABLE_PATROL_PERSON_PATH;
		//sql更新
		String UPDATE_DATA="update "+table+" set IS_SUCCSEND='0' where IS_SUCCSEND='2' and PERSON_ID='"+personId+"' ";
		db.execSQL(UPDATE_DATA);
	}
	/**
	 * 删除巡检表已上传的数据
	 * @return
	 */
	public long deletePatrolPersonPathByPersonId(String personId) {
		long flags = 0;
		flags = db.delete(DB_TABLE_PATROL_PERSON_PATH, " IS_SUCCSEND='1' and PERSON_ID=? ", new String[]{ personId });
		return flags;
	}
	
	
	/**
	 * 管线桩点
	 * @param bean
	 * @return
	 */
	public int insertPilePoint(PipemodeCenterLineStake bean) {
		ContentValues newValues = new ContentValues();
		newValues.put(SUBLINE_ID, bean.subline_id);
		newValues.put(BID, bean.bid);
		newValues.put(SERIAL_NO, bean.serial_NO);
		newValues.put(STAKE_NO, bean.stake_NO);
		newValues.put(X, bean.x);
		newValues.put(Y, bean.y);
		return Integer.valueOf((int) db.insert(DB_TABLE_PILE_POINT, null, newValues));
	}
	/**
	 * 查询管线桩点
	 * @return
	 */
	public PipemodeCenterLineStake[] queryPilePoint(String subline_id) {
		PipemodeCenterLineStake[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PILE_POINT;
		try {
	        results = db.query(table, new String[] { KEY_ID, SUBLINE_ID, BID, SERIAL_NO, STAKE_NO, X, Y }, " SUBLINE_ID like ? " , new String[]{ "%"+subline_id+"%" } , null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPilePoint(results);
			return bean;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private PipemodeCenterLineStake[] ConvertToPilePoint(Cursor cursor) {
		PipemodeCenterLineStake[] bean = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					bean = new PipemodeCenterLineStake[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						bean[i] = new PipemodeCenterLineStake();
						bean[i].subline_id = cursor.getInt(cursor.getColumnIndex(SUBLINE_ID));
						bean[i].bid = cursor.getInt(cursor.getColumnIndex(BID));
						bean[i].serial_NO = cursor.getInt(cursor.getColumnIndex(SERIAL_NO));
						bean[i].stake_NO = cursor.getString(cursor.getColumnIndex(STAKE_NO));
						bean[i].x = cursor.getDouble(cursor.getColumnIndex(X));
						bean[i].y = cursor.getDouble(cursor.getColumnIndex(Y));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return bean;
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 删除管线桩点数据
	 * @return
	 */
	public long deletePilePoint(String subline_id) {
		long flags = 0;
		flags = db.delete(DB_TABLE_PILE_POINT, " SUBLINE_ID like ? ", new String[]{ "%"+subline_id+"%" });
		return flags;
	}
	
	
	/**
	 * 任务作业点
	 * @param bean
	 * @return
	 */
	public int insertTaskPoint(PatrolTaskPoint bean) {
		ContentValues newValues = new ContentValues();
		newValues.put(TASK_ID, bean.id);
		newValues.put(SUBLINE_ID, bean.subline_id);
		newValues.put(LONGITUDE, bean.longitude);
		newValues.put(LATITUDE, bean.latitude);
		return Integer.valueOf((int) db.insert(DB_TABLE_TASK_POINT, null, newValues));
	}
	/**
	 * 查询任务作业点
	 * @return
	 */
	public PatrolTaskPoint[] queryTaskPoint(String subline_id) {
		PatrolTaskPoint[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_TASK_POINT;
		try {
	        results = db.query(table, new String[] { KEY_ID, TASK_ID, SUBLINE_ID, LONGITUDE, LATITUDE }, " SUBLINE_ID like ? " , new String[]{ "%"+subline_id+"%" } , null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToTaskPoint(results);
			return bean;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private PatrolTaskPoint[] ConvertToTaskPoint(Cursor cursor) {
		PatrolTaskPoint[] bean = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					bean = new PatrolTaskPoint[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						bean[i] = new PatrolTaskPoint();
						bean[i].id = cursor.getInt(cursor.getColumnIndex(TASK_ID));
						bean[i].subline_id = cursor.getInt(cursor.getColumnIndex(SUBLINE_ID));
						bean[i].longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE));
						bean[i].latitude = cursor.getString(cursor.getColumnIndex(LATITUDE));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return bean;
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 删除任务作业点
	 * @return
	 */
	public long deleteTaskPoint(String subline_id) {
		long flags = 0;
		flags = db.delete(DB_TABLE_TASK_POINT, " SUBLINE_ID like ? ", new String[]{ "%"+subline_id+"%" });
		return flags;
	}
	
	/**
	 * 新增缺陷报告
	 * @param bean
	 * @return
	 */
	public int insertBugPatrol(BugPatrol bean) {
		ContentValues newValues = new ContentValues();
		newValues.put(BUGID, bean.id);
		newValues.put(NAME, bean.name);
		return Integer.valueOf((int) db.insert(DB_TABLE_PATROL_BUG, null, newValues));
	}
	/**
	 * 查询缺陷报告
	 * @return
	 */
	public BugPatrol[] queryBugPatrol() {
		BugPatrol[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_BUG;
		try {
	        results = db.query(table, new String[] { KEY_ID, BUGID, NAME }, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToBugPatrol(results);
			return bean;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private BugPatrol[] ConvertToBugPatrol(Cursor cursor) {
		BugPatrol[] bean = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					bean = new BugPatrol[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						bean[i] = new BugPatrol();
						bean[i].id = cursor.getInt(cursor.getColumnIndex(BUGID));
						bean[i].name = cursor.getString(cursor.getColumnIndex(NAME));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return bean;
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 删除缺陷报告
	 * @return
	 */
	public long deleteBugPatrol() {
		long flags = 0;
		flags = db.delete(DB_TABLE_PATROL_BUG, null, null);
		return flags;
	}
	
	
	/**
	 * 上传照片表
	 * @param bean
	 * @return
	 */
	public int insertPatrolBugPhoto(PatrolBugPhoto bean) {
		ContentValues newValues = new ContentValues();
		newValues.put(BUG_REPORT_ID, bean.BUG_REPORT_ID);
		newValues.put(PHOTO_NAME, bean.PHOTO_NAME);
		newValues.put(CONTENT, bean.CONTENT);
		newValues.put(LONGITUDE, bean.LONGITUDE);
		newValues.put(LATITUDE, bean.LATITUDE);
		newValues.put(TIME, bean.TIME);
		newValues.put(ISUPLOAD, bean.ISUPLOAD);
		newValues.put(STATUS, bean.STATUS);
		newValues.put(PERSON_ID, bean.PERSON_ID);
		newValues.put(HEIGHT, bean.HEIGHT);
		return Integer.valueOf((int) db.insert(DB_TABLE_PATROL_BUGPHOTO, null, newValues));
	}
	/**
	 * 查询照片表状态为0的，0表示未上传的
	 * @return
	 */
	public PatrolBugPhoto[] queryPatrolBugPhotoIsUploadBy0(String personId) {
		PatrolBugPhoto[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_BUGPHOTO;
		try {
	        results = db.query(table, new String[] { KEY_ID,BUG_REPORT_ID,PHOTO_NAME,CONTENT,LONGITUDE,LATITUDE,TIME,ISUPLOAD,STATUS,PERSON_ID,HEIGHT }, " ISUPLOAD='0' and PERSON_ID=? ", new String[]{ personId }, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPatrolBugPhoto(results);
			return bean;
		}
	}
	/**
	 * 查询照片表状态为0和2的，0表示未上传的和2表示上传意外中断未成功的
	 * @param personId
	 * @return
	 */
	public PatrolBugPhoto[] queryPatrolBugPhotoIsUploadBy0_2(String personId) {
		PatrolBugPhoto[] bean = null;
		Cursor results = null;
		String table = DB_TABLE_PATROL_BUGPHOTO;
		try {
	        results = db.query(table, new String[] { KEY_ID,BUG_REPORT_ID,PHOTO_NAME,CONTENT,LONGITUDE,LATITUDE,TIME,ISUPLOAD,STATUS,PERSON_ID,HEIGHT }, " ISUPLOAD='0' or ISUPLOAD='2' and PERSON_ID=? ", new String[]{ personId }, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bean = ConvertToPatrolBugPhoto(results);
			return bean;
		}
	}
	/**
	 * 转换数据
	 * @param cursor
	 * @return
	 */
	private PatrolBugPhoto[] ConvertToPatrolBugPhoto(Cursor cursor) {
		PatrolBugPhoto[] bean = null;
		if(cursor!=null){
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}else{
				try {
					bean = new PatrolBugPhoto[resultCounts];
					for (int i = 0; i < resultCounts; i++) {
						bean[i] = new PatrolBugPhoto();
						bean[i].BUG_REPORT_ID = cursor.getInt(cursor.getColumnIndex(BUG_REPORT_ID));
						bean[i].PHOTO_NAME = cursor.getString(cursor.getColumnIndex(PHOTO_NAME));
						bean[i].CONTENT = cursor.getString(cursor.getColumnIndex(CONTENT));
						bean[i].LONGITUDE = cursor.getString(cursor.getColumnIndex(LONGITUDE));
						bean[i].LATITUDE = cursor.getString(cursor.getColumnIndex(LATITUDE));
						bean[i].TIME = cursor.getString(cursor.getColumnIndex(TIME));
						bean[i].ISUPLOAD = cursor.getString(cursor.getColumnIndex(ISUPLOAD));
						bean[i].STATUS = cursor.getString(cursor.getColumnIndex(STATUS));
						bean[i].PERSON_ID = cursor.getString(cursor.getColumnIndex(PERSON_ID));
						bean[i].HEIGHT = cursor.getString(cursor.getColumnIndex(HEIGHT));
						cursor.moveToNext();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					cursor.close();
					return bean;
				}
			}
		}else{
			return null;
		}
	}
	/**
	 * 删除上传照片表已上传的数据
	 * @return
	 */
	public long deletePatrolBugPhoto(String personId) {
		long flags = 0;
		flags = db.delete(DB_TABLE_PATROL_BUGPHOTO, " ISUPLOAD='1' and PERSON_ID=? ", new String[]{ personId });
		return flags;
	}
	/**
	 * 重置中断未上传成功的缺陷照片巡检数据
	 * @param personId
	 */
	public void initPatrolBugPhotoByPersonId(String personId){
		String table = DB_TABLE_PATROL_BUGPHOTO;
		//sql更新
		String UPDATE_DATA="update "+table+" set ISUPLOAD='0' where ISUPLOAD='2' and PERSON_ID='"+personId+"' ";
		db.execSQL(UPDATE_DATA);
	}
	/**
	 * 锁定上传中的缺陷照片数据
	 * @param personId
	 * @param time
	 */
	public void lockPatrolBugPhotoByPersonId(String personId,String time){
		String table = DB_TABLE_PATROL_BUGPHOTO;
		//sql更新
		String UPDATE_DATA="update "+table+" set ISUPLOAD='2' where PERSON_ID='"+personId+"' and TIME='"+time+"' ";
		db.execSQL(UPDATE_DATA);
	}
	/**
	 * 更新本地巡检表已上传的数据状态
	 * @param personId
	 * @param pathPointTime
	 */
	public void updatePatrolBugPhotoByPersonId(String personId,String time){
		String table = DB_TABLE_PATROL_BUGPHOTO;
		//sql更新
		String UPDATE_DATA="update "+table+" set ISUPLOAD='1' where PERSON_ID='"+personId+"' and TIME='"+time+"' ";
		db.execSQL(UPDATE_DATA);
	}
	
	
	/** 静态Helper类，用于建立、更新和打开数据库 */
	private static class DBOpenHelper extends SQLiteOpenHelper {

		private int version;

		public DBOpenHelper(Context context) {  
            super(context, DB_NAME,  null , DB_VERSION);  
        }
		
		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			this.version = version;
		}
		
		// 用户
		private static final String DB_CREATE_TABLE_USER = "create table "
				+ DB_TABLE_USER + " (" + KEY_ID
				+ " integer primary key autoincrement, " + NAME + "," + PASSWORD + ");";
		
		// 获取巡检参数
		private static final String DB_CREATE_TABLE_PATROL_PARAM = "create table "
				+ DB_TABLE_PATROL_PARAM + " (" + KEY_ID
				+ " integer primary key autoincrement, " + PARAMNAME + "," + PARAMVALUE + "," + CHNAME + "," + REMARK + ");";
		
		// 管线subline_id表
		private static final String DB_CREATE_TABLE_PATROL_SUBLINE = "create table "
				+ DB_TABLE_PATROL_SUBLINE + " (" + KEY_ID
				+ " integer primary key autoincrement, " + NAME + "," + LINE_ID + "," + BEGIN + "," + END + ");";
		
		// 巡检数据表
		private static final String DB_CREATE_TABLE_PATROL_PERSON_PATH = "create table "
				+ DB_TABLE_PATROL_PERSON_PATH + " (" + KEY_ID
				+ " integer primary key autoincrement, " + PERSON_ID + "," + LONGITUDE + "," + LATITUDE + "," + HEIGHT
				+ "," + PATH_POINT_TIME + "," + PRINCIPAL_TYPE +  "," + PRINCIPAL_ID +  "," + BUG_ID 
				+  "," + IS_SUCCSEND + "," + SPEED + "," + PIPEDEVIATION + "," + PHOTO_NAME +");";
		
		// 巡检桩点表
		private static final String DB_CREATE_TABLE_PILE_POINT = "create table "
				+ DB_TABLE_PILE_POINT + " (" + KEY_ID
				+ " integer primary key autoincrement, " + SUBLINE_ID + "," + BID + "," + SERIAL_NO + "," + STAKE_NO
				+ "," + X + "," + Y +");";

		// 任务作业点表
		private static final String DB_CREATE_TABLE_TASK_POINT = "create table "
				+ DB_TABLE_TASK_POINT + " (" + KEY_ID
				+ " integer primary key autoincrement, " + TASK_ID + "," + SUBLINE_ID + "," + LONGITUDE + "," + LATITUDE +");";
		
		// 缺陷报告表
		private static final String DB_CREATE_TABLE_PATROL_BUG = "create table "
				+ DB_TABLE_PATROL_BUG + " (" + KEY_ID
				+ " integer primary key autoincrement, " + BUGID + "," + NAME +");";
		
		// 上传照片表
		private static final String DB_CREATE_TABLE_PATROL_BUGPHOTO = "create table "
				+ DB_TABLE_PATROL_BUGPHOTO + " (" + KEY_ID
				+ " integer primary key autoincrement, " + BUG_REPORT_ID + "," + PHOTO_NAME + "," 
				+ CONTENT + "," + LONGITUDE +"," 
				+ LATITUDE + "," + TIME +"," 
				+ ISUPLOAD + "," + STATUS +"," 
				+ PERSON_ID + "," + HEIGHT +");";
		
		
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DB_CREATE_TABLE_USER);//用户
			_db.execSQL(DB_CREATE_TABLE_PATROL_PARAM);//巡检参数
			_db.execSQL(DB_CREATE_TABLE_PATROL_SUBLINE);//管线subline_id表
			_db.execSQL(DB_CREATE_TABLE_PATROL_PERSON_PATH);//巡检数据表
			_db.execSQL(DB_CREATE_TABLE_PILE_POINT);//巡检桩点表
			_db.execSQL(DB_CREATE_TABLE_TASK_POINT);//任务作业点表
			_db.execSQL(DB_CREATE_TABLE_PATROL_BUG);//缺陷报告表
			_db.execSQL(DB_CREATE_TABLE_PATROL_BUGPHOTO);//上传照片表
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_USER);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_PATROL_PARAM);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_PATROL_SUBLINE);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_PATROL_PERSON_PATH);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_PILE_POINT);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_TASK_POINT);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_PATROL_BUG);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_TABLE_PATROL_BUGPHOTO);
			onCreate(_db);
		}
		
	}
	
	
}
