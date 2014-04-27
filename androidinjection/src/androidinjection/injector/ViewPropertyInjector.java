package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.widget.CompoundButton;
import android.widget.TextView;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;
import androidinjection.annotation.ViewProperty;


public class ViewPropertyInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return ViewProperty.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
        String[] text = ((ViewProperty)annotation).text();
        if(text != null && text.length > 0) {
        	if (TextView.class.isAssignableFrom(invocation.getField().getType())) {
        		TextView view = (TextView) invocation.getValue();
        		view.setText(text[0]);
        	}
        }
        
        boolean[] checked = ((ViewProperty)annotation).checked();
        if(checked != null && checked.length > 0) {
        	if (CompoundButton.class.isAssignableFrom(invocation.getField().getType())) {
        		CompoundButton view = (CompoundButton) invocation.getValue();
        		view.setChecked(checked[0]);
        	}
        }
         
        debug(invocation.toString());
	}

}
