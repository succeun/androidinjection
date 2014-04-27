package androidinjection.injector;


import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import androidinjection.MobileException;
import androidinjection.annotation.Background;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;


public class BackgroundInjector extends MethodInjector {
	private ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	private Map<String, MethodInvocation> backgroundMethods = new HashMap<String, MethodInvocation>();
	
	@Override
	public Class<? extends Annotation> getTarget() {
		return Background.class;
	}

	@Override
	public void inject(Annotation annotation, MethodInvocation invocation, Object ... params) {
		backgroundMethods.put(invocation.getMethod().getName(), invocation);
	}

	@Override
	public Object perform(Object... params) {
		String methodName = (String) params[0];
		Object[] args = (Object[]) params[1];
		MethodInvocation invocation = backgroundMethods.get(methodName);
		if (invocation == null)
			throw new MobileException("'" + methodName + "'에 대응하는 메소드가 정의되지 않았습니다.");
		BackgroundWorker worker = new BackgroundWorker(invocation, args);
		threadExecutor.execute(worker);
		return null;
	}
	
	public void invokeBackground(Runnable runnable) {
		threadExecutor.execute(runnable);
	}
	
	private void fireException(MethodInvocation invocation, final Throwable t) {
		((Activity)invocation.getReceiver()).runOnUiThread(new Runnable() {
    		public void run() {
    			throw new MobileException(t);
    		}
    	});
	}
	
	private final class BackgroundWorker implements Runnable {
		private Object[] args;
		private MethodInvocation invocation;

		private BackgroundWorker(MethodInvocation invocation, Object ... args) {
			this.invocation = invocation;
			this.args = args;
		}
		
		@Override
		public void run() {
		    try {
		    	invocation.invoke(args);
		    } catch (Exception e) {
		    	fireException(invocation, e);
			}
		}
	}

}
