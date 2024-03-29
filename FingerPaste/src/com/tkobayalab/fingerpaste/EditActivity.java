package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	EditText editText;
	RadioGroup radioGroup;
	ImageButton imageButton;
	Button button1,button2;
	EditManager editManager;
	
	Gesture gesture;
	private boolean firstFocused=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		TextView textView = (TextView) findViewById(R.id.textView2);
		textView.setText(R.string.editHeader);
		editText= (EditText)findViewById(R.id.editText1);
		radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		imageButton= (ImageButton) findViewById(R.id.imageButton1);
		imageButton.setOnClickListener(this);
		button1 = (Button)findViewById(R.id.button1);
		button2 = (Button)findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	
		editManager = new EditManager(this);
		Intent intent = getIntent();
		if(intent != null){
			int id = intent.getIntExtra("ID", -1);
			if(id != -1){
				editManager.setId(id);
			}
		}
		firstFocused = true;
		

	}
	
	@Override
	 public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(firstFocused){
			editManager.fillForm(editManager.getId());
			firstFocused=false;
		}
	 }

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radioButton1:
			editManager.removeGesture();
			break;
		case R.id.radioButton2:
			editManager.startGestureInputActivity();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==button1.getId()){
			editManager.editItem(editText.getText().toString(), gesture);
		}else if(v.getId()==button2.getId()){
			editManager.closeEditActivity();
		}else if(v.getId()==imageButton.getId()){
			RadioButton radioButton2 = ((RadioButton) findViewById(R.id.radioButton2));
			if(radioButton2.isChecked()){
				editManager.startGestureInputActivity();
			}else{
				radioButton2.setChecked(true);
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==AbstractGestureInputActivity.SUCCESS
				&&requestCode==EditManager.REQUESTCODE){
			gesture = ((Gesture)data.getExtras().getParcelable("Gesture"));
			editManager.receiveGesture(gesture);
		}else{
			RadioButton radioButton1 = ((RadioButton) findViewById(R.id.radioButton1));
			radioButton1.setChecked(true);
		}
	}



}
