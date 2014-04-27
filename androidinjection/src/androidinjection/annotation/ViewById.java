package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.ViewByIdInjector;
 
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(ViewByIdInjector.class)
public @interface ViewById {
    int value() default 0;
}
