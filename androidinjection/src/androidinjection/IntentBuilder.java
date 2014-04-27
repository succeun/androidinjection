package androidinjection;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

public class IntentBuilder {
	private Activity activity = null;
	private Intent intent = null;
	public IntentBuilder(Activity activity, Class<?> targetClass) {
		this.activity = activity; 
		intent = new Intent(activity, targetClass);
	}
	
	public IntentBuilder(Activity activity, Intent o) {
		this.activity = activity; 
		intent = new Intent(o);
	}

	public IntentBuilder(Activity activity, String action) {
		this.activity = activity; 
		intent = new Intent(action);
	}

	public IntentBuilder(Activity activity, String action, Uri uri) {
		this.activity = activity; 
		intent = new Intent(action, uri);
	}

	public IntentBuilder(Activity activity, String action, Uri uri, Class<?> cls) {
		this.activity = activity; 
		intent = new Intent(action, uri, activity, cls);
	}
	
	public Intent getIntent() {
		return intent;
	}

	public void startActivityForResult(int requestCode) {
		activity.startActivityForResult(intent, requestCode);
	}
	
	public void startActivity() {
		activity.startActivity(intent);
	}
	
	public IntentBuilder setResult(int resultCode) {
		activity.setResult(resultCode, intent);
		return this;
	}
	
	public IntentBuilder setResultNotWithIntent(int resultCode) {
		activity.setResult(resultCode);
		return this;
	}
	
	public void finish() {
		activity.finish();
	}
	
	public IntentBuilder put(String name, double[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, int value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, CharSequence value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, char value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, Bundle value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, Parcelable[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, Serializable value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, int[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, float value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, byte[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, long[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, Parcelable value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, float[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, long value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, String[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, boolean value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, boolean[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, short value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, double value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, short[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, String value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, byte value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, char[] value) {
		intent.putExtra(name, value);
		return this;
	}

	public IntentBuilder put(String name, CharSequence[] value) {
		intent.putExtra(name, value);
		return this;
	}
	
	public IntentBuilder setAction(String action) {
		intent.setAction(action);
		return this;
	}
	
	public IntentBuilder setClass(Class<?> cls) {
		intent.setClass(activity, cls);
		return this;
	}
	
	public IntentBuilder setData(Uri data) {
		intent.setData(data);
		return this;
	}
	
	public IntentBuilder setDataAndType(Uri data, String type) {
		intent.setDataAndType(data, type);
		return this;
	}
	
	public IntentBuilder setType(String type) {
		intent.setType(type);
		return this;
	}
}
