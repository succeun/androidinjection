package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.StrictOnInjector;

/**
 * ���ο� Gingerbread API�� StrictMode�� �������ִ� ������̼��̴�.
 * ����ϱ� ���ؼ��� �ݵ�� AndroidManifest.xml���� application �±׳���
 * android:debuggable="true" �Ӽ��� ����Ͽ� �ϸ�,
 * �ݵ�� API 2.3�̻� ��, android:minSdkVersion="9" �̻��̾�� �Ѵ�. 
 * @author silver
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(StrictOnInjector.class)
public @interface StrictOn {
	//
}
