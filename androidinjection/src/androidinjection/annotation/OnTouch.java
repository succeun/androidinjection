package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.OnTouchInjector;
 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(OnTouchInjector.class)
public @interface OnTouch {
	int[] value() default {};
}
