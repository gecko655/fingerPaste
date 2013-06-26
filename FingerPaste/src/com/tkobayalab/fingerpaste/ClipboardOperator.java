package com.tkobayalab.fingerpaste;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipDescription;
import android.content.ClipData;

public class ClipboardOperator {
	private ClipboardManager cm;
	
	public ClipboardOperator(Context context){
		cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
	}

	public String getText(){
		ClipDescription cd = cm.getPrimaryClipDescription();
		
		if(cd.hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST)) return null;
		if(cd.hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)) return null;
		if(cd.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) return null;  // this also can through as MIMETYPE_TEXT_PLAIN
		
		return cm.getPrimaryClip().toString();
	}
	
	public void setText(String text){
		ClipData.Item item = new ClipData.Item("クリップボードにコピーしたいテキストデータ");
		String[] mimeType = new String[1];
		mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
		 
		ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);
		cm.setPrimaryClip(cd);
	}
}
