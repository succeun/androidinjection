package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.ExtraInjector;
 
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(ExtraInjector.class)
public @interface Extra {
    String value() default "";
}
