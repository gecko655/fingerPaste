package com.tkobayalab.fingerpaste;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Date;
import java.util.ArrayList;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.gesture.Gesture;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.util.Log;


public class DatabaseManager {
	
	private GestureStore gestures;
	private DatabaseOpenHelper dbHelper;
	
	final private String pathToDB = "/data/data/com.tkobayalab.fingerpaste/databases/gesturedb";
	final int alphaID = -999;
	final int betaID = -888;
	final int gammaID = -777;
	
	
	public DatabaseManager(Context context){
		dbHelper = new DatabaseOpenHelper(context);
		gestures = new GestureStore();
		
		loadGestures();
	}

	public void delete(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		if(id == alphaID || id == betaID || id == gammaID) return;
		db.delete("textdb", "_id =" + id, null);
		gestures.removeEntry("" + id);
		saveGestures();
	}
	
	public void deleteAllItem(){
		int[] typeOfItems = getElementsId();
		
		for(int i = 0; i < 100; i++){
			if(typeOfItems[i] == 1){
				delete(i);
			} else if(typeOfItems[i] == 2){
				delete(i);
				gestures.removeEntry("" + i);
			}
		}
		saveGestures();
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
        saveGestures();
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
	}
	
	public String getText(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "select text from textdb where _id =" + id + ";";
		Cursor c = db.rawQuery(sql, null);
		
        int indexText  = c.getColumnIndex("text");
        String text = "";
        while(c.moveToNext()){
        	text  = c.getString(indexText);
        }
		return text;
	}
	
	// not put in class diagram
	public long getUpdateTime(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "select updatetime from textdb where _id =" + id + ";";
		Cursor c = db.rawQuery(sql, null);
		
        int indexText  = c.getColumnIndex("text");
        String text  = c.getString(indexText);
		
		return Long.parseLong(text);
	}
	
	public int getIdOfMaxScore(Gesture gesture){
		ArrayList<Prediction> predictions = gestures.recognize(gesture);
		
		if(predictions.size() > 0){
			return Integer.parseInt(predictions.get(0).name);
		}
		return -1;
	}
	
	public boolean hasSimilarItem(Gesture gesture){
		return false;
	}
	
	public void changeAlpha(Gesture gesture){
		gestures.removeEntry("" + alphaID);
		gestures.addGesture("" + alphaID, gesture);
		saveGestures();
	}
	
	public void changeBeta(Gesture gesture){
		gestures.removeEntry("" + betaID);
		gestures.addGesture("" + betaID, gesture);
		saveGestures();
	}
	
	public void changeGamma(Gesture gesture){
		gestures.removeEntry("" + gammaID);
		gestures.addGesture("" + gammaID, gesture);
		saveGestures();
	}
	
	public boolean isAlpha(int id){
		if(id == alphaID) return true;
		return false;
	}
	
	public boolean isBeta(int id){
		if(id == betaID) return true;
		return false;
	}
	
	public boolean isGamma(int id){
		if(id == gammaID) return true;
		return false;
	}
	

	public Bitmap getGestureImage(int id){
		ArrayList<Gesture> g = gestures.getGestures("" + id);
		
		if(g.size() == 0) return null;
		return g.get(0).toBitmap(100, 100, 8, 0xFFFFFF00);
	}
	
	public int[] getElementsId(){
		int[] typeOfItems = new int[100];
		SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor cursor = null;
    	try{
    		cursor = db.query("textdb", 
    				new String[]{"_id", "text", "updatetime"}, 
    				"_id >= 0", null, 
    				null, null, null );

    		int indexId = cursor.getColumnIndex("_id");

            while(cursor.moveToNext()){
                int id = cursor.getInt(indexId);
                typeOfItems[id]++;  // when text of this id exists 
            }
    	}
    	finally{
    		if( cursor != null ){
    			cursor.close();
    		}
    	}
		
    	String[] gestureid = (String[])gestures.getGestureEntries().toArray();
    	for(int i = 0; i < gestureid.length; i++){
    		if(Integer.parseInt(gestureid[i]) == alphaID) continue;
    		if(Integer.parseInt(gestureid[i]) == betaID) continue;
    		if(Integer.parseInt(gestureid[i]) == gammaID) continue;
    		typeOfItems[Integer.parseInt(gestureid[i])]++; // when gesture of this id exists
    	}
		
		return typeOfItems;
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
    				new String[]{"_id", "text", "updatetime"}, 
    				"_id >= 0", null, 
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
