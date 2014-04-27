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
 * 해당 Activity와 같이 객체에 대해 AnnotationManager는  하나의 인스턴스만 존재하며,
 * 대상 Activity내에 동일 어노테이션의 경우, 하나의 Injector가 담당하여, 처리한다.
 * 따라서, 동일 어노테이션이 여러 필드 또는 메소드에 걸쳐 있다면, Injector 내에서 관리하여야 한다.
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
	 * 어노테이션에 의해 관리되는 메소드, 필드등을 가지는 객체를 정의한다.
	 * @param receiver 어노테이션을 가지는  객체
	 */
    public AnnotationManager(Object receiver) {
		this.receiver = receiver;
		// 이클립스의 VisualEditor 편집모드 라면, 리턴한다. 
		if (receiver instanceof View) {
			if (((View)receiver).isInEditMode())
				return;
		}
		
		Class<?> clazz = receiver.getClass();
		List<Class<?>> classes = new ArrayList<Class<?>>();
		while (clazz != null) {
			classes.add(clazz);
			// 부모 클래스가 안드로이드, 자바 팩키지만 추가 중단
			clazz = clazz.getSuperclass();
	        if (clazz != null) {
	        	String packageName = clazz.getPackage().getName();
	        	if (packageName.startsWith("android.") || packageName.startsWith("java.")) {
	        		break;
	        	}
	        }
		}
		
		// Field 초기화
		// 부모의 부모 클래스까지 필드를 찾아낸다.
		// 단, android, java 팩키지는 제외한다. (예, Activity 등)
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
		
		// Method 초기화
		// 부모의 부모 클래스까지 메소드를 찾아낸다.
		// 단, android, java 팩키지는 제외한다. (예, Activity 등)
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
		// FIXME: 성능향상을 위해 anntationCls에 따른 injectorClass를 캐쉬처리 필요
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
     * 클래스 어노테이션에 대하여 주입힌다.
     * @param params injector에게 넘겨질 파라미터
     */
    public void injectClass(Object ... params) {
    	// FullScreen과 NoTitle은 호출순서가 무조건 앞서야 함으로 강제 적용
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
		
    	// getAnnotations() 반환시 선언 순서에 상관없이 배열 반환됨
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
	 * 필드 어노테이션에 대하여 주입한다.
	 * @param uiObject findViewById(), getResources()등 UI와 관련된 메소드를 가진 객체
	 *        단, View, Activity의 자식 객체만 사용해야 한다.
	 * @param params injector에게 넘겨질 파라미터
	 */
	public void injectFields(Object uiObject, Object ... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject는 View 또는 Activity의 자식객체만 허용합니다.");
		}
		
		Set<Class<? extends Annotation>> keys = fieldAnnotations.keySet();
		Iterator<Class<? extends Annotation>> itr = keys.iterator();
    	while(itr.hasNext()) {
    		Class<? extends Annotation> anntationCls = itr.next();
    		injectField(anntationCls, uiObject, params);
    	}
	}
	
	/**
	 * 필드 어노테이션에 대하여 주입한다.
	 * @param uiObject findViewById(), getResources()등 UI와 관련된 메소드를 가진 객체
	 *        단, View, Activity의 자식 객체만 사용해야 한다.
	 * @param params injector에게 넘겨질 파라미터
	 */
	public void injectFields(Class<? extends Annotation>[] annotationClasses, Object uiObject, Object ... params) {
		for (Class<? extends Annotation> anntationCls : annotationClasses) {
			injectField(anntationCls, uiObject, params);
		}
	}

	public void injectField(Class<? extends Annotation> anntationCls, Object uiObject, Object... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject는 View 또는 Activity의 자식객체만 허용합니다.");
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
	 * 메소드 어노테이션에 대하여 주입한다.
	 * @param uiObject findViewById(), getResources()등 UI와 관련된 메소드를 가진 객체
	 *        단, View, Activity의 자식 객체만 사용해야 한다.
	 * @param params injector에게 넘겨질 파라미터
	 */
	public void injectMethods(Object uiObject, Object ... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject는 View 또는 Activity의 자식객체만 허용합니다.");
		}
		
		Set<Class<? extends Annotation>> keys = methodAnnotations.keySet();
        Iterator<Class<? extends Annotation>> itr = keys.iterator();
    	while(itr.hasNext()) {
    		Class<? extends Annotation> anntationCls = itr.next();
    		injectMethod(anntationCls, uiObject, params);
    	}
	}
	
	/**
	 * 메소드 어노테이션에 대하여 주입한다.
	 * @param uiObject findViewById(), getResources()등 UI와 관련된 메소드를 가진 객체
	 *        단, View, Activity의 자식 객체만 사용해야 한다.
	 * @param params injector에게 넘겨질 파라미터
	 */
	public void injectMethods(Class<? extends Annotation>[] annotationClasses, Object uiObject, Object ... params) {
		for (Class<? extends Annotation> anntationCls : annotationClasses) {
    		injectMethod(anntationCls, uiObject, params);
		}
	}
	
	public void injectMethod(Class<? extends Annotation> anntationCls, Object uiObject, Object... params) {
		if (!(uiObject instanceof View) && !(uiObject instanceof Activity)) {
			throw new MobileException("uiObject는 View 또는 Activity의 자식객체만 허용합니다.");
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
	// 기본 기능외에 AnnotationManager를 필드 보관하여 사용하는 추가 기능 

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
