package com.tkobayalab.fingerpaste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.util.Log;

import android.content.res.AssetManager;

public class GestureLibraryManager {
	static private GestureStore gestures;
	final static private String pathToGestureDB = "/data/data/com.tkobayalab.fingerpaste/databases/gesturedb";
	
	
	static {
		gestures = new GestureStore();
		FileInputStream fis;
		try{
			//FileInputStream fis = new FileInputStream(pathToGestureDB);
			fis = new FileInputStream(pathToGestureDB);
			gestures.load(fis);
			fis.close();
    		Log.d("myTest", "successed load gesturedb.");
    	} catch (IOException e){
    		Log.d("myTest", "failed load gesturedb.");
    	}
	}
	
	static public boolean saveGestures(){
    	try{
    		FileOutputStream fos = new FileOutputStream(pathToGestureDB ,true);
    		gestures.save(fos);
    		fos.close();
    		Log.d("myTest", "successed save gesturedb.");
    	} catch (IOException e){
    		Log.d("myTest",e.toString());
    		Log.d("myTest", "failed save gesturedb.");
    		return false;
    	}
    	
    	return true;
    }

	static public void deleteGesture(String entry){
		gestures.removeEntry(entry);
		saveGestures();
	}
	
	static public void addGesture(String entry, Gesture gesture){
		gestures.addGesture(entry, gesture);
		saveGestures();
	}
	
	static public ArrayList<Prediction> getPredictions(Gesture gesture){
		return gestures.recognize(gesture);
	}
	
	static public ArrayList<Gesture> getGestures(String entry){
		return gestures.getGestures(entry);
	}
	
	static public String[] getGestureEntrys(){
		return gestures.getGestureEntries().toArray(new String[0]);
	}
	
	static public void changeGesture(String entry, Gesture gesture){
		gestures.removeEntry(entry);
		gestures.addGesture(entry, gesture);
		saveGestures();
	}
	
	static public void initializeGestures(Resources res){
		File fl = new File("/data/data/com.tkobayalab.fingerpaste/databases/", "gesturedb");
		try{
			AssetManager as = res.getAssets();   
			InputStream is = as.open("init_gesturedb");  
			gestures.load(is);
		} catch(IOException e){
			Log.d("myTest", "failed initialization.");
		}
		try{
			fl.createNewFile();
		} catch(IOException e){
			Log.d("myTest",e.toString());
			Log.d("myTest", "failed create gesturedb.");
		}
		saveGestures();
	}
	
	static public boolean isGestureOfThisId(int id){
		ArrayList<Gesture> g = gestures.getGestures("" + id);
		if(g == null) return false;
		if(g.size() == 0) return false;
		return true;
	}
}
