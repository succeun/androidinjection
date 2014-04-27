package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnTouch;


public class OnTouchInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnTouch.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
		int[] ids = ((OnTouch)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id�� ���� ���� �ʾҰų�, ã�� ���� �����ϴ�.");
	         
	        View view = invocation.findViewById(id);
	        if (view == null) {
	        	String idName = invocation.getResources().getResourceEntryName(id);
	        	Log.w(TAG, "ID['" + idName +"']�� View�� " + invocation.getUiObject() + "���� ã�� �� ���ų�, ���� ȭ�� �ʱ�ȭ�� ���� �ʾҽ��ϴ�.");
	        	return;
	        }
	        
	        final boolean isVoid = invocation.isVoid();
	        final int type = getParameterType(allowedParamTypes, invocation.getMethod());
	         
	        view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Object returnValue = invoke(argumentOrders[type], invocation, v, event);
					return (Boolean) (isVoid ? false : returnValue);
				}
	        });
	         
	        debug(invocation.toString());
        }
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{View.class, MotionEvent.class},
			new Class[]{MotionEvent.class, View.class},
			new Class[]{View.class},
			new Class[]{MotionEvent.class}
	};
	
	// 0: v, 1: event
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0, 1},
			new int[]{1, 0},
			new int[]{0},
			new int[]{1}
	};
	
	@Override
	public Object perform(Object... params) {
		return null;
	}

}
