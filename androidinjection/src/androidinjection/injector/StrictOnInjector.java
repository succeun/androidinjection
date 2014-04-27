package androidinjection.injector;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import androidinjection.MobileException;
import androidinjection.annotation.ClassInjector;
import androidinjection.annotation.ClassInvocation;
import androidinjection.annotation.StrictOn;


public class StrictOnInjector extends ClassInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return StrictOn.class;
	}

	@Override
	public void inject(Annotation annotation, ClassInvocation invocation, Object ... params) {
		Context context = (Context) invocation.getReceiver();
		
		boolean SUPPORT_STRICT_MODE = Build.VERSION_CODES.FROYO < Build.VERSION.SDK_INT;
		boolean debuggable = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		
		if (SUPPORT_STRICT_MODE && debuggable) {
			Class<?> strictMode = null;
			Method enableDefaults = null;
			try {
			    strictMode = Class.forName("android.os.StrictMode");
			} catch (ClassNotFoundException e) {
			     throw new MobileException(e);
			}

			if(strictMode != null){
			    try {
			       enableDefaults = strictMode.getMethod("enableDefaults", null);
			    } catch (NoSuchMethodException e) {
			    	 throw new MobileException(e);
			    }
			}

			if(enableDefaults != null){
			    try {
			        enableDefaults.invoke(null, null);
			    } catch (IllegalArgumentException e) {
			    	 throw new MobileException(e);
			    } catch (IllegalAccessException e) {
			    	 throw new MobileException(e);
			    } catch (InvocationTargetException e) {
			    	 throw new MobileException(e);
			    } 
			}
		}
         
		debug(invocation.toString());
	}
	
	@Override
	public Object perform(Object ... params) {
		return null;
	}

}
