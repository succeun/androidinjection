package androidinjection.injector;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnCheckedChange;


public class OnCheckedChangeInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnCheckedChange.class;
	}

	@Override
	public void inject(Annotation annotation, final MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnCheckedChange)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");
	        
	        View view = (View)invocation.findViewById(id);
	        if (view == null) {
	        	String idName = invocation.getResources().getResourceEntryName(id);
	        	Log.w(TAG, "ID['" + idName +"']의 View를 " + invocation.getUiObject() + "에서 찾을 수 없거나, 아직 화면 초기화가 되지 않았습니다.");
	        	return;
	        }
	        
	        if (CompoundButton.class.isAssignableFrom(view.getClass())) {
	        	CompoundButton button = (CompoundButton) view;
		        
		        final int type = getParameterType(allowedParamTypesByCompoundButton, invocation.getMethod());
		         
		        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						invoke(argumentOrdersByCompoundButton[type], invocation, buttonView, isChecked);
					}
				});
	        } else if (RadioGroup.class.isAssignableFrom(view.getClass())) {
	        	RadioGroup radioGroup = (RadioGroup) view;
		        
		        final int type = getParameterType(allowedParamTypesByRadioGroup, invocation.getMethod());
		         
		        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						invoke(argumentOrdersByRadioGroup[type], invocation, group, checkedId);
					}
				});
	        }
	         
	        debug(invocation.toString());
        }
	}
	
	private Class<?>[][] allowedParamTypesByCompoundButton = new Class<?>[][]{
			new Class[]{},
			new Class[]{CompoundButton.class, Boolean.class},
			new Class[]{Boolean.class, CompoundButton.class},
			new Class[]{CompoundButton.class},
			new Class[]{Boolean.class}
	};
	
	// 0: buttonView, 1: isChecked
	private int[][] argumentOrdersByCompoundButton = new int[][]{
			new int[]{},
			new int[]{0, 1},
			new int[]{1, 0},
			new int[]{0},
			new int[]{1}
	};
	
	private Class<?>[][] allowedParamTypesByRadioGroup = new Class<?>[][]{
			new Class[]{},
			new Class[]{RadioGroup.class, int.class},
			new Class[]{int.class, RadioGroup.class},
			new Class[]{RadioGroup.class},
			new Class[]{int.class}
	};
	
	// 0: group, 1: checkedId
	private int[][] argumentOrdersByRadioGroup = new int[][]{
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
