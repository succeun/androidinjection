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
        	throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");
        
        Class<?> fieldClass = invocation.getField().getType();
        View view = invocation.findViewById(id);
        if (view == null)
        	return;
         
        if (fieldClass.isAssignableFrom(view.getClass())) {
        	invocation.setValue(view);
        } else {
        	throw new MobileException("아이디로 찾은 View 타입과 일치하지 않습니다.: " + fieldClass);
        }
         
        debug(invocation.toString());
	}

}
