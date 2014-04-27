package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnGetDropDownView;


public class OnGetDropDownViewInjector extends MethodInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return OnGetDropDownView.class;
	}

	@Override
	public void inject(Annotation annotation, MethodInvocation invocation, Object ... params) {
		int[] ids = ((OnGetDropDownView)annotation).value();
		for (int id : ids) {
			if (id == 0)
				throw new MobileException("id가 정의 되지 않았거나, 찾을 수가 없습니다.");

			// OnGetDropDownView만 Activity에서 정의하므로 강제적으로 Receiver 객체에서 찾는다.
			View view = ((Activity) invocation.getReceiver()).findViewById(id);
			if (view == null)
				return;

			final int type = getParameterType(allowedParamTypes, invocation.getMethod());

			if (params.length >= 4) {
				int position = (Integer) params[0];
				View convertView = (View) params[1];
				ViewGroup parentGroup = (ViewGroup) params[2];
				Object item = params[3];

				invoke(argumentOrders[type], invocation, position, convertView, parentGroup, item);

				debug(invocation.toString());
			}
        }
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{int.class, View.class, ViewGroup.class, Object.class},
			new Class[]{int.class, View.class, ViewGroup.class},
			new Class[]{View.class, int.class, ViewGroup.class},
			new Class[]{int.class, View.class, Object.class},
			new Class[]{int.class, View.class},
			new Class[]{View.class, int.class},
			new Class[]{View.class, Object.class},
			new Class[]{int.class, Object.class},
			new Class[]{int.class},
			new Class[]{View.class},
			new Class[]{Activity.class},
			new Class[]{Object.class}
	};
	
	// 0: position, 1: convertView, 2: parentGroup, 3: item, 4: activity
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0, 1, 2, 3},
			new int[]{0, 1, 2},
			new int[]{1, 0, 2},
			new int[]{0, 1, 3},
			new int[]{0, 1},
			new int[]{1, 0},
			new int[]{1, 3},
			new int[]{0, 3},
			new int[]{0},
			new int[]{1},
			new int[]{4},
			new int[]{3}
	};
	
	@Override
	public Object perform(Object... params) {
		return null;
	}

}
