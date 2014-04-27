package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.OnItemClickInjector;
 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(OnItemClickInjector.class)
public @interface OnItemClick {
	int[] value() default {};
}
