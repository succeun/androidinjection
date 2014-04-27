package androidinjection.sample;

import androidinjection.annotation.AnnotationManager;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.ViewById;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidinjection.sample.R;

@EActivity(R.layout.activity_text_print)
public class ActivityTextPrint extends Activity {
	@ViewById(R.id.etName)
	private EditText etName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new AnnotationManager(this).inject();
	}
	
	@OnClick(R.id.btnAlert)
	public void onClickBtnAlert(View view) {
		String name = etName.getText().toString();
		alert(name);
	}
	
	public void alert(String message) {
		AlertDialog alert = new AlertDialog.Builder( this )
		.setTitle( "¾Ë¸²" )
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
