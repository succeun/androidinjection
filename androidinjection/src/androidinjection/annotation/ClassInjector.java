package androidinjection.annotation;

import java.lang.annotation.Annotation;

public abstract class ClassInjector extends Injector  {
    public abstract void inject(Annotation annotation, ClassInvocation invocation, Object ... params);
	
	public abstract Object perform(Object ... params);
}