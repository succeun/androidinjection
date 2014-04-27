package androidinjection.injector;


import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.annotation.Annotation;

import android.content.Context;
import androidinjection.UncaughtExceptionReporter;
import androidinjection.annotation.ClassInjector;
import androidinjection.annotation.ClassInvocation;
import androidinjection.annotation.ExceptionReporter;


public class ExceptionReporterInjector extends ClassInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return ExceptionReporter.class;
	}

	@Override
	public void inject(Annotation annotation, ClassInvocation invocation, Object ... params) {
		Context context = (Context) invocation.getReceiver();
		
		boolean isFile = ((ExceptionReporter)annotation).file();
		String serverUrl = ((ExceptionReporter)annotation).serverUrl();
		String encoding = ((ExceptionReporter)annotation).encoding();
		
		UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        if ( !(handler instanceof UncaughtExceptionReporter) ) {
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionReporter(context, isFile, serverUrl, encoding));
        }
         
        debug(invocation.toString());
	}
	
	@Override
	public Object perform(Object ... params) {
		return null;
	}

}
