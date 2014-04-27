package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import androidinjection.annotation.ClassInjector;
import androidinjection.annotation.ClassInvocation;
import androidinjection.annotation.OptionsMenu;


public class OptionsMenuInjector extends ClassInjector {
	
	private Activity activity = null;
	private int[] optionsMenuIds = null;
	
	@Override
	public Class<? extends Annotation> getTarget() {
		return OptionsMenu.class;
	}

	@Override
	public void inject(Annotation annotation, ClassInvocation invocation, Object ... params) {
        optionsMenuIds = ((OptionsMenu)annotation).value();
        activity = (Activity) invocation.getReceiver();
        
        debug(invocation.toString());
	}
	
	@Override
	public Object perform(Object ... params) {
		Menu menu = (Menu) params[0];
		if (optionsMenuIds!= null && activity != null) {
			MenuInflater inflater = activity.getMenuInflater();
			if (optionsMenuIds != null) {
				for (int id : optionsMenuIds) {
					inflater.inflate(id, menu);
				}
			}
		}
		
		return null;
	}

}
