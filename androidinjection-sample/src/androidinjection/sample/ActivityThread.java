package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.Background;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.UiThread;
import androidinjection.annotation.ViewById;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import androidinjection.sample.R;

@EActivity(R.layout.activity_thread)
public class ActivityThread extends AnnotationActivity {
	@ViewById(R.id.pbJob)
	private ProgressBar pbJob; 
	
	private boolean isCancel = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@OnClick(R.id.btnRunThread)
	public void onClickBtnRunThread(Button button) {
		isCancel = false;
		int max = 100;
		pbJob.setProgress(0);	// 초기값 설정
		pbJob.setMax(max);		// 최대값 설정
		invokeBackground("runThread", max);
	}
	
	@Background
	public void runThread(int max) {
		for (int i = 0; !isCancel && i <= max ; i++) {
			invokeUiThread("updateProbarJob", 1);
			Log.d("example", "Count: " + i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//
			}
		}
	}
	
	@UiThread
	public void updateProbarJob(int diff) {
		pbJob.incrementProgressBy(diff);
	}
	
	@OnClick(R.id.btnCancel)
	public void onClickBtnCancel() {
		isCancel = true;
		toast("취소 되었습니다.");
	}
}
