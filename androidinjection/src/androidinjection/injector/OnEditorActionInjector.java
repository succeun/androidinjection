package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnEditorAction;


public class OnEditorActionInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnEditorAction.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
		int[] ids = ((OnEditorAction)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");
	         
	        TextView view = (TextView) invocation.findViewById(id);
	        if (view == null) {
	        	String idName = invocation.getResources().getResourceEntryName(id);
	        	Log.w(TAG, "ID['" + idName +"']의 View를 " + invocation.getUiObject() + "에서 찾을 수 없거나, 아직 화면 초기화가 되지 않았습니다.");
	        	return;
	        }
	        
	        final boolean isVoid = invocation.isVoid();
	        final int type = getParameterType(allowedParamTypes, invocation.getMethod());
	         
	        view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int id, KeyEvent event) {
					Object returnValue = invoke(argumentOrders[type], invocation, v, id, event);
					return (Boolean) (isVoid ? false : returnValue);
				}
	        });
	         
	        debug(invocation.toString());
        }
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{View.class, int.class, KeyEvent.class},
			new Class[]{int.class, View.class, KeyEvent.class},
			new Class[]{View.class, KeyEvent.class, int.class},
			new Class[]{View.class, int.class},
			new Class[]{int.class, KeyEvent.class},
			new Class[]{KeyEvent.class, int.class},
			new Class[]{View.class, KeyEvent.class},
			new Class[]{KeyEvent.class, View.class},
			new Class[]{int.class},
			new Class[]{View.class},
			new Class[]{KeyEvent.class}
	};
	
	// 0: v, 1: id, 2: event
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0, 1, 2},
			new int[]{1, 0, 2},
			new int[]{0, 2, 1},
			new int[]{0, 1},
			new int[]{1, 2},
			new int[]{2, 1},
			new int[]{0, 2},
			new int[]{2, 0},
			new int[]{1},
			new int[]{0},
			new int[]{2}
	};
	
	@Override
	public Object perform(Object... params) {
		return null;
	}

}
