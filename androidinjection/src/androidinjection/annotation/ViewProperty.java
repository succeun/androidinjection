package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.ViewPropertyInjector;
 
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(ViewPropertyInjector.class)
public @interface ViewProperty {
    String[] text() default {};
    boolean[] checked() default {} ;
}
