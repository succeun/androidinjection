package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnBeforeTextChanged;


public class OnBeforeTextChangedInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnBeforeTextChanged.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnBeforeTextChanged)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");
	         
	        final TextView view = (TextView)invocation.findViewById(id);
	        if (view == null) {
	        	String idName = invocation.getResources().getResourceEntryName(id);
	        	Log.w(TAG, "ID['" + idName +"']의 View를 " + invocation.getUiObject() + "에서 찾을 수 없거나, 아직 화면 초기화가 되지 않았습니다.");
	        	return;
	        }
	        
	        final int type = getParameterType(allowedParamTypes, invocation.getMethod());
	        
	        view.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					//
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					invoke(argumentOrders[type], invocation, view, s, start, count, after);
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
			new Class[]{TextView.class, CharSequence.class, int.class, int.class, int.class},
			new Class[]{CharSequence.class, TextView.class, int.class, int.class, int.class},
			new Class[]{CharSequence.class, int.class, int.class, int.class},
			new Class[]{TextView.class, CharSequence.class},
			new Class[]{CharSequence.class, TextView.class},
			new Class[]{TextView.class},
			new Class[]{CharSequence.class}
	};
	
	// 0: view, 1: s, 2: start, 3: count, 4: after
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0, 1, 2, 3, 4},
			new int[]{1, 0, 2, 3, 4},
			new int[]{1, 2, 3, 4},
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
