package androidinjection.injector;


import java.io.File;
import java.lang.annotation.Annotation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.BatteryManager;
import android.os.Environment;
import android.provider.Settings;
import androidinjection.annotation.ClassInjector;
import androidinjection.annotation.ClassInvocation;
import androidinjection.annotation.RootCheck;


/**
 * ���� üũ, ADB ���� ����, ����� ���� ����, USB ���� ����, AC ���� ���� ���θ� 
 * Ȯ���Ͽ�, �˸� ���̾�α׸� ������ ��, ���α׷��� ���� �Ѵ�.
 */
public class RootCheckInjector extends ClassInjector {
	public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "";
    public static final String ROOTING_PATH_1 = "/system/bin/su";
    public static final String ROOTING_PATH_2 = "/system/xbin/su";
    public static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
    public static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";
     
    public String[] rootFilesPath = new String[]{
            ROOT_PATH + ROOTING_PATH_1 ,
            ROOT_PATH + ROOTING_PATH_2 , 
            ROOT_PATH + ROOTING_PATH_3 , 
            ROOT_PATH + ROOTING_PATH_4
    };

	@Override
	public Class<? extends Annotation> getTarget() {
		return RootCheck.class;
	}

	@Override
	public void inject(Annotation annotation, ClassInvocation invocation, Object ... params) {
		Activity activity = (Activity) invocation.getReceiver();
		boolean isCheckRoot = ((RootCheck)annotation).root();
		boolean isCheckDebug = ((RootCheck)annotation).debug();
		boolean isCheckAdb = ((RootCheck)annotation).adb();
		boolean isCheckAc = ((RootCheck)annotation).ac();
		boolean isCheckUsb = ((RootCheck)annotation).usb();
		boolean isKillProcess = ((RootCheck)annotation).killProcess();
		
		checkRoot(activity, isCheckRoot, isCheckDebug, isCheckAdb, isCheckAc, isCheckUsb, isKillProcess);
         
		debug(invocation.toString());
	}

	private void checkRoot(Activity activity, boolean isCheckRoot,
			boolean isCheckDebug, boolean isCheckAdb, boolean isCheckAc,
			boolean isCheckUsb, final boolean isKillProcess) {
		// ���� ����
		if (isCheckRoot) {
			boolean isRootingFlag = false;
			 
	        try {
	        	Runtime.getRuntime().exec("su");
	            isRootingFlag = true;
	        } catch (Exception e) {
	            isRootingFlag = false;
	        }
	         
	        if(!isRootingFlag){
	            isRootingFlag = checkRootingFiles(rootFilesPath);
	        }
			
	        if (isRootingFlag) {
	        	openPopupAndFinish(activity, "������ �̿��� �������� ����Ͻ� �� �����ϴ�.", isKillProcess);
	        }
		}
		
		// ����� ���� ����
		if (isCheckDebug) {
			boolean isDebug = android.os.Debug.isDebuggerConnected();
			if (isDebug) {
				openPopupAndFinish(activity, "����� �����߿��� ����Ͻ� �� �����ϴ�.", isKillProcess);
			}
		}
        
		// ADB ���� ����
		if (isCheckAdb) {
			int adb = Settings.Secure.getInt(activity.getContentResolver(),
					android.provider.Settings.Secure.ADB_ENABLED, 0);
			if (adb == 1) {
				openPopupAndFinish(activity, "ADB �����߿��� ����Ͻ� �� �����ϴ�.\n���� > ������ �ɼ� > USB ����� ����� �� �ֽñ� �ٶ��ϴ�.", isKillProcess);
			}
		}
		
		// AC ���� ���� ����
		if (isCheckAc) {
			Intent intent = activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		    int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		    if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
		    	openPopupAndFinish(activity, "�����߿��� ����Ͻ� �� �����ϴ�.", isKillProcess);
		    }
		}
		
		// USB ���� ����
		if (isCheckUsb) {
			Intent intent = activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		    int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		    if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
		    	openPopupAndFinish(activity, "USB �����߿��� ����Ͻ� �� �����ϴ�.", isKillProcess);
		    }
		}
	}
	
	public void openPopupAndFinish(Activity activity, String message, final boolean isKillProcess) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		// ���Ű�� ���� ����ó�� ����
		alert.setCancelable(false);
		alert.setTitle("�˸�");
		alert.setMessage(message);
		alert.setPositiveButton( "Ȯ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (isKillProcess) {
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
			});
		Dialog dialog = alert.show();
		
		// ��� ����
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		// ���Ű�� ���� ����ó�� ����
		dialog.setCancelable(false);
	}
	
	@Override
	public Object perform(Object ... params) {
		return null;
	}
     
    private boolean checkRootingFiles(String ... files){
    	File[] rootingFiles = new File[files.length];
        for(int i=0 ; i < files.length; i++){
            rootingFiles[i] = new File(files[i]);
        }
        
        boolean result = false;
        for(File f : rootingFiles){
            if(f != null && f.exists() && f.isFile()){
                result = true;
                break;
            }else{
                result = false;
            }
        }
        return result;
    }
}
