package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.util.SparseArray;
import android.view.MenuItem;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.OnOptionsItem;


public class OnOptionsItemInjector extends MethodInjector {

	private SparseArray<MethodInvocation> onOptionsItemMethods = new SparseArray<MethodInvocation>();
	
	@Override
	public Class<? extends Annotation> getTarget() {
		return OnOptionsItem.class;
	}

	@Override
	public void inject(Annotation annotation, MethodInvocation invocation, Object ... params) {
        int[] ids = ((OnOptionsItem)annotation).value();
        for (int id : ids) {
	        if (id == 0)
	        	throw new MobileException("id�� ���� ���� �ʾҰų�, ã�� ���� �����ϴ�.");
	        
	        onOptionsItemMethods.append(id, invocation);
        }
	}

	@Override
	public Object perform(Object... params) {
		MenuItem item = (MenuItem) params[0];
		if (item != null) {
			MethodInvocation invocation = onOptionsItemMethods.get(item.getItemId());
			if (invocation == null)
				throw new MobileException("�޴� '" + item + "'�� �����ϴ� �޼ҵ尡 ���ǵ��� �ʾҽ��ϴ�.");
			
			boolean isVoid = invocation.isVoid();
			
			int type = getParameterType(allowedParamTypes, invocation.getMethod());
			
			Object returnValue = invoke(argumentOrders[type], invocation, item);
			return (Boolean) (isVoid ? true : returnValue);
		}
		return true;
	}
	
	private Class<?>[][] allowedParamTypes = new Class<?>[][]{
			new Class[]{},
			new Class[]{MenuItem.class}
	};
	
	private int[][] argumentOrders = new int[][]{
			new int[]{},
			new int[]{0}
	};

}
