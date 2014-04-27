Andriod Injection
=========


이 프로젝트는 AndroidAnnotation에서 아이디어를 얻었으며,
그 프로젝트의 Annotation을 닮으러 노력하였습니다.
유사 프로젝트로 AndroidAnnotation, butterknife가 있으며,
차이점으로 Annotation processing이 아닌 Reflection을 
사용하고자 합니다.

이유인즉, 운영에서 소스 생성을 허락하지 않는 회사에서
사용하고자 소스 변경이 발생하지 않도록 Reflection을 사용하여
동일한 동작을 하고자 합니다.

Code Sample
-------

```java
import androidinjection.annotation.AnnotationActivity;

@EActivity(R.layout.translate)
public class TranslateActivity extends AnnotationActivity {

    @ViewById(R.id.textInput) // Injects
    EditText textInput;

    @ViewById(R.id.myTextView) // Injects
    TextView result;

    @ResAnimation(android.R.anim.fade_in) // Injects 
    Animation fadeIn;

    @Click(R.id.doTranslate) // When button is clicked 
    void doTranslate() {
         invokeBackground("translateInBackground", textInput.getText().toString());
    }

    @Background // Executed in a background thread
    void translateInBackground(String textToTranslate) {
         String translatedText = callGoogleTranslate(textToTranslate);
         invokeUiThread("showResult", translatedText);
    }

    @UiThread // Executed in the ui thread
    void showResult(String translatedText) {
         result.setText(translatedText);
         result.startAnimation(fadeIn);
    }

    // [...]
}
```

Version
--------

0.1



