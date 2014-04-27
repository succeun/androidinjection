package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.OnEditorActionInjector;
 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(OnEditorActionInjector.class)
public @interface OnEditorAction {
	int[] value() default {};
}
