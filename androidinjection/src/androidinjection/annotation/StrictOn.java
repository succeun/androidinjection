package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.StrictOnInjector;

/**
 * 새로운 Gingerbread API로 StrictMode를 설정해주는 어노테이션이다.
 * 사용하기 위해서는 반드시 AndroidManifest.xml내의 application 태그내에
 * android:debuggable="true" 속성을 사용하여 하며,
 * 반드시 API 2.3이상 즉, android:minSdkVersion="9" 이상이어야 한다. 
 * @author silver
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(StrictOnInjector.class)
public @interface StrictOn {
	//
}
