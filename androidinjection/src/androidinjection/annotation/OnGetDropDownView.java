package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.OnGetDropDownViewInjector;
 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(OnGetDropDownViewInjector.class)
public @interface OnGetDropDownView {
	int[] value() default {};
}
