package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.view.View;
import androidinjection.MobileException;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;
import androidinjection.annotation.ViewById;


public class ViewByIdInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return ViewById.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
		int id = ((ViewById)annotation).value();
        if (id == 0)
        	throw new MobileException("id�� ���� ���� �ʾҰų�, ã�� ���� �����ϴ�.");
        
        Class<?> fieldClass = invocation.getField().getType();
        View view = invocation.findViewById(id);
        if (view == null)
        	return;
         
        if (fieldClass.isAssignableFrom(view.getClass())) {
        	invocation.setValue(view);
        } else {
        	throw new MobileException("���̵�� ã�� View Ÿ�԰� ��ġ���� �ʽ��ϴ�.: " + fieldClass);
        }
         
        debug(invocation.toString());
	}

}
