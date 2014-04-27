package androidinjection.annotation;

import java.lang.annotation.Annotation;

import android.content.Context;
import android.util.Log;

public abstract class Injector {
	public String TAG = getClass().getSimpleName();
	
	public void debug(String message) {
		Log.d(TAG, "{" + getTarget().getSimpleName() + "} " + message);
	}
	
	/**
	 * 리소스 이름에 대한 아이디를 반환한다.
	 * @param context Context
	 * @param defType 리소스 타입(id, layout, strings, ...)
	 * @return
	 */
	public int getId(Context context, String defType) {
		String className = context.getClass().getName();
		return context.getResources().getIdentifier(className, defType, context.getPackageName());
	}
	
	public abstract Class<? extends Annotation> getTarget();
}
