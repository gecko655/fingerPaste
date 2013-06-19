package com.tkobayalab.fingerpaste;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.gesture.Gesture;
import android.gesture.GestureStore;
import android.graphics.Bitmap;
import android.util.Log;

public class DatabaseManager {
	
	private GestureStore gestures;
	private DatabaseOpenHelper dbHelper;
	
	final private String pathToDB = "/data/data/com.tkobayalab.fingerpaste/databases/gesturedb";
	
	public DatabaseManager(Context context){
		dbHelper = new DatabaseOpenHelper(context);
		gestures = new GestureStore();
		
		loadGestures();
	}

	public void delete(int id){
	}
	
	public void deleteAllItem(){
	}
	
	public void add(String text, Gesture gesture){
		int id = getNextId();
		if(id == -1) return;
		try{
			Date date = new Date();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues val = new ContentValues();
			val.put("_id", id);
			val.put("text" , text);
			val.put("updatetime", date.getTime());
        
			db.insert("textdb", null, val);
			gestures.addGesture("" + id, gesture);
			gestures.save(new FileOutputStream(pathToDB));
		} catch (IOException e){
			Log.d("myTest", "save error");
		}
        return;
	}
	
	public void add(String text){
		int id = getNextId();
		if(id == -1) return;
		Date date = new Date();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("_id", id);
        val.put("text" , text);
        val.put("updatetime", date.getTime());
        
        db.insert("textdb", null, val);
        return;
	}
	
	public String getText(int id){
		return null;
	}
	
	public int getIdOfMaxScore(Gesture gesture){
		return 0;
	}
	
	public boolean hasSimilarItem(Gesture gesture){
		return false;
	}
	
	public void changeAlpha(Gesture gesture){
	}
	
	public void changeBeta(Gesture gesture){
	}
	
	public void changeGamma(Gesture gesture){
	}
	
	public boolean isAlpha(int id){
		return false;
	}
	
	public boolean isBeta(int id){
		return false;
	}
	
	public boolean isGamma(int id){
		return false;
	}
	
	public Bitmap getGestureImage(int id){
		return null;
	}
	
	public int[] getElementsId(){
		return null;
	}
	
	
	// not put in class diagram
	private boolean saveGestures(){
    	try{
    		gestures.save(new FileOutputStream(pathToDB));
    	} catch (IOException e){
    		Log.d("myTest", "save error");
    		return false;
    	}
    	
    	return true;
    }
    
	
	// not put in class diagram
    private boolean loadGestures(){
    	try{
    		gestures.load(new FileInputStream(pathToDB));
    	} catch (IOException e){
    		Log.d("myTest", "load error");
    		return false;
    	}
    	
    	return true;
    }
	
    // not put in class diagram
    private int getNextId(){
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor cursor = null;
    	try{
    		cursor = db.query("textdb", 
    				new String[]{ "_id", "text", "updatetime"}, 
    				null, null, 
    				null, null, null );

    		return getFreeId(cursor);
    	}
    	finally{
    		if( cursor != null ){
    			cursor.close();
    		}
    	}
    }
    
    // not put in class diagram
    private int getFreeId(Cursor cursor){
        int indexId = cursor.getColumnIndex("_id");
        BitSet bs = new BitSet(100);

        bs.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(indexId);
            bs.set(id);
        }
        
        for(int i = 0; i < 100; i++){
        	if(!bs.get(i)) return i;
        }
        
        return -1;
    }
    
	public class DatabaseOpenHelper extends SQLiteOpenHelper {

	    private static final String DATABASE_NAME = "textdb1"; 
	    private static final int DATABASE_VERSION = 1; 																	

	    									
	    private static final String CREATE_TABLE_SQL = 
	         " create table textdb (_id int primary key,text text not null, updatetime int not null)";
	    private static final String DROP_TABLE_SQL = "drop table if exists prefecture";

	    public DatabaseOpenHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(CREATE_TABLE_SQL); 
	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    }
	}
}
