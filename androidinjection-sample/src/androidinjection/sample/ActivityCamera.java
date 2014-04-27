package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnActivityResult;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.ViewById;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidinjection.sample.R;

@EActivity(R.layout.activity_camera)
public class ActivityCamera extends AnnotationActivity {
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;

	@ViewById(R.id.ivCanvas)
	private ImageView ivCanvas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@OnClick(R.id.btnGet)
	public void onClickBtnGet(View view) {
		DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getImageFromPhoto();
			}
		};

		DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getImageFromAlbum();
			}
		};

		DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};

		new AlertDialog.Builder(this)
			.setTitle("가져올 매체 선택")
			.setPositiveButton("카메라", cameraListener)
			.setNeutralButton("앨범", albumListener)
			.setNegativeButton("취소", cancelListener)
			.show();
	}
	
	private void getImageFromPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, PICK_FROM_CAMERA);
	}
	
	private void getImageFromAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	@OnActivityResult(PICK_FROM_ALBUM)
	protected void onActivityResultPickFromAlbum(int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		ivCanvas.setImageURI(data.getData());
	}
	
	@OnActivityResult(PICK_FROM_CAMERA)
	protected void onActivityResultPickFromCamera(int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
		if( thumbnail != null )
			ivCanvas.setImageBitmap(thumbnail);
	}
}
