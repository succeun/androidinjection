package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.Extra;
import android.os.Bundle;

public class ActivityIntentSubThree extends AnnotationActivity {
	@Extra("xml")
	private String xml;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alert("XML: " + xml);
	}
}
