package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import androidinjection.MobileException;
import androidinjection.annotation.ClassInjector;
import androidinjection.annotation.ClassInvocation;
import androidinjection.annotation.EActivity;


public class EActivityInjector extends ClassInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return EActivity.class;
	}

	@Override
	public void inject(Annotation annotation, ClassInvocation invocation, Object ... params) {
		Activity activity = (Activity) invocation.getReceiver();
		int id = ((EActivity)annotation).value();
        if (id == 0)
        	throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");
         
    	activity.setContentView(id);
    	
    	String title = ((EActivity)annotation).title();
    	if (title != null && title.length() > 0) {
    		activity.setTitle(title);
    	}
        
    	debug(invocation.toString());
	}
	
	@Override
	public Object perform(Object ... params) {
		return null;
	}

}
