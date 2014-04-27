package androidinjection.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidinjection.UncaughtExceptionReporter;
import androidinjection.injector.ExceptionReporterInjector;

 
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InjectorAnnotation(ExceptionReporterInjector.class)
public @interface ExceptionReporter {
	boolean file() default true;
	String serverUrl() default "";
	String encoding() default UncaughtExceptionReporter.DEFAULT_ENCODING;
}
