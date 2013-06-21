package com.tkobayalab.fingerpaste;

import android.graphics.Bitmap;

public class Item {
	private int id;
	private String text;
	private Bitmap img;
	public Item( int id, String text, Bitmap img ) {
		this.id = id;
		this.text = text;
		this.img = img;
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
}