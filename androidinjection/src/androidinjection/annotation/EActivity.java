package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.injector.EActivityInjector;
 
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(EActivityInjector.class)
public @interface EActivity {
    int value() default 0;
    String title() default "";
}
