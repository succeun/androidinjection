package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnActivityResult;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.ViewById;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

@EActivity(R.layout.activity_intent)
public class ActivityIntent extends AnnotationActivity {
	@ViewById(R.id.etName)
	private EditText etName;
	
	@ViewById(R.id.etAge)
	private EditText etAge;
	
	public static final int EXAMPLE_REQUEST_CODE = 1000;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@OnClick(R.id.btnStartActivity)
	public void onClickStartActivity() {
		Intent intent = new Intent(this, ActivityIntentSubOne.class);
		intent.putExtra("name", etName.getText().toString());
		intent.putExtra("age", etAge.getText().toString());
		startActivity(intent);
	}
	
	@OnClick(R.id.btnResultActivity)
	public void onClickResultActivity() {
		Intent intent = new Intent(this, ActivityIntentSubTwo.class);
		intent.putExtra("name", etName.getText().toString());
		intent.putExtra("age", etAge.getText().toString());
		startActivityForResult(intent, EXAMPLE_REQUEST_CODE);
	}
	
	@OnClick(R.id.btnStartActivityInModel)
	public void onClickBtnStartActivityInModel() {
		Intent intent = new Intent(this, ActivityIntentSubThree.class);
		String xml = "<root>"
					+ "<name>"+etName.getText().toString()+"</name>"
					+ "<age>"+etAge.getText().toString()+"</age>"
					+ "</root>";
		
		intent.putExtra("xml", xml);
		startActivity(intent);
	}
	
	@OnActivityResult(EXAMPLE_REQUEST_CODE)
	public void onActivityResultExampleRequectCode(int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			alert("응답코드: " + resultCode + ", 회사: " + intent.getStringExtra("company"));
		}
	}
}
