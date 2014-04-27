package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnAfterTextChanged;


public class OnAfterTextChangedInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnAfterTextChanged.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnAfterTextChanged)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id�� ���� ���� �ʾҰų�, ã�� ���� �����ϴ�.");
	         
	        final TextView view = (TextView)invocation.findViewById(id);
	        if (view == null) {
	        	String idName = invocation.getResources().getResourceEntryName(id);
	        	Log.w(TAG, "ID['" + idName +"']�� View�� " + invocation.getUiObject() + "���� ã�� �� ���ų�, ���� ȭ�� �ʱ�ȭ�� ���� �ʾҽ��ϴ�.");
	        	return;
	        }
	        
	        final int type = getParameterType(allowedParamTypes, invocation.getMethod());
	        
	        view.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					invoke(argumentOrders[type], invocation, view, s);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					//
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					//
				}
	        	
	        });
	        
	        debug(invocation.toString());
        }
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{TextView.class, Editable.class},
			new Class[]{Editable.class, TextView.class},
			new Class[]{TextView.class},
			new Class[]{Editable.class}
	};
	
	// 0: view, 1: s
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
