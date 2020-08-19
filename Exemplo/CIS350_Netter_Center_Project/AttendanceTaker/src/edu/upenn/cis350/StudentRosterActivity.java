package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.parse.Parse;

/**
 * Roster of students for a particular activity
 * and buttons to give the user options to add, edit,
 * or delete students from the roster.
 */
public class StudentRosterActivity extends Activity{
	
	private List<StudentObject> listOfItems;
	private ArrayList<String> ids;
	private String activity;
	private String roster_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_roster);
        Parse.initialize(this, "cuoXWbqvBKs8SUrhnyKdyNWiMPZxuDBZ31ehltVI", "tl8VMcFHu7u3haym9KSbRKEP61MmxPDvmL06dxeo"); 
        Bundle extras = getIntent().getExtras();
        activity = extras.getString("className");
        roster_title = extras.getString("name");
        TextView activity_name;
        activity_name = (TextView) findViewById (R.id.title_stud);
	    activity_name.setText(roster_title + " " + "Roster");
        createList();        
    }
    
    public void onAddStudentClick(View view) {
    	Intent i = new Intent(this, AddStudentActivity.class);
    	ids = new ArrayList<String>();
    	for(StudentObject s : listOfItems) {
    		ids.add(s.getStudentid());
    	}
    	i.putStringArrayListExtra("ids", ids);
    	i.putExtra("activity", activity);
    	i.putExtra("activity_name", roster_title);

    	startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_AddStudent);
    }
    
    public void createList() {
    	listOfItems = ParseHandler.getStudentsForActivity(activity);
        ListView list = (ListView) findViewById(R.id.StudentList);
        
        //handles null case
  		if(list == null){
  			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			list = (ListView) inflater.inflate(R.id.RosterList, null);
  		}
        
        list.setClickable(true);
    	AddStudentAdapter adapter = new AddStudentAdapter(this, listOfItems);
        
        //click listener for each list item
        list.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
        	  // get info for the student to be displayed
        	  StudentObject student = listOfItems.get(position);
        	  String name = student.getName();
        	  String idNumber = student.getStudentid();
        	  int grade = student.getGradeLevel();
        	  String phone = student.getPhone();
        	  String address = student.getAddress();
        	  String school = student.getSchool();
        	  String site = student.getSite();
        	  String contactName = student.getContactName();
        	  String contactType = student.getContactType();
        	  
        	  Intent i = new Intent(view.getContext(), ProfileActivity.class);
        	  
        	  // put info for the student in intent
        	  // defect log change "Name" -> "name"
        	  i.putExtra("name", name);
        	  i.putExtra("idNumber", idNumber);
        	  i.putExtra("grade", grade+"");
        	  i.putExtra("phone", phone);
        	  i.putExtra("address", address);
        	  i.putExtra("school", school);
        	  i.putExtra("site", site);
        	  i.putExtra("contactName", contactName);
        	  i.putExtra("contactType", contactType);
        	  startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_ProfileActivity);
          }
        });
        list.setAdapter(adapter);       
    }
    
    public void onRemoveStudentClick(View view) {
    	Intent i = new Intent(this, RemoveStudentActivity.class);
    	ids = new ArrayList<String>();
    	for(StudentObject s : listOfItems) {
    		ids.add(s.getStudentid());
    	}
    	i.putStringArrayListExtra("ids", ids);
    	i.putExtra("activity_name", roster_title);
    	i.putExtra("activity", activity);
    	startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_RemoveStudent);
    }
    
    public void onRefreshClick(View view) {
    	createList();
    }

}