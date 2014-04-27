package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import android.view.WindowManager;
import androidinjection.annotation.ClassInjector;
import androidinjection.annotation.ClassInvocation;
import androidinjection.annotation.FullScreen;


public class FullScreenInjector extends ClassInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return FullScreen.class;
	}

	@Override
	public void inject(Annotation annotation, ClassInvocation invocation, Object ... params) {
		Activity activity = (Activity) invocation.getReceiver();
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         
		debug(invocation.toString());
	}
	
	@Override
	public Object perform(Object ... params) {
		return null;
	}

}
