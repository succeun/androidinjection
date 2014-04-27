package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.ResString;
import androidinjection.annotation.SystemService;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;
import androidinjection.sample.R;

@EActivity(R.layout.activity_system_service)
public class ActivitySystemService extends AnnotationActivity {
	@ResString(R.string.app_name)
	private String appName;
	
	@SystemService
	private Vibrator vibrator;
	
	@SystemService
	private WifiManager wifi;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@OnClick(R.id.btnPrint)
	public void onClickBtnAlert(View view) {
		alert(appName);
	}
	
	@OnClick(R.id.btnVibrator)
	public void onClickBtnVibrator(View view) {
		// android.permission.VIBRATE 권한 필요
		vibrator.vibrate(1000);
		Toast.makeText(this, "Vibrating...", Toast.LENGTH_SHORT).show();
	}
	
	@OnClick(R.id.btnWifiIP)
	public void onClickBtnWifiIP() {
		WifiInfo wifiInfo = wifi.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = String.format("%d.%d.%d.%d", 
				(ipAddress & 0xff),	(ipAddress >> 8 & 0xff), 
				(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
		alert("현재 IP는 " + ip + "입니다.");
	}
	
	public void alert(String message) {
		AlertDialog alert = new AlertDialog.Builder( this )
		.setTitle( "알림" )
		.setMessage( message )
		.setPositiveButton( "OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		})
		.show();
	}
}
