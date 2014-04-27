package androidinjection.injector;


import java.lang.annotation.Annotation;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import androidinjection.annotation.FieldInjector;
import androidinjection.annotation.FieldInvocation;
import androidinjection.annotation.SystemService;


public class SystemServiceInjector extends FieldInjector {

	@Override
	public Class<? extends Annotation> getTarget() {
		return SystemService.class;
	}

	@Override
	public void inject(Annotation annotation, FieldInvocation invocation, Object ... params) {
		Object service = null;
		Activity activity = (Activity) invocation.getReceiver();
        Class<?> fieldClass = invocation.getField().getType();
        if (fieldClass == WindowManager.class) {
        	service = activity.getSystemService(Context.WINDOW_SERVICE);
        } else if (fieldClass == LayoutInflater.class) {
        	service = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        } else if (fieldClass == ActivityManager.class) {
        	service = activity.getSystemService(Context.ACTIVITY_SERVICE);
        } else if (fieldClass == PowerManager.class) {
        	service = activity.getSystemService(Context.POWER_SERVICE);
        } else if (fieldClass == AlarmManager.class) {
        	service = activity.getSystemService(Context.ALARM_SERVICE);
        } else if (fieldClass == NotificationManager.class) {
        	service = activity.getSystemService(Context.NOTIFICATION_SERVICE);
        } else if (fieldClass == KeyguardManager.class) {
        	service = activity.getSystemService(Context.KEYGUARD_SERVICE);
        } else if(fieldClass == LocationManager.class) {
        	service = activity.getSystemService(Context.LOCATION_SERVICE);
        } else if (fieldClass == SearchManager.class) {
        	service = activity.getSystemService(Context.SEARCH_SERVICE);
        } else if (fieldClass == Vibrator.class) {
        	service = activity.getSystemService(Context.VIBRATOR_SERVICE);
        } else if (fieldClass == ConnectivityManager.class) {
        	service = activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        } else if (fieldClass == WifiManager.class) {
        	service = activity.getSystemService(Context.WIFI_SERVICE);
        } else if (fieldClass == InputMethodManager.class) {
        	service = activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        } else if (fieldClass == UiModeManager .class) {
        	service = activity.getSystemService(Context.UI_MODE_SERVICE);
        } else if (fieldClass == ClipboardManager.class) {
        	service = activity.getSystemService(Context.CLIPBOARD_SERVICE);
        } else if (fieldClass == AccountManager.class) {
        	service = activity.getSystemService(Context.ACCOUNT_SERVICE);
        } else if (fieldClass == AudioManager.class) {
        	service = activity.getSystemService(Context.AUDIO_SERVICE);
        } else if (fieldClass == DevicePolicyManager.class) {
        	service = activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        } else if (fieldClass == DropBoxManager.class) {
        	service = activity.getSystemService(Context.DROPBOX_SERVICE);
        } else if (fieldClass == TelephonyManager.class) {
        	service = activity.getSystemService(Context.TELEPHONY_SERVICE);
        } else if (fieldClass == WallpaperManager.class) {
        	service = activity.getSystemService(Context.WALLPAPER_SERVICE);
        } else if (Activity.class.isAssignableFrom(fieldClass)) {
        	service = activity;
        }
        
        if (service != null) {
        	invocation.setValue(service);
        }
         
        debug(invocation.toString());
	}
	
}
