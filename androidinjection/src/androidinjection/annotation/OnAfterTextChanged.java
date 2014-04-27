package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.OnAfterTextChangedInjector;
 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(OnAfterTextChangedInjector.class)
public @interface OnAfterTextChanged {
	int[] value() default {};
}
