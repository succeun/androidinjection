package androidinjection.injector;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import android.content.Intent;
import android.util.SparseArray;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnActivityResult;


public class OnActivityResultInjector extends MethodInjector {

	private SparseArray<MethodInvocation> onActivityResultMethods = new SparseArray<MethodInvocation>();
	
	@Override
	public Class<? extends Annotation> getTarget() {
		return OnActivityResult.class;
	}

	@Override
	public void inject(Annotation annotation, MethodInvocation invocation, Object ... params) {
        int[] codes = ((OnActivityResult)annotation).value();
        for (int code : codes) {
	        onActivityResultMethods.append(code, invocation);
        }
	}

	@Override
	public Object perform(Object... params) {
		int requestCode = (Integer) params[0];
		int resultCode = (Integer) params[1];
		Intent intent = (Intent) params[2];
		MethodInvocation invocation = onActivityResultMethods.get(requestCode);
		if (invocation == null)
			throw new MobileException("'" + requestCode + "'에 대응하는 메소드가 정의되지 않았습니다.");
		Method method = invocation.getMethod();
		
		final int type = getParameterType(allowedParamTypes, method);
		
		if (method != null) {
			invoke(argumentOrders[type], invocation, requestCode, resultCode, intent);
		}
		return null;
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{int.class, int.class, Intent.class},
			new Class[]{int.class, Intent.class},
			new Class[]{Intent.class, int.class},
			new Class[]{int.class},
			new Class[]{Intent.class}
	};
	
	// 0: requestCode, 1: resultCode, 2: intent
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0, 1, 2},
			new int[]{1, 2},
			new int[]{0, 1},
			new int[]{1},
			new int[]{2}
	};

}
