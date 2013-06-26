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
		ClipData cd = cm.getPrimaryClip();

		if(cd != null){
			ClipData.Item item = cd.getItemAt(0);
			return item.getText().toString();
		}
		return null;
	}
	
	public void setText(String text){
		ClipData.Item item = new ClipData.Item("クリップボードにコピーしたいテキストデータ");
		String[] mimeType = new String[1];
		mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
		 
		ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);
		cm.setPrimaryClip(cd);
	}
}
