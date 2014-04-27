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
	 * ���ҽ� �̸��� ���� ���̵� ��ȯ�Ѵ�.
	 * @param context Context
	 * @param defType ���ҽ� Ÿ��(id, layout, strings, ...)
	 * @return
	 */
	public int getId(Context context, String defType) {
		String className = context.getClass().getName();
		return context.getResources().getIdentifier(className, defType, context.getPackageName());
	}
	
	public abstract Class<? extends Annotation> getTarget();
}
