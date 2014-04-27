package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import android.view.Window;
import androidinjection.annotation.ClassInjector;
import androidinjection.annotation.ClassInvocation;
import androidinjection.annotation.NoTitle;


public class NoTitleInjector extends ClassInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return NoTitle.class;
	}

	@Override
	public void inject(Annotation annotation, ClassInvocation invocation, Object ... params) {
		Activity activity = (Activity) invocation.getReceiver();
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
         
		debug(invocation.toString());
	}
	
	@Override
	public Object perform(Object ... params) {
		return null;
	}

}
