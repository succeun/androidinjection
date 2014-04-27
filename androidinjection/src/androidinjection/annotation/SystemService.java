package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.SystemServiceInjector;
 
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(SystemServiceInjector.class)
public @interface SystemService {
    //
}
