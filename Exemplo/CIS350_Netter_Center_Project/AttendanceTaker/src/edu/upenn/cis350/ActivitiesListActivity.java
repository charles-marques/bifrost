package edu.upenn.cis350;

import java.util.List;

import com.parse.Parse;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ActivitiesListActivity extends Activity {

	private List<ActivityObject> listOfItems;
	private static final int ADD_ACTIVITY = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_list);        
        Parse.initialize(this, "cuoXWbqvBKs8SUrhnyKdyNWiMPZxuDBZ31ehltVI",
        		"tl8VMcFHu7u3haym9KSbRKEP61MmxPDvmL06dxeo");         
        createList();        
    }
    
    public void createList() {
    	listOfItems = ParseHandler.getAllActivities();
    	
        ActivitiesAdapter adapter = new ActivitiesAdapter(this, listOfItems);
        
        ListView list = (ListView) findViewById(R.id.ActivitiesList);
        list.setClickable(true);
        //click listener for each list item - on click, goes to Roster page
        list.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {  
        	String name = listOfItems.get(position).getName();
        	String className = listOfItems.get(position).getClassName();

			Log.v("activity:", className);
        	ParseHandler.increaseActivityCount(className);
            Intent i = new Intent(view.getContext(), ActivityHomeActivity.class);
      	  	i.putExtra("name", name);
      	  	i.putExtra("className", className);
        	startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_ActivityHome);
          }
        });
        
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onAddActivityClick(View view){
    	showDialog(ADD_ACTIVITY);
    }
    
    public void onRefreshClick(View view) {
    	createList();
    }
    
    @Override
	protected Dialog onCreateDialog(int id) {
    	if (id == ADD_ACTIVITY) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enter activity name");
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(1);
            final EditText input = new EditText(this);
	        ll.addView(input);
	        builder.setView(ll);
	    	builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
	    	        	ParseHandler.addActivity(activity_string);
	    	        	dialog.cancel();
	    	        	createList();
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