package androidinjection.annotation;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import androidinjection.MobileException;


public class MethodInvocation {
    protected Object receiver = null;
    protected Method method = null;
    protected Object uiObject = null;	// UI를 가지는 객체로써, View 또는 Activity 
    
	public MethodInvocation(Object receiver, Method method, Object uiObject) {
		this.receiver = receiver;
		this.method = method;
		this.uiObject = uiObject;
	}
	
	public Object invoke(Object ... args) {
		try {
			method.setAccessible(true);
			return method.invoke(receiver, args);
		} catch (IllegalArgumentException e) {
			throw new MobileException(e);
		} catch (IllegalAccessException e) {
			throw new MobileException(e);
		} catch (InvocationTargetException e) {
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
	
	public boolean isVoid() {
        return Void.TYPE.equals(method.getReturnType());
    }
	
	public Method getMethod() {
		return method;
	}
	
	public Object getReceiver() {
		return receiver;
	}
	
	public Object getUiObject() {
		return uiObject;
	}
	
	@Override
	public String toString() {
		return "[method: " + method.getName()
				+ ", Receiver: " + receiver
				+ ", UiObject: " + uiObject + "]";
	}
}
