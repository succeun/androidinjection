package androidinjection.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import androidinjection.MobileException;


public abstract class MethodInjector extends Injector  {
	
	protected int getParameterType(Class<?>[][] allowedParamTypes, Method method) {
		Class<?>[] paramTypes = method.getParameterTypes();
		for (int p = 0; p < allowedParamTypes.length; p++) {
			Class<?>[] item = allowedParamTypes[p];
			if (item.length == paramTypes.length) {
				boolean isEqual = true;
				for (int i = 0; isEqual && i < item.length; i++) {
					if (item[i].isAssignableFrom(paramTypes[i])) {
						isEqual = true;
					} else {
						isEqual = false;
					}
				}
				
				if (isEqual) {
					return p;
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(method.getName()).append("(");
		String seperator = "";
		for (Class<?> paramType : paramTypes) {
			sb.append(seperator);
			sb.append(paramType.getName());
			seperator = ",";
		}
		sb.append(")");
		
		throw new MobileException("현재 지원하지 않는 파라미터 인자형태입니다.: " + sb.toString());
	}
	
	protected Object invoke(int[] order, MethodInvocation invocation, Object ... args) {
		Object[] tmp = new Object[order.length];
		for (int i = 0; i < order.length; i++) {
			tmp[i] = args[order[i]];
		}
		return invocation.invoke(tmp);
	}
    
	public abstract void inject(Annotation annotation, MethodInvocation invocation, Object ... params);
    
    public abstract Object perform(Object ... params);
}