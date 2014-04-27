package androidinjection.injector;


import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import androidinjection.MobileException;
import androidinjection.annotation.MethodInjector;
import androidinjection.annotation.MethodInvocation;
import androidinjection.annotation.UiThread;


public class UiThreadInjector extends MethodInjector {
	private Handler uiHandler = new Handler(Looper.getMainLooper());
	
	private Map<String, UiThreadMethodItem> uiThreadMethods = new HashMap<String, UiThreadMethodItem>();
	
	@Override
	public Class<? extends Annotation> getTarget() {
		return UiThread.class;
	}

	@Override
	public void inject(Annotation annotation, MethodInvocation invocation, Object ... params) {
		long delay = ((UiThread) annotation).delay();
		uiThreadMethods.put(invocation.getMethod().getName(), new UiThreadMethodItem(invocation, delay));
	}
	
	private class UiThreadMethodItem {
		public long delay = 0;
		public MethodInvocation invocation = null;
		
		public UiThreadMethodItem(MethodInvocation invocation, long delay) {
			this.invocation = invocation;
			this.delay = delay;
		}
	}

	@Override
	public Object perform(Object... params) {
		String methodName = (String) params[0];
		Object[] args = (Object[]) params[1];
		UiThreadMethodItem item = uiThreadMethods.get(methodName);
		if (item == null)
			throw new MobileException("'" + methodName + "'에 대응하는 메소드가 정의되지 않았습니다.");
		UiThreadWorker worker = new UiThreadWorker(item.invocation, args);
		runInUiThread(worker, item.delay);
		return null;
	}
	
	public void runInUiThread(Runnable run, long delayMillis) {
        if (delayMillis <= 0) {
            // 현재 쓰레드가 UI 쓰레드인 경우 그냥 run 실행
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                run.run();
                return;
            }

            uiHandler.post(run);
            return;
        }

        uiHandler.postDelayed(run, delayMillis);
    }
	
	public void invokeUiThread(Runnable runnable) {
		runInUiThread(runnable, 0);
	}
	
	private void fireException(MethodInvocation invocation, final Throwable t) {
		((Activity)invocation.getReceiver()).runOnUiThread(new Runnable() {
    		public void run() {
    			throw new MobileException(t);
    		}
    	});
	}
	
	private final class UiThreadWorker implements Runnable {
		private Object[] args;
		private MethodInvocation invocation;

		private UiThreadWorker(MethodInvocation invocation, Object ... args) {
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
