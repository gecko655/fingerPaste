package com.tkobayalab.fingerpaste;

import android.graphics.Bitmap;

public class Item {
	private int id;
	private String text;
	private Bitmap img;
	private long date;
	public Item( int id, String text, Bitmap img, long date ) {
		this.id = id;
		this.text = text;
		this.img = img;
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	public Bitmap getBitmap() {
		return img;
	}
	public long getDate() {
		return date;
	}
}