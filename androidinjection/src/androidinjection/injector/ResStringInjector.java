package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import androidinjection.MobileException;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;
import androidinjection.annotation.ResString;


public class ResStringInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return ResString.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
		int id = ((ResString)annotation).value();
        if (id == 0)
        	throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");
        
        Activity activity = (Activity) invocation.getReceiver();
        String value = activity.getResources().getString(id);
        if (value == null)
        	return;
         
        if (value.getClass() == invocation.getField().getType()) {
        	invocation.setValue(value);
        }
         
        debug(invocation.toString());
	}

}
