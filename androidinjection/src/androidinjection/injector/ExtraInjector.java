package androidinjection.injector;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import androidinjection.annotation.Extra;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;
import androidinjection.util.bean.conversion.Convertor;


public class ExtraInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return Extra.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
        String key = ((Extra)annotation).value();
        Field field = invocation.getField();
        if(key == null || key.length() <= 0) {
            key = field.getName();
        }
        Activity activity = (Activity) invocation.getReceiver();
        Object value = null; 
        if (Bundle.class.isAssignableFrom(field.getType())) {
        	value = activity.getIntent().getExtras();
        	invocation.setValue(value);
        } else {
        	value = Convertor.convert(field.getType(), activity.getIntent().getExtras().get(key));
        	invocation.setValue(value);
        }
         
        debug(invocation.toString() + " value: " + value);
	}

}
