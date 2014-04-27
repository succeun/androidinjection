package androidinjection.annotation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidinjection.IntentBuilder;


public class AnnotationActivity extends Activity {
	private AnnotationManager annotationManager = null;
	
	public AnnotationManager getAnnotationManager() {
		return annotationManager;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        annotationManager = new AnnotationManager(this);
        annotationManager.inject();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		return annotationManager.onCreateOptionsMenu(this, menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		return annotationManager.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		annotationManager.onActivityResult(requestCode, resultCode, intent);
	}
	
	public void invokeBackground(String methodName, Object ... args) {
		annotationManager.invokeBackground(methodName, args);
	}
	
	public void invokeBackground(Runnable runnable) {
		annotationManager.invokeBackground(runnable);
	}
	
	public void invokeUiThread(String methodName, Object ... args) {
		annotationManager.invokeUiThread(methodName, args);
	}
	
	public void invokeUiThread(Runnable runnable) {
		annotationManager.invokeUiThread(runnable);
	}
	
	public void toast(String message) {
		toast(message, Toast.LENGTH_SHORT);
	}
	
	public void toastLong(String message) {
		toast(message, Toast.LENGTH_LONG);
	}
	
	public void toast(final String message, final int duration) {
		Runnable showAlert = new Runnable() {
			public void run() {
				Toast.makeText(AnnotationActivity.this, message, duration).show();
			}
		};

		this.runOnUiThread(showAlert);
	}
	
	public void start(Class<?> cls) {
		new IntentBuilder(this, cls).startActivity();
	}
	
	public void startAndFinish(Class<?> cls) {
		start(cls);
		finish();
	}
	
	public void alert(final String message) {
		alert(message, null);
	}
	
	public void alert(final String message, final String title) {
		Runnable showAlert = new Runnable() {
			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(AnnotationActivity.this);
				if (title != null && title.length() > 0) {
					alert.setTitle(title);
				}
				alert.setMessage(message);
				alert.setPositiveButton( "OK", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					});
				alert.show();
			}
		};

		this.runOnUiThread(showAlert);
	}
}
