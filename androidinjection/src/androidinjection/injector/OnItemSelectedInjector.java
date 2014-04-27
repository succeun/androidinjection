package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnItemSelected;


public class OnItemSelectedInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnItemSelected.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnItemSelected)annotation).value();
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
					Object clickedItem = parent.getItemAtPosition(position);
					invoke(argumentOrders[type], invocation, parent, view, position, id, clickedItem);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					//
				}
	        });
	         
	        debug(invocation.toString());
        }
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{AdapterView.class, View.class, int.class, long.class},
			new Class[]{View.class, int.class, long.class},
			new Class[]{int.class, View.class, Object.class},
			new Class[]{View.class, int.class, Object.class},
			new Class[]{int.class, long.class},
			new Class[]{int.class, View.class},
			new Class[]{long.class, int.class},
			new Class[]{long.class, View.class},
			new Class[]{View.class, int.class},
			new Class[]{View.class, long.class},
			new Class[]{int.class, Object.class},
			new Class[]{long.class, Object.class},
			new Class[]{View.class, Object.class},
			new Class[]{int.class},
			new Class[]{View.class},
			new Class[]{Object.class}
	};
	
	// 0: parent, 1: view, 2: position, 3: id, 4: clickedItem
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0, 1, 2, 3},
			new int[]{1, 2, 3},
			new int[]{2, 1, 4},
			new int[]{1, 2, 4},
			new int[]{2, 3},
			new int[]{2, 1},
			new int[]{3, 2},
			new int[]{3, 1},
			new int[]{1, 2},
			new int[]{1, 3},
			new int[]{2, 4},
			new int[]{3, 4},
			new int[]{1, 4},
			new int[]{2},
			new int[]{1},
			new int[]{3}
	};
	
	@Override
	public Object perform(Object... params) {
		return null;
	}
}
