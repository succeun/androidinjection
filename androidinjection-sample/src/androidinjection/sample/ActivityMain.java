package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.ExceptionReporter;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.OnOptionsItem;
import androidinjection.annotation.OptionsMenu;
import androidinjection.annotation.RootCheck;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

@RootCheck(killProcess=false)
@ExceptionReporter
@OptionsMenu(R.menu.activity_main)
@EActivity(R.layout.activity_main)
public class ActivityMain extends AnnotationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@OnClick(R.id.btnRun1)
	public void onClick1() {
		run(ActivityTextPrint.class);
	}
	
	@OnClick(R.id.btnRun2)
	public void onClick2() {
		run(ActivitySystemService.class);
	}
	
	@OnClick(R.id.btnRun3)
	public void onClick3() {
		run(ActivityNoTitleFullScreen.class);
	}
	
	@OnClick(R.id.btnRun4)
	public void onClick4() {
		run(ActivityOptionsMenu.class);
	}
	
	@OnClick(R.id.btnRun5)
	public void onClick5() {
		run(ActivityThread.class);
	}
	
	@OnClick(R.id.btnRun6)
	public void onClick6() {
		run(ActivityIntent.class);
	}
	
	@OnClick(R.id.btnRun7)
	public void onClick7() {
		run(ActivityListView.class);
	}
	
	@OnClick(R.id.btnRun9)
	public void onClick9() {
		run(ActivityOpenApi.class);
	}
	
	@OnClick(R.id.btnRun10)
	public void onClick10() {
		run(ActivityCamera.class);
	}
	
	private void run(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		startActivity(intent);
	}

	@OnOptionsItem(R.id.menu_settings)
	public void onOptionsItem(MenuItem item) {
		alert(item.toString());
	}

}
