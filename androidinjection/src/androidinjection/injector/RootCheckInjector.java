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
 * 루팅 체크, ADB 연결 여부, 디버그 연결 여부, USB 연결 여부, AC 충전 연결 여부를 
 * 확인하여, 알림 다이얼로그를 보여준 후, 프로그램을 종료 한다.
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
		// 루팅 여부
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
	        	openPopupAndFinish(activity, "순정폰 이외의 폰에서는 사용하실 수 없습니다.", isKillProcess);
	        }
		}
		
		// 디버그 연결 여부
		if (isCheckDebug) {
			boolean isDebug = android.os.Debug.isDebuggerConnected();
			if (isDebug) {
				openPopupAndFinish(activity, "디버그 연결중에는 사용하실 수 없습니다.", isKillProcess);
			}
		}
        
		// ADB 연결 여부
		if (isCheckAdb) {
			int adb = Settings.Secure.getInt(activity.getContentResolver(),
					android.provider.Settings.Secure.ADB_ENABLED, 0);
			if (adb == 1) {
				openPopupAndFinish(activity, "ADB 연결중에는 사용하실 수 없습니다.\n설정 > 개발자 옵션 > USB 디버깅 기능을 꺼 주시기 바랍니다.", isKillProcess);
			}
		}
		
		// AC 충전 연결 여부
		if (isCheckAc) {
			Intent intent = activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		    int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		    if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
		    	openPopupAndFinish(activity, "충전중에는 사용하실 수 없습니다.", isKillProcess);
		    }
		}
		
		// USB 연결 여부
		if (isCheckUsb) {
			Intent intent = activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		    int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		    if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
		    	openPopupAndFinish(activity, "USB 연결중에는 사용하실 수 없습니다.", isKillProcess);
		    }
		}
	}
	
	public void openPopupAndFinish(Activity activity, String message, final boolean isKillProcess) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		// 취소키에 의한 종료처리 해제
		alert.setCancelable(false);
		alert.setTitle("알림");
		alert.setMessage(message);
		alert.setPositiveButton( "확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (isKillProcess) {
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
			});
		Dialog dialog = alert.show();
		
		// 배경 제거
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		// 취소키에 의한 종료처리 해제
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
