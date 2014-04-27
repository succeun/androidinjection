package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import androidinjection.MobileException;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;
import androidinjection.annotation.ResColor;


public class ResColorInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return ResColor.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
		int id = ((ResColor)annotation).value();
        if (id == 0)
        	throw new MobileException("id�� ���� ���� �ʾҰų�, ã�� ���� �����ϴ�.");
        
        Activity activity = (Activity) invocation.getReceiver();
        int value = activity.getResources().getColor(id);
        if (Integer.TYPE.equals(invocation.getField().getType())) {
        	invocation.setValue(value);
        }
         
        debug(invocation.toString());
	}

}
