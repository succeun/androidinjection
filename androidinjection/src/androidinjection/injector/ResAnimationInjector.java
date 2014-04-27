package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidinjection.MobileException;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;
import androidinjection.annotation.ResAnimation;


public class ResAnimationInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return ResAnimation.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
		int id = ((ResAnimation)annotation).value();
        if (id == 0)
        	throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");
        Activity activity = (Activity) invocation.getReceiver(); 
        Animation value = AnimationUtils.loadAnimation(activity, id);
        if (value == null)
        	return;
         
        if (Animation.class.isAssignableFrom(invocation.getField().getType())) {
        	invocation.setValue(value);
        }
         
        debug(invocation.toString());
	}

}
