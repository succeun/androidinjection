package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.RootCheckInjector;

/**
 * 루팅 체크, ADB 연결 여부, 디버그 연결 여부, USB 연결 여부, AC 충전 연결 여부를 
 * 확인하여, 팝업을 보여준 후, 프로그램을 종료 한다.
 * 단, 팝업(ActivityPopup)으로 인해, AndroidManifest.xml에 등록이 필요하다.
 * <activity android:name="hi.mobile.widget.ActivityPopup" 
 *          android:theme="@android:style/Theme.Dialog" /> 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(RootCheckInjector.class)
public @interface RootCheck {
	/**
	 * 루팅 여부
	 * @return 여부값
	 */
	boolean root() default true;
	/**
	 * ADB 연결 여부
	 * @return 여부값
	 */
	boolean adb() default false;
	/**
	 * 디버그 연결 여부
	 * @return 여부값
	 */
	boolean debug() default true;
	/**
	 * USB 연결 여부
	 * @return 여부값
	 */
	boolean usb() default true;
	/**
	 * AC 충전 여부
	 * @return 여부값
	 */
	boolean ac() default false;
	/**
	 * 프로세스를 종료할지 여부
	 * @return 여부값
	 */
	boolean killProcess() default true;
}
