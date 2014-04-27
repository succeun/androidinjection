package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.RootCheckInjector;

/**
 * ���� üũ, ADB ���� ����, ����� ���� ����, USB ���� ����, AC ���� ���� ���θ� 
 * Ȯ���Ͽ�, �˾��� ������ ��, ���α׷��� ���� �Ѵ�.
 * ��, �˾�(ActivityPopup)���� ����, AndroidManifest.xml�� ����� �ʿ��ϴ�.
 * <activity android:name="hi.mobile.widget.ActivityPopup" 
 *          android:theme="@android:style/Theme.Dialog" /> 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(RootCheckInjector.class)
public @interface RootCheck {
	/**
	 * ���� ����
	 * @return ���ΰ�
	 */
	boolean root() default true;
	/**
	 * ADB ���� ����
	 * @return ���ΰ�
	 */
	boolean adb() default false;
	/**
	 * ����� ���� ����
	 * @return ���ΰ�
	 */
	boolean debug() default true;
	/**
	 * USB ���� ����
	 * @return ���ΰ�
	 */
	boolean usb() default true;
	/**
	 * AC ���� ����
	 * @return ���ΰ�
	 */
	boolean ac() default false;
	/**
	 * ���μ����� �������� ����
	 * @return ���ΰ�
	 */
	boolean killProcess() default true;
}
