package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnNothingSelected;


public class OnNothingSelectedInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnNothingSelected.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnNothingSelected)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id�� ���� ���� �ʾҰų�, ã�� ���� �����ϴ�.");
	         
	        AdapterView<?> view = (AdapterView<?>)invocation.findViewById(id);
	        if (view == null) {
	        	String idName = invocation.getResources().getResourceEntryName(id);
	        	Log.w(TAG, "ID['" + idName +"']�� View�� " + invocation.getUiObject() + "���� ã�� �� ���ų�, ���� ȭ�� �ʱ�ȭ�� ���� �ʾҽ��ϴ�.");
	        	return;
	        }
	        
	        final int type = getParameterType(allowedParamTypes, invocation.getMethod());
	         
	        view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					//
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					invoke(argumentOrders[type], invocation, parent);
				}
	        });
	         
	        debug(invocation.toString());
        }
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{AdapterView.class}
	};
	
	// 0: parent
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0}
	};
	
	@Override
	public Object perform(Object... params) {
		return null;
	}
}
