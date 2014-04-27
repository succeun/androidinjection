package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import android.app.Application;
import androidinjection.annotation.App;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;


public class AppInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return App.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
		Activity activity = (Activity)invocation.getReceiver();
    	Application value = (Application) activity.getApplication();
    	if (value != null) {
    		invocation.setValue(value);
    	}
         
        debug(invocation.toString());
	}

}
