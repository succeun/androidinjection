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

		// AnnotationActivity ���� ������ alert ���
		alert("�̸�: " + name + ", ����: " + age);
	}
}
