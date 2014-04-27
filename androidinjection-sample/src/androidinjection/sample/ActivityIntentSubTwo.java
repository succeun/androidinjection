package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.Extra;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.ViewById;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidinjection.sample.R;

@EActivity(R.layout.activity_six_sub_two)
public class ActivityIntentSubTwo extends AnnotationActivity {
	@Extra("name")
	private String name;
	
	@Extra("age")
	private int age;
	
	@ViewById(R.id.tvName)
	private TextView tvName;
	
	@ViewById(R.id.tvAge)
	private TextView tvAge;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvName.setText("이름: " + name);
		tvAge.setText("나이: " + age);
	}
	
	@OnClick(R.id.btnReturn)
	public void onClickBtnReturn() {
		Intent intent = new Intent();
		intent.putExtra("company", "현대HDS");
		setResult(RESULT_OK, intent);
		finish();
	}
}
