package androidinjection.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidinjection.MobileException;
import androidinjection.injector.BackgroundInjector;
import androidinjection.injector.UiThreadInjector;

/**
 * �ش� Activity�� ���� ��ü�� ���� AnnotationManager��  �ϳ��� �ν��Ͻ��� �����ϸ�,
 * ��� Activity���� ���� ������̼��� ���, �ϳ��� Injector�� ����Ͽ�, ó���Ѵ�.
 * ����, ���� ������̼��� ���� �ʵ� �Ǵ� �޼ҵ忡 ���� �ִٸ�, Injector ������ �����Ͽ��� �Ѵ�.
 * @author Eun, Jeong-Ho
 */
public class AnnotationManager {
	private Map<Class<? extends Injector>, Injector> injectors = 
			new LinkedHashMap<Class<? extends Injector>, Injector>();
	
	private Map<Class<? extends Annotation>, List<AnnotationFieldItem>> fieldAnnotations = 
			new LinkedHashMap<Class<? extends Annotation>, List<AnnotationFieldItem>>();
	private Map<Class<? extends Annotation>, List<AnnotationMethodItem>> methodAnnotations = 
			new LinkedHashMap<Class<? extends Annotation>, List<AnnotationMethodItem>>();
	
	private Object receiver = null;
	
	/**
	 * ������̼ǿ� ���� �����Ǵ� �޼ҵ�, �ʵ���� ������ ��ü�� �����Ѵ�.
	 * @param receiver ������̼��� ������  ��ü
	 */
    public AnnotationManager(Object receiver) {
		this.receiver = receiver;
		// ��Ŭ������ VisualEditor ������� ���, �����Ѵ�. 
		if (receiver instanceof View) {
			if (((View)receiver).isInEditMode())
				return;
		}
		
		Class<?> clazz = receiver.getClass();
		List<Class<?>> classes = new ArrayList<Class<?>>();
		while (clazz != null) {
			classes.add(clazz);
			// �θ� Ŭ������ �ȵ���̵�, �ڹ� ��Ű���� �߰� �ߴ�
			clazz = clazz.getSuperclass();
	        if (clazz != null) {
	        	String packageName = clazz.getPackage().getName();
	        	if (packageName.startsWith("android.") || packageName.startsWith("java.")) {
	        		break;
	        	}
	        }
		}
		
		// Field �ʱ�ȭ
		// �θ��� �θ� Ŭ�������� �ʵ带 ã�Ƴ���.
		// ��, android, java ��Ű���� �����Ѵ�. (��, Activity ��)
		for (int i = classes.size() - 1; i >= 0; i--) {
			clazz = classes.get(i);
			Field[] fields = clazz.getDeclaredFields();
	        for (Field field : fields) {
	        	Annotation[] annotations = field.getAnnotations();
	    		for (Annotation annotation : annotations) {
	    			Class<? extends Annotation> annotationCls = annotation.annotationType();
	    			List<AnnotationFieldItem> items = fieldAnnotations.get(annotationCls);
	    			if (items == null) {
	    				items = new LinkedList<AnnotationFieldItem>();
	    				fieldAnnotations.put(annotationCls, items);
	    			}
	   				items.add(new AnnotationFieldItem(annotation, field));
	    		}
	        }
		}
		
		// Method �ʱ�ȭ
		// �θ��� �θ� Ŭ�������� �޼ҵ带 ã�Ƴ���.
		// ��, android, java ��Ű���� �����Ѵ�. (��, Activity ��)
		for (int i = classes.size() - 1; i >= 0; i--) {
			clazz = classes.get(i);
	        Method[] methods = clazz.getDeclaredMethods();
	        for (Method method : methods) {
	        	Annotation[] annotations = method.getAnnotations();
	    		for (Annotation annotation : annotations) {
	    			Class<? extends Annotation> annotationCls = annotation.annotationType();
	    			List<AnnotationMethodItem> items = methodAnnotations.get(annotationCls);
	    			if (items == null) {
	    				items = new LinkedList<AnnotationMethodItem>();
	    				methodAnnotations.put(annotationCls, items);
	    			}
	   				items.add(new AnnotationMethodItem(annotation, method));
	    		}
	        }
		}
	}
    
    private Injector createInjector(Class<? extends Injector> injectorClass) {
		try {
			Injector injector = injectors.get(injectorClass);
			if (injector == null) {
				injector = injectorClass.newInstance();
	    		injectors.put(injectorClass, injector);
	    	}
			return injector;
		} catch (IllegalAccessException e) {
			throw new MobileException(e);
		} catch (InstantiationException e) {
			throw new MobileException(e);
		}
	}
    
    private Injector getInjector(Class<? extends Annotation> anntationCls) {
		// FIXME: ��������� ���� anntationCls�� ���� injectorClass�� ĳ��ó�� �ʿ�
    	Injector injector = null;
		InjectorAnnotation inAnn = anntationCls.getAnnotation(InjectorAnnotation.class);
		if (inAnn != null) {
    		Class<? extends Injector> injectorClass = inAnn.value();
    		injector = createInjector(injectorClass);
		}
		return injector; 
	}

	
    
    public void inject() {
    	inject(receiver);
    }
    
    public void inject(Object uiObject) {
    	injectClass();
        injectFields(receiver);
        injectMethods(receiver);
    }
    /**
     * Ŭ���� ������̼ǿ� ���Ͽ� ��������.
     * @param params injector���� �Ѱ��� �Ķ����
     */
    public void injectClass(Object ... params) {
    	// FullScreen�� NoTitle�� ȣ������� ������ �ռ��� ������ ���� ����
    	{
	    	Annotation annotation = receiver.getClass().getAnnotation(FullScreen.class);
	    	if (annotation != null) {
	    		injectClass(annotation, params);
	    	}
    	}
    	{
			Annotation annotation = receiver.getClass().getAnnotation(NoTitle.class);
			if (annotation != null) {
				injectClass(annotation, params);
			}
    	}
		
    	// getAnnotations() ��ȯ�� ���� ������ ������� �迭 ��ȯ��
    	Annotation[] annotations = receiver.getClass().getAnnotations();
    	for (Annotation annotation : annotations) {
    		if (!(annotation instanceof FullScreen) && !(annotation instanceof NoTitle)) {
    			injectClass(annotation, params);
    		}
    	}
	}

	private void injectClass(Annotation annotation, Object... params) {
		Class<? extends Annotation> anntationCls = annotation.annotationType();
		ClassInjector injector = (ClassInjector) getInjector(anntationCls);
		if (injector != null) {
			ClassInvocation invocation = new ClassInvocation(receiver);
			injector.inject(annotation, invocation, params);
		}
	}
    
    /**
	 * �ʵ� ������̼ǿ� ���Ͽ� �����Ѵ�.
	 * @param uiObject findViewById(), getResources()�� UI�� ���õ� �޼ҵ带 ���� ��ü
	 *        ��, View, Activity�� �ڽ� ��ü�� ����ؾ� �Ѵ�.
	 * @param params injector���� �Ѱ��� �Ķ����
	 */
	public void injectFields(Object uiObject, Object ... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject�� View �Ǵ� Activity�� �ڽİ�ü�� ����մϴ�.");
		}
		
		Set<Class<? extends Annotation>> keys = fieldAnnotations.keySet();
		Iterator<Class<? extends Annotation>> itr = keys.iterator();
    	while(itr.hasNext()) {
    		Class<? extends Annotation> anntationCls = itr.next();
    		injectField(anntationCls, uiObject, params);
    	}
	}
	
	/**
	 * �ʵ� ������̼ǿ� ���Ͽ� �����Ѵ�.
	 * @param uiObject findViewById(), getResources()�� UI�� ���õ� �޼ҵ带 ���� ��ü
	 *        ��, View, Activity�� �ڽ� ��ü�� ����ؾ� �Ѵ�.
	 * @param params injector���� �Ѱ��� �Ķ����
	 */
	public void injectFields(Class<? extends Annotation>[] annotationClasses, Object uiObject, Object ... params) {
		for (Class<? extends Annotation> anntationCls : annotationClasses) {
			injectField(anntationCls, uiObject, params);
		}
	}

	public void injectField(Class<? extends Annotation> anntationCls, Object uiObject, Object... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject�� View �Ǵ� Activity�� �ڽİ�ü�� ����մϴ�.");
		}
		
		FieldInjector injector = (FieldInjector) getInjector(anntationCls);
		if (injector != null) {
			List<AnnotationFieldItem> items = fieldAnnotations.get(anntationCls);
			if (items != null) {
				for (AnnotationFieldItem item : items) {
					FieldInvocation invocation = new FieldInvocation(receiver, item.field, uiObject);
					injector.inject(item.annotation, invocation, params);
				}
			}
		}
	}
	
	/**
	 * �޼ҵ� ������̼ǿ� ���Ͽ� �����Ѵ�.
	 * @param uiObject findViewById(), getResources()�� UI�� ���õ� �޼ҵ带 ���� ��ü
	 *        ��, View, Activity�� �ڽ� ��ü�� ����ؾ� �Ѵ�.
	 * @param params injector���� �Ѱ��� �Ķ����
	 */
	public void injectMethods(Object uiObject, Object ... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject�� View �Ǵ� Activity�� �ڽİ�ü�� ����մϴ�.");
		}
		
		Set<Class<? extends Annotation>> keys = methodAnnotations.keySet();
        Iterator<Class<? extends Annotation>> itr = keys.iterator();
    	while(itr.hasNext()) {
    		Class<? extends Annotation> anntationCls = itr.next();
    		injectMethod(anntationCls, uiObject, params);
    	}
	}
	
	/**
	 * �޼ҵ� ������̼ǿ� ���Ͽ� �����Ѵ�.
	 * @param uiObject findViewById(), getResources()�� UI�� ���õ� �޼ҵ带 ���� ��ü
	 *        ��, View, Activity�� �ڽ� ��ü�� ����ؾ� �Ѵ�.
	 * @param params injector���� �Ѱ��� �Ķ����
	 */
	public void injectMethods(Class<? extends Annotation>[] annotationClasses, Object uiObject, Object ... params) {
		for (Class<? extends Annotation> anntationCls : annotationClasses) {
    		injectMethod(anntationCls, uiObject, params);
		}
	}
	
	public void injectMethod(Class<? extends Annotation> anntationCls, Object uiObject, Object... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject�� View �Ǵ� Activity�� �ڽİ�ü�� ����մϴ�.");
		}
		
		MethodInjector injector = (MethodInjector) getInjector(anntationCls);
		if (injector != null) {
			List<AnnotationMethodItem> items = methodAnnotations.get(anntationCls);
			if (items != null) {
				for (AnnotationMethodItem item : items) {
					MethodInvocation invocation = new MethodInvocation(receiver, item.method, uiObject);
					injector.inject(item.annotation, invocation, params);
				}
			}
		}
	}
	
	public class AnnotationFieldItem {
		public Annotation annotation = null;
		public Field field = null;
		public AnnotationFieldItem(Annotation annotation, Field field) {
			this.annotation = annotation;
			this.field = field;
		}
	}
	
	public class AnnotationMethodItem {
		public Annotation annotation = null;
		public Method method = null;
		public AnnotationMethodItem(Annotation annotation, Method method) {
			this.annotation = annotation;
			this.method = method;
		}
	}
	
	//_________________________________________________________________________
	// �⺻ ��ɿܿ� AnnotationManager�� �ʵ� �����Ͽ� ����ϴ� �߰� ��� 

	public boolean onCreateOptionsMenu(Activity activity, Menu menu) {
		((ClassInjector)getInjector(OptionsMenu.class)).perform(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return (Boolean) ((MethodInjector)getInjector(OnOptionsItem.class)).perform(item);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		((MethodInjector)getInjector(OnActivityResult.class)).perform(requestCode, resultCode, intent);
	}

	public void invokeBackground(String methodName, Object ... args) {
		((MethodInjector)getInjector(Background.class)).perform(methodName, args);
	}
	
	public void invokeBackground(Runnable runnable) {
		((BackgroundInjector)getInjector(Background.class)).invokeBackground(runnable);
	}
	
	public void invokeUiThread(String methodName, Object ... args) {
		((MethodInjector)getInjector(UiThread.class)).perform(methodName, args);
	}
	
	public void invokeUiThread(Runnable runnable) {
		((UiThreadInjector)getInjector(UiThread.class)).invokeUiThread(runnable);
	}
}
