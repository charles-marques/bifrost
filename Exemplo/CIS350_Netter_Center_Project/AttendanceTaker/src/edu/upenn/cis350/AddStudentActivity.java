package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.parse.Parse;

/**
 * Where user can add a student to the roster of
 * students for a particular activity in the database.
 *
 */
public class AddStudentActivity extends Activity{
	
	private List<StudentObject> listOfItems;
	private static final int ADD = 0;
	private ArrayList<String> ids;
	private StudentObject student;
	private String activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);        
        Parse.initialize(this, "cuoXWbqvBKs8SUrhnyKdyNWiMPZxuDBZ31ehltVI", "tl8VMcFHu7u3haym9KSbRKEP61MmxPDvmL06dxeo"); 
        Bundle extras = getIntent().getExtras();
        ids = extras.getStringArrayList("ids");
        activity = extras.getString("activity_name");
        TextView activity_name;
        activity_name = (TextView) findViewById (R.id.title_thinger);
	    activity_name.setText(activity + " " + "Roster");
        createList();        
    }
    
    public void createList() {
    	listOfItems = ParseHandler.getAllStudents();
    	AddStudentAdapter adapter = new AddStudentAdapter(this, listOfItems);
        ListView list = (ListView) findViewById(R.id.StudentList);
        list.setClickable(true);
        //click listener for each list item - on click, goes to Roster page
        list.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {  
        	StudentObject currentStudent = listOfItems.get(position);
        	boolean already_in_roster = false;
        	for (String ID : ids) {
        		if(currentStudent.getStudentid().equals(ID))
        			already_in_roster = true;
        	}
        	if(already_in_roster) {
        		Toast.makeText(getApplicationContext(), "That student is already part of the activity",
    	                Toast.LENGTH_SHORT).show();
        	}
        	else {
        		student = currentStudent;
        		showDialog(ADD);
        	}
          }
        });
        
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    
    @Override
	protected Dialog onCreateDialog(int id) {
    	if (id == ADD) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to add this student?");
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(1);
	        builder.setView(ll);
	    	builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			Double studentID = Double.parseDouble(student.getStudentid());
	    			ParseHandler.addStudentToActivity(studentID, activity);
	    			dialog.cancel();
	        		Toast.makeText(getApplicationContext(), "Student successfully added.",
	    	                Toast.LENGTH_SHORT).show();
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


