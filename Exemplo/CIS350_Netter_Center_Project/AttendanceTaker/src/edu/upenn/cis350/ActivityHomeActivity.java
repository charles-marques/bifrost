package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseQuery;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Represents home screen for when an activity is clicked;
 * gives the user a choice between editing the roster for
 * that activity and taking attendance for that activity
 */
public class ActivityHomeActivity extends Activity{
	
	private List<ActivityObject> listOfItems;
	private static final int RENAME_ACTIVITY = 0;
	private String activityName;
	private String className;
	private ArrayList<String> ids;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 setContentView(R.layout.activity_home);
		 TextView registration_name;
		 registration_name = (TextView) findViewById (R.id.titlething);
	     listOfItems = ParseHandler.getAllActivities();
		 super.onCreate(savedInstanceState);
	     Bundle extras = getIntent().getExtras();
	     activityName = extras.getString("name");
	     className = extras.getString("className"); 
	     registration_name.setText(activityName);
	     List<StudentObject> list = ParseHandler.getStudentsForActivity(className);
	     ids = new ArrayList<String>();
	     for(StudentObject s : list) {
	    	 ids.add(s.getStudentid());
	     }
	 }

	 public void onViewRosterClick(View view) {
        Intent i = new Intent(view.getContext(), StudentRosterActivity.class);
  	  	i.putExtra("name", activityName);
  	  	i.putExtra("className", className);
  	  	i.putStringArrayListExtra("ids", ids);
     	startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_StudentRoster);
	 }
	 
	 public void onTakeAttendanceClick(View view) {
        Intent i = new Intent(view.getContext(), EditRosterActivity.class);
  	  	i.putExtra("name", activityName);
  	  	i.putExtra("className", className);
  	  	i.putStringArrayListExtra("ids", ids);
     	startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_Roster);
	 }
	 
	 public void onRenameActivityClick (View view) {
		 showDialog(RENAME_ACTIVITY);
	 }
	 
	 protected Dialog onCreateDialog(int id) {
	    	if (id == RENAME_ACTIVITY) {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setMessage("Enter new activity name");
	            LinearLayout ll = new LinearLayout(this);
	            ll.setOrientation(1);
	            final EditText input = new EditText(this);
		        ll.addView(input);
		        builder.setView(ll);
		    	builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int id) {
		    			Editable activity = input.getText();
		    	        String activity_string = activity.toString();
		    	        if (activity_string.length() == 0) {
		    				Toast.makeText(getApplicationContext(), "A required field is blank",
		    						Toast.LENGTH_SHORT).show();
		    				        return;
		    			}
		    	        boolean activity_already_exists = false;
		    	        
		    	        // changed to a for each loop - Defect log
		    	        for(ActivityObject a : listOfItems) {
		    	        	if(a.getName().equals(activity_string)) {
		    	        		activity_already_exists = true;
		    	        		// added the break for the defect log to improve speed
		    	        		break;
		    	        	}
		    	        }
		    	        //changed the toast format - Defect Log       
		    	        if(activity_already_exists) {
		    	        	Toast.makeText(getApplicationContext(), "Activity already exists",
		    						Toast.LENGTH_SHORT).show();
		    	        }
		    	        else {
		    	        	Log.v(activity_string, "this is the new name");
		    	            ParseHandler.renameActivity(className, activity_string);
		    	            Toast.makeText(getApplicationContext(), "Activity name change successful, please click back and refresh to update the names",
		    						Toast.LENGTH_LONG).show();
//		    	        	ParseHandler.addActivity(activity_string);
		    	        	dialog.cancel();
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
	    	 return null; //removed else - defect log fix
	    }
		
	}
	 
