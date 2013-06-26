package com.tkobayalab.fingerpaste;


import java.util.BitSet;
import java.util.Date;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.gesture.Gesture;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.util.Log;


public class DatabaseManager {
	
	private DatabaseOpenHelper dbHelper;
	
	final int alphaID = -999;
	final int betaID = -888;
	final int gammaID = -777;
	
	public static final int MAX_ITEM = 100;

	public static final int TYPE_EMPTY = 0;
	public static final int TYPE_NO_GESTURE = 1;
	public static final int TYPE_HAS_GESTURE = 2;
	
	public DatabaseManager(Context context){
		dbHelper = new DatabaseOpenHelper(context);
	}

	public void delete(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		if(id == alphaID || id == betaID || id == gammaID) return;
		db.delete("textdb", "_id =" + id, null);
		GestureLibraryManager.deleteGesture("" + id);
	}
	
	public void deleteAllItem(){
		int[] typeOfItems = getElementsId();
		
		for(int i = 0; i < 100; i++){
			if(typeOfItems[i] == 1){
				delete(i);
			} else if(typeOfItems[i] == 2){
				delete(i);
				GestureLibraryManager.deleteGesture("" + i);
			}
		}
	}
	
	public void add(String text, Gesture gesture){
		int id = getNextId();
		if(id == -1) return;
		
		Date date = new Date();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put("_id", id);
		val.put("text" , text);
		val.put("updatetime", date.getTime());
        	
		db.insert("textdb", null, val);
		GestureLibraryManager.addGesture("" + id, gesture);
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
		
		ArrayList<Prediction> predictions = GestureLibraryManager.getPredictions(gesture);
		
		if(predictions.size() > 0){
			return Integer.parseInt(predictions.get(0).name);
		}
		return -1;
	}
	
	public boolean hasSimilarItem(Gesture gesture){
		// todo
		return false;
	}
	
	public void changeAlpha(Gesture gesture){
		GestureLibraryManager.changeGesture("" + alphaID, gesture);
		
	}
	
	public void changeBeta(Gesture gesture){
		GestureLibraryManager.changeGesture("" + betaID, gesture);
	}
	
	public void changeGamma(Gesture gesture){
		GestureLibraryManager.changeGesture("" + gammaID, gesture);
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
		ArrayList<Gesture> g = GestureLibraryManager.getGestures("" + id);
		if(g == null) return null;
		if(g.size() == 0) return null;
		return g.get(0).toBitmap(100, 100, 8, 0xFFFFFF00);
	}
	
	public Bitmap getGestureImage(int id, int width, int height, int inset, int color){
		ArrayList<Gesture> g = GestureLibraryManager.getGestures("" + id);
		if(g == null) return null;
		if(g.size() == 0) return null;
		return g.get(0).toBitmap(width, height, inset, color);
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
		
    	String[] gestureid = GestureLibraryManager.getGestureEntrys();
    	Log.d("myTest", "" + gestureid.length);
    	for(int i = 0; i < gestureid.length; i++){
    		if(Integer.parseInt(gestureid[i]) == alphaID) continue;
    		if(Integer.parseInt(gestureid[i]) == betaID) continue;
    		if(Integer.parseInt(gestureid[i]) == gammaID) continue;
    		typeOfItems[Integer.parseInt(gestureid[i])]++; // when gesture of this id exists
    	}
    	
    	
		return typeOfItems;
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
