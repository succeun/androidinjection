package androidinjection.sample;

import androidinjection.annotation.AnnotationActivity;
import androidinjection.annotation.AnnotationAdapter;
import androidinjection.annotation.EActivity;
import androidinjection.annotation.OnGetView;
import androidinjection.annotation.OnItemClick;
import androidinjection.annotation.ViewById;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidinjection.sample.R;

@EActivity(R.layout.activity_listview)
public class ActivityListView extends AnnotationActivity {
	@ViewById(R.id.lvMember)
	private ListView lvMember;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		List<Member> members = new ArrayList<Member>();
	    members.add(new Member(1, "Kim", 34));
	    members.add(new Member(2, "Lee", 26));
	    members.add(new Member(3, "Eun", 39));
	    members.add(new Member(4, "Park", 34));
	    members.add(new Member(5, "Shim", 26));
	    members.add(new Member(6, "Kang", 39));
	    members.add(new Member(7, "Ahn", 34));
	    members.add(new Member(8, "Jo", 26));
	    members.add(new Member(9, "Jung", 39));
	    members.add(new Member(10, "Kim", 34));
	    members.add(new Member(11, "Lee", 26));
	    members.add(new Member(12, "Eun", 39));
	    members.add(new Member(13, "Park", 34));
	    members.add(new Member(14, "Shim", 26));
	    members.add(new Member(15, "Kang", 39));
	    members.add(new Member(16, "Ahn", 34));
	    members.add(new Member(17, "Jo", 26));
	    members.add(new Member(18, "Jung", 39));
	    
	    AnnotationAdapter<Member> adapter = new AnnotationAdapter<Member>(this, 
	    		R.layout.cell_member, members);
	    lvMember.setAdapter(adapter);
	}
	
	@OnGetView(R.id.lvMember)
	//public void setView(int position, View convertView, ViewGroup parent, Member member) {
	public void setView(View convertView, Member member) {
		TextView tvNo = (TextView) convertView.findViewById(R.id.member_row_no);
		tvNo.setText(member.getNo() + "");
		
		TextView tvName = (TextView) convertView.findViewById(R.id.member_row_name);
		tvName.setText(member.getName());
		
		TextView tvAge = (TextView) convertView.findViewById(R.id.member_row_age);
		tvAge.setText(member.getAge() + "");
	}
	
	@OnItemClick(R.id.lvMember)
	public void onItemClick(int postion, Member member) {
		alert("[" + postion + "] " + member.toString());
	}
}
