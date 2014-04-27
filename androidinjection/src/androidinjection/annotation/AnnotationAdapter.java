package androidinjection.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AnnotationAdapter<T> extends ArrayAdapter<T> {
	private AnnotationActivity activity = null;
	private int viewResourceId; 
	private AnnotationManager annotationManager = null;
	private int dropDownViewResourceId;
	private LayoutInflater inflater;
	
	public AnnotationAdapter(AnnotationActivity activity, int viewResourceId, List<T> objects) {
		super(activity, viewResourceId, objects);
		this.activity = activity;
		this.viewResourceId = viewResourceId;
		this.dropDownViewResourceId = viewResourceId;
		this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		annotationManager = activity.getAnnotationManager();
		if (annotationManager == null)
			annotationManager = new AnnotationManager(activity);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(viewResourceId, parent, false);
		}	
		
		T item = getItem(position);
		if (item != null) {
			annotationManager.injectMethod(OnGetView.class, convertView, 
					new Object[]{position, convertView, parent, item});
			
			@SuppressWarnings("unchecked")
			Class<? extends Annotation>[] annotations = new Class[]{
					OnTouch.class, 
					OnClick.class, 
					OnLongClick.class,
					OnItemClick.class,
					OnItemLongClick.class,
					OnItemSelected.class,
					OnNothingSelected.class};
			annotationManager.injectMethods(annotations, convertView);
		}
		return convertView;
	}
	
	@Override
	public void setDropDownViewResource(int resource) {
		super.setDropDownViewResource(resource);
		this.dropDownViewResourceId = resource;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(dropDownViewResourceId, parent, false);
		}
		
		T item = getItem(position);
		if (item != null) {
			annotationManager.injectMethod(OnGetDropDownView.class, convertView, 
					new Object[]{position, convertView, parent, item});
			
			@SuppressWarnings("unchecked")
			Class<? extends Annotation>[] annotations = new Class[]{
					OnTouch.class, 
					OnClick.class, 
					OnLongClick.class,
					OnItemClick.class,
					OnItemLongClick.class,
					OnItemSelected.class,
					OnNothingSelected.class};
			annotationManager.injectMethods(annotations, convertView);
		}
		
		return convertView;
	}
}
