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
import android.widget.Toast;

public class AddActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	EditText editText;
	RadioGroup radioGroup;
	ImageButton imageButton;
	Button button1,button2;
	AddManager addManager;
	
	Gesture gesture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		editText= (EditText)findViewById(R.id.editText1);
		radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		imageButton= (ImageButton) findViewById(R.id.imageButton1);
		imageButton.setOnClickListener(this);
		button1 = (Button)findViewById(R.id.button1);
		button2 = (Button)findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	
		addManager = new AddManager(this);
		Intent intent = getIntent();
		if(intent!=null){
			String text = (String) intent.getCharSequenceExtra("text");
			if(text != null){
				addManager.fillForm(text);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radioButton1:
			addManager.removeGesture();
			break;
		case R.id.radioButton2:
			addManager.startGestureInputActivity();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==button1.getId()){
			addManager.addItem(editText.getText().toString(), gesture);
		}else if(v.getId()==button2.getId()){
			addManager.closeAddActivity();
		}else if(v.getId()==imageButton.getId()){
			RadioButton radioButton2 = ((RadioButton) findViewById(R.id.radioButton2));
			if(radioButton2.isChecked()){
				addManager.startGestureInputActivity();
			}else{
				radioButton2.setChecked(true);
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(this, resultCode+"", Toast.LENGTH_LONG).show();
		if(resultCode==AbstractGestureInputActivity.SUCCESS
				&&requestCode==AddManager.REQUESTCODE){
			gesture = ((Gesture)data.getExtras().getParcelable("Gesture"));
			addManager.receiveGesture(gesture);
		}else{
			RadioButton radioButton1 = ((RadioButton) findViewById(R.id.radioButton1));
			radioButton1.setChecked(true);
		}
	}



}
