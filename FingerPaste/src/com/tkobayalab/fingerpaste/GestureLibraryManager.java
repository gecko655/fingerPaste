package com.tkobayalab.fingerpaste;

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
	final static private String pathToInitialGestureDB = "/assets/init_gesturedb";
	
	static {
		gestures = new GestureStore();
		try{
    		gestures.load(new FileInputStream(pathToGestureDB));
    	} catch (IOException e){
    		Log.d("myTest", "initialerror");
    	}
		
		Log.d("myTest", "create gestures.");
	}
	
	static public boolean saveGestures(){
    	try{
    		gestures.save(new FileOutputStream(pathToGestureDB));
    	} catch (IOException e){
    		Log.d("myTest", "save error");
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
		try{
			AssetManager as = res.getAssets();   
			InputStream is = as.open("init_gesturedb");  
			gestures.load(is);
			saveGestures();
		} catch(IOException e){
			Log.d("myTest", "failed initialization.");
		}
	}
}
