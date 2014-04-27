package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.OptionsMenuInjector;
 
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(OptionsMenuInjector.class)
public @interface OptionsMenu {
    int[] value() default 0;
}
