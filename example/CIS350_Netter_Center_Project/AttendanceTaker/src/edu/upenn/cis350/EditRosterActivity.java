package edu.upenn.cis350;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Represents the roster in which the user can take attendance
 * for a particular activity.
 *
 */
public class EditRosterActivity extends Activity{
	
	//changed variable name to list of students
	private List<StudentObject> listOfStudents;
	private static final int ADD_STUDENT = 0;
	private String activityName;
	private String className;
	private ActivityObject currentActivity;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roster);
   
        Bundle extras = getIntent().getExtras();
        activityName = extras.getString("name");
        className = extras.getString("className");
        TextView activity_name;
        activity_name = (TextView) findViewById (R.id.roster_title);
	    activity_name.setText(activityName + " " + "Students");
        currentActivity = ParseHandler.getActivityByName(className);
        createRoster(); 
    }
    
    public void createRoster() {
    	listOfStudents = populateRoster();
    	
        ListView list = (ListView) findViewById(R.id.RosterList);
        
        //handles null case
  		if(list == null){
  			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			list = (ListView) inflater.inflate(R.id.RosterList, null);
  		}
        
        list.setClickable(true);

        RosterAdapter adapter = new RosterAdapter(this, listOfStudents);
        
        //click listener for each list item
        list.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
        	  // get info for the student to be displayed
        	  StudentObject student = listOfStudents.get(position);
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
    
    /** creates and populates roster **/
	public List<StudentObject> populateRoster(){
		List<StudentObject> actlist = ParseHandler.getStudentsForActivity(className);
		Collections.sort(actlist, new Comparator<StudentObject>(){
	   	     public int compare(StudentObject o1, StudentObject o2){
	   	         return o1.getName().compareToIgnoreCase(o2.getName());
	   	     }
	   	});
		return actlist;
	}
    
	/** handles click for Back button **/
	public void onSubmitButtonClick(View view) {
		if(listOfStudents == null){
			Toast.makeText(getApplicationContext(), "No Students in Roster",
	                Toast.LENGTH_SHORT).show();
		}
		else{
			String output = "Attendance Submitted!";
			
			Toast.makeText(getApplicationContext(), output,
	                Toast.LENGTH_SHORT).show();
			ParseHandler.submitActivityAttendance(currentActivity, listOfStudents);
		}
	}
	
    @Override
	protected Dialog onCreateDialog(int id) {
    	if (id == ADD_STUDENT) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enter student name");
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(1); // suggestion was to change 1 to vertical, not sure if the enum is accepted
            // Edit text is final - Defect Log changes
            final EditText input = new EditText(this);
	        ll.addView(input);
	        builder.setView(ll);
	    	builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			Editable activity = input.getText();
	    	        String activity_string = activity.toString();
	    	        boolean activity_already_exists = false;
	    	        
	    	        // changed to a for each lopp and added the break statement
	    	        for(StudentObject s : listOfStudents) {
	    	        	if(s.getName().equals(activity_string)) {
	    	        		activity_already_exists = true;
	    	        		break;
	    	        	}
	    	        }
	    	        
	    	        // changed the instantiation of the toast
	    	        
	    	        if(activity_already_exists) {
	    	        	Toast.makeText(getApplicationContext(), "Activity already exists",
	    						Toast.LENGTH_SHORT).show();
	    	        }
	    	        
	    	        else {
	    	        	ParseHandler.addStudent(new StudentObject(activity_string));
	    	        	dialog.cancel();
	    	        	createRoster();
	    	        }
	    	    }
	    	 });
	    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			dialog.cancel();
	    		}
	    	});
	    	return builder.create();
    	}
    	else return null;
    }
}
