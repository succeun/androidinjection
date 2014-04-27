package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.util.Log;
import android.view.View;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnClick;


public class OnClickInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnClick.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnClick)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id�� ���� ���� �ʾҰų�, ã�� ���� �����ϴ�.");
	         
	        View view = invocation.findViewById(id);
	        if (view == null) {
	        	String idName = invocation.getResources().getResourceEntryName(id);
	        	Log.w(TAG, "ID['" + idName +"']�� View�� " + invocation.getUiObject() + "���� ã�� �� ���ų�, ���� ȭ�� �ʱ�ȭ�� ���� �ʾҽ��ϴ�.");
	        	return;
	        }
	        
	        final int type = getParameterType(allowedParamTypes, invocation.getMethod());
	         
	        view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					invoke(argumentOrders[type], invocation, v);
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
