package edu.upenn.cis350;

import com.parse.Parse;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;


/**
 * Screen where user is prompted to register a new
 * username and password for the application.
 */
public class RegistrationScreenActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate (savedInstanceState);
    	setContentView (R.layout.resgistration);
    	//initializes connection with Parse
        Parse.initialize(this, "cuoXWbqvBKs8SUrhnyKdyNWiMPZxuDBZ31ehltVI", "tl8VMcFHu7u3haym9KSbRKEP61MmxPDvmL06dxeo"); 
    	
    }

	public void onRegisterAccountButtonClick (View view) {
		EditText registration_name = (EditText) findViewById (R.id.reg_username);
    	// EditText email = (EditText) findViewById (R.id.reg_email);
    	EditText password = (EditText) findViewById (R.id.reg_password);
    	EditText password_confirm = (EditText) findViewById (R.id.reg_password_confirm);
    	
    	String nameString = registration_name.getText().toString();
    	// String emailString = email.getText().toString();
    	String passwordString  = password.getText().toString();
    	String passConfirmString = password_confirm.getText().toString();
    	

    	if ((nameString.length() == 0) /*|| (emailString.length() == 0)*/ || (passwordString.length() == 0) || (passConfirmString.length() == 0)) {
    		Context context = getApplicationContext();
    		CharSequence text = "Some required field(s) are blank";
    		int duration = Toast.LENGTH_SHORT;
    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    		
    	}
    	
    	else if (!(passwordString.equals(passConfirmString))) {
    		Context context = getApplicationContext();
    		CharSequence text = "Incorrect password, please try again";
    		int duration = Toast.LENGTH_SHORT;
    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    		
    	}
    	
    	else{
    		ParseHandler.signUpUser(nameString, passwordString, getApplicationContext());
    		Intent i = new Intent(this, AttendanceTakerActivity.class);
	    	startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_RegistrationActivity);

    	}
		
	}
	
}
