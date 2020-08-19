package edu.upenn.cis350;

import com.parse.Parse;
import com.parse.ParseUser;

import edu.upenn.cis350.R;
import edu.upenn.cis350.R.id;
import edu.upenn.cis350.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AttendanceTakerActivity extends Activity {

    private static final int LOGIN_DIALOG = 0;
    public static final int ACTIVITY_Attendance = 1;
    public static final int ACTIVITY_Roster = 2;
    public static final int ACTIVITY_AddActivity = 3;
    public static final int ACTIVITY_ActivitiesList = 4;
    public static final int ACTIVITY_EditActivities = 5;
    public static final int ACTIVITY_ProfileActivity = 6;
    public static final int ACTIVITY_RegistrationActivity = 7;
    public static final int ACTIVITY_AddStudent = 8;
    public static final int ACTIVITY_ActivityHome = 9;
    public static final int ACTIVITY_StudentRoster = 10;
    public static final int ACTIVITY_RemoveStudent = 11;
	public static final int ACTIVITY_EditProfile = 12;
	public static final int ACTIVITY_RemoveActivity = 13;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_taker);
        showDialog(LOGIN_DIALOG);
        //initializes connection with Parse
        Parse.initialize(this, "cuoXWbqvBKs8SUrhnyKdyNWiMPZxuDBZ31ehltVI", "tl8VMcFHu7u3haym9KSbRKEP61MmxPDvmL06dxeo"); 
    }
    
    public void onLoginClick(View view) {
    	EditText u = (EditText) findViewById (R.id.usernameField);
    	EditText p = (EditText) findViewById (R.id.passwordField);
    	String username = u.getText().toString();
    	String password = p.getText().toString();
    	
    	ParseUser userObject = ParseHandler.checkLogin(username, password);
    	if(userObject!=null){
	    	Intent i = new Intent(this, ActivitiesListActivity.class);
	    	startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_ActivitiesList);
    	}
    	else{
    		Context context = getApplicationContext();
     		CharSequence text = "Login Failed! Incorrect username or password.";
    		int duration = Toast.LENGTH_SHORT;
    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    	}
    }
    
    public void onRegisterClick(View view) {
    	Intent i = new Intent(this, RegistrationScreenActivity.class);
    	startActivity(i);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    }

}

