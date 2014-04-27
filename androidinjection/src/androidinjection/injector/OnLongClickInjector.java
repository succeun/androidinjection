package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.util.Log;
import android.view.View;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnLongClick;


public class OnLongClickInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnLongClick.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnLongClick)annotation).value();
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
	         
	        view.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Object returnValue = invoke(argumentOrders[type], invocation, v);
					return (Boolean) (isVoid ? false : returnValue);
				}
	        });
	         
	        debug(invocation.toString());
        }
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{View.class}
	};
	
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0}
	};
	
	@Override
	public Object perform(Object... params) {
		return null;
	}

}
