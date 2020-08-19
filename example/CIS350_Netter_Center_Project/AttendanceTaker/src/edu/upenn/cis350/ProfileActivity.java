package edu.upenn.cis350;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Representation of a profile for a student.
 * Every student profile has the student's name,
 * student ID, grade, site, school, address, and
 * emergency contact information displayed.
 */
public class ProfileActivity extends Activity{
	
	Bundle extras;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        extras = getIntent().getExtras();
        
        String studentName = extras.getString("name");
        TextView studentNameView = (TextView)findViewById(R.id.profileName);
        studentNameView.setText(studentName);
        
        String studentPhoneNumber = extras.getString("phone");
        TextView phoneNumberView = (TextView)findViewById(R.id.profilePhone);
        phoneNumberView.setText(studentPhoneNumber);
        
        String studentAddress = extras.getString("address");
        TextView studentAddressView = (TextView)findViewById(R.id.profileAddress);
        studentAddressView.setText(studentAddress);
        
        String studentIDNumber = extras.getString("idNumber");
        TextView studentIDNumberView = (TextView)findViewById(R.id.profileIDNumber);
        studentIDNumberView.setText(studentIDNumber);
        
        String studentContactName = extras.getString("contactName");
        TextView studentContactNameView = (TextView)findViewById(R.id.profileEmergContName);
        studentContactNameView.setText(studentContactName);
        
        String studentContactType = extras.getString("contactType");
        TextView studentContactTypeView = (TextView)findViewById(R.id.profileEmergContType);
        studentContactTypeView.setText(studentContactType);
        
        String studentSite = extras.getString("site");
        TextView studentSiteView = (TextView)findViewById(R.id.profileSite);
        studentSiteView.setText(studentSite);
        
        String studentGrade = extras.getString("grade");
        TextView studentGradeView = (TextView)findViewById(R.id.profileGrade);
        studentGradeView.setText(studentGrade);
        
        String studentSchool = extras.getString("school");
        TextView studentSchoolView = (TextView)findViewById(R.id.profileSchool);
        studentSchoolView.setText(studentSchool);
        
    }

	public void onEditButtonClick(View view) {
		Intent i = new Intent(view.getContext(), EditProfileActivity.class);
		i.putExtras(extras);
		startActivityForResult(i, AttendanceTakerActivity.ACTIVITY_EditProfile);
	}
	
	public void onRefreshClick(View view) {
		
	}

}