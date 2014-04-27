package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.ResAnimationInjector;
 
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(ResAnimationInjector.class)
public @interface ResAnimation {
    int value() default 0;
}
