package edu.upenn.cis350;

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
 * Screen where user can remove a student from
 * a particular activity's roster.
 */
public class RemoveStudentActivity extends Activity{
	
	private List<StudentObject> listOfItems;
	private static final int REMOVE = 0;
	private StudentObject student;
	private String activity;
	private String classname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);        
        Parse.initialize(this, "cuoXWbqvBKs8SUrhnyKdyNWiMPZxuDBZ31ehltVI", "tl8VMcFHu7u3haym9KSbRKEP61MmxPDvmL06dxeo"); 
        Bundle extras = getIntent().getExtras();
        activity = extras.getString("activity_name");
        classname = extras.getString("activity");
        TextView activity_name;
        activity_name = (TextView) findViewById (R.id.title_thinger);
	    activity_name.setText(activity + " " + "Roster");
        createList();        
    }
    
    public void createList() {
    	listOfItems = ParseHandler.getStudentsForActivity(classname);
    	AddStudentAdapter adapter = new AddStudentAdapter(this, listOfItems);
        ListView list = (ListView) findViewById(R.id.StudentList);
        list.setClickable(true);
        //click listener for each list item - on click, goes to Roster page
        list.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {  
        	student = listOfItems.get(position);
        	showDialog(REMOVE);
        	}
        });
        
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    
    @Override
	protected Dialog onCreateDialog(int id) {
    	if (id == REMOVE) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this student?");
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(1);
	        builder.setView(ll);
	    	builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			Double studentID = Double.parseDouble(student.getStudentid());
	    			ParseHandler.removeStudentFromActivity(studentID, activity);
	    			dialog.cancel();
	        		Toast.makeText(getApplicationContext(), "Student successfully removed.",
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