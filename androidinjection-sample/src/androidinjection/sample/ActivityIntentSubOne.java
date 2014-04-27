package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.Extra;
import android.os.Bundle;

public class ActivityIntentSubOne extends AnnotationActivity {
	@Extra("name")
	private String name;
	
	@Extra("age")
	private int age;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// AnnotationActivity 내에 구현된 alert 사용
		alert("이름: " + name + ", 나이: " + age);
	}
}
