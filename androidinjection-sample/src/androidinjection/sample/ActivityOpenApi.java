package androidinjection.sample;

import androidinjection.MobileException;
import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.Background;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnClick;
import androidinjection.annotation.OnGetView;
import androidinjection.annotation.UiThread;
import androidinjection.annotation.ViewById;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_open_api)
public class ActivityOpenApi extends AnnotationActivity {
	@ViewById(R.id.lvNews)
	private ListView lvNews;
	
	@ViewById(R.id.etWord)
	private EditText etWord;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		etWord.setText("현대해상");
	}
	
	@OnClick(R.id.btnSearch)
	public void onClickBtnSearch() {
		invokeBackground("getRealSearchWord", etWord.getText().toString());
	}
	
	@Background
	public void getRealSearchWord(String word) {
		try {
			// 1. 질의 URL
			String openapiKey = "a716f06649cc8bb5ac93c1acc054d031";
			String queryURL = "http://openapi.naver.com/search?key=" + openapiKey
					+ "&query="+ URLEncoder.encode(word, "utf-8")+"&target=news&start=1&display=10";
	
			// 2. 질의요청
			URL url = new URL(queryURL);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
	
			// 3. 질의결과 XML에 대한 접근 처리
//			model = new Model(this, is);
//			invokeUiThread("setListView", model);
		} catch (FileNotFoundException e) {
			// HTTP_BAD_REQUEST 400
			alert("통신중 에러가 발생하였습니다.\n다시 시도하시기 바랍니다.");
		} catch (IOException e) {
			throw new MobileException(e);
		}
	}
	
//	@UiThread
//	public void setListView(Model model) {
//		lvNews.setAdapter(new AnnotationModelAdapter(this, 
//				R.layout.cell_news, 
//				model, 
//				"/rss/channel/item"));
//	}
//	
//	@OnGetView(R.id.lvNews)
//	public void onGetView(int position, View convertView, ViewGroup parent, Model sub) {
//		// XML 구조 예제
//		// http://openapi.naver.com/search?key=a716f06649cc8bb5ac93c1acc054d031&query=%EC%A3%BC%EC%8B%9D&target=news&start=1&display=10
//		System.out.println(sub.toString());
//		TextView textView = (TextView) convertView.findViewById(R.id.tvTitle);
//		textView.setText(Html.fromHtml(sub.getValue("/item/title")));	// 키워드
//		
//		TextView textView1 = (TextView) convertView.findViewById(R.id.tvDescription);
//		textView1.setText(Html.fromHtml(sub.getValue("/item/description")));
//		
//		TextView textView2 = (TextView) convertView.findViewById(R.id.tvPubDate);
//		textView2.setText(sub.getValue("/item/pubDate"));
//		
//		Button button = (Button) convertView.findViewById(R.id.btnMove);
//		button.setTag(sub.getValue("/item/link"));
//	}
	
	@OnClick(R.id.btnMove)
	public void onClickRowButton(View v) {
		String url = (String) v.getTag();
		if (url != null && url.length() > 0) {
			Intent i = new Intent(Intent.ACTION_VIEW); 
			Uri u = Uri.parse(url); 
			i.setData(u); 
			startActivity(i);
		}
	}
}
