package androidinjection.annotation;

import java.lang.annotation.Annotation;

public abstract class FieldInjector extends Injector {
	
	public abstract void inject(Annotation annotation, FieldInvocation invocation, Object ... params);
}