package androidinjection.annotation;


import java.lang.reflect.Field;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import androidinjection.MobileException;


public class FieldInvocation {
    protected Object receiver = null;
    protected Field field = null;
    protected Object uiObject = null;	// UI를 가지는 객체로써, View 또는 Activity
    
	public FieldInvocation(Object receiver, Field field, Object uiObject) {
		this.receiver = receiver;
		this.field = field;
		this.uiObject = uiObject;
	}
	
	public void setValue(Object param) {
		try {
		    field.setAccessible(true);
		    field.set(receiver, param);
		} catch (IllegalArgumentException e) {
		    throw new MobileException(e);
		} catch (IllegalAccessException e) {
			throw new MobileException(e);
		}
	}
	
	public Object getValue() {
		try {
			field.setAccessible(true);
			return field.get(receiver);
		} catch (IllegalArgumentException e) {
			throw new MobileException(e);
		} catch (IllegalAccessException e) {
			throw new MobileException(e);
		}
	}
	
	public View findViewById(int id) {
    	View view = null;
    	if (uiObject instanceof View)
    		view = ((View)uiObject).findViewById(id);
    	else if (uiObject instanceof Activity)
    		view = ((Activity)uiObject).findViewById(id);
    	return view;
    }
	
	public Resources getResources() {
		Resources resources = null;
		if (uiObject instanceof View)
    		resources = ((View)uiObject).getResources();
    	else if (uiObject instanceof Activity)
    		resources = ((Activity)uiObject).getResources();
    	return resources;
	}
	
	public Field getField() {
		return field;
	}
	
	public Object getReceiver() {
		return receiver;
	}
	
	public Object getUiObject() {
		return uiObject;
	}
	
	@Override
	public String toString() {
		return "[field: " + field.getName()
				+ ", Receiver: " + receiver
				+ ", UiObject: " + uiObject + "]";
	}
}
