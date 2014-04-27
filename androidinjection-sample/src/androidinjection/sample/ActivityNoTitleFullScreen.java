package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.FullScreen;
import androidinjection.annotation.NoTitle;
import androidinjection.annotation.OnTouch;
import androidinjection.annotation.ViewById;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidinjection.sample.R;

@NoTitle
@FullScreen
@EActivity(R.layout.activity_notitle_fullscreen)
public class ActivityNoTitleFullScreen extends AnnotationActivity {
	@ViewById(R.id.tvTouch)
	private TextView tvTouch;
	
	private int colorIndex = 0;
	
	private int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.YELLOW};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@OnTouch(R.id.tvTouch)
	public void onClickBtnAlert(MotionEvent event) {
		int index = colorIndex++ % colors.length;
		((LinearLayout)tvTouch.getParent()).setBackgroundColor(colors[index]);
		String msg = "XÁÂÇ¥: " + (int)event.getX() + ", YÁÂÇ¥: " + (int)event.getY();
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
