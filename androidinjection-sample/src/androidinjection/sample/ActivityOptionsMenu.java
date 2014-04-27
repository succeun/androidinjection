package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnOptionsItem;
import androidinjection.annotation.OptionsMenu;
import android.os.Bundle;
import android.view.MenuItem;
import androidinjection.sample.R;

@OptionsMenu(R.menu.menu_options_menu)
@EActivity(R.layout.activity_options_menu)
public class ActivityOptionsMenu extends AnnotationActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@OnOptionsItem(R.id.menu_dialog_dialer)
	public void onOptionsItemDialer(MenuItem item) {
		alert(item.toString() + "을 선택하였습니다.");
	}
	
	@OnOptionsItem(R.id.menu_dialog_email)
	public void onOptionsItemEmail() {
		alert("'이메일'을 선택하였습니다.");
	}
	
	@OnOptionsItem( {R.id.menu_dialog_map, R.id.menu_dialog_info} )
	public void onOptionsItemMapOrInfo(MenuItem item) {
		alert(item.toString() + "을 선택하였습니다.");
	}
}
