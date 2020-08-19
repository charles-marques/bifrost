package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Screen where user can edit the information pertaining
 * to a student.
 *
 */

public class EditProfileActivity extends Activity{
	Bundle extras;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
		extras = getIntent().getExtras();

		String studentName = extras.getString("name");
		EditText studentNameView = (EditText)findViewById(R.id.editProfileName);
		studentNameView.setText(studentName);

		String studentPhoneNumber = extras.getString("phone");
		EditText phoneNumberView = (EditText)findViewById(R.id.editProfilePhone);
		phoneNumberView.setText(studentPhoneNumber);

		String studentAddress = extras.getString("address");
		EditText studentAddressView = (EditText)findViewById(R.id.editProfileAddress);
		studentAddressView.setText(studentAddress);

		String studentIDNumber = extras.getString("idNumber");
		EditText studentIDNumberView = (EditText)findViewById(R.id.editProfileIDNumber);
		studentIDNumberView.setText(studentIDNumber);

		String studentContactName = extras.getString("contactName");
		EditText studentContactNameView = (EditText)findViewById(R.id.editProfileEmergContName);
		studentContactNameView.setText(studentContactName);

		String studentContactType = extras.getString("contactType");
		EditText studentContactTypeView = (EditText)findViewById(R.id.editProfileEmergContType);
		studentContactTypeView.setText(studentContactType);

		String studentSite = extras.getString("site");
		EditText studentSiteView = (EditText)findViewById(R.id.editProfileSite);
		studentSiteView.setText(studentSite);

		String studentGrade = extras.getString("grade");
		EditText studentGradeView = (EditText)findViewById(R.id.editProfileGrade);
		studentGradeView.setText(studentGrade);

		String studentSchool = extras.getString("school");
		EditText studentSchoolView = (EditText)findViewById(R.id.editProfileSchool);
		studentSchoolView.setText(studentSchool);

	}

	public void onDoneButtonClick(View view){
		String nameEdit = ((EditText)findViewById(R.id.editProfileName)).getText().toString();
		String phoneEdit = ((EditText)findViewById(R.id.editProfilePhone)).getText().toString();	
		String addressEdit = ((EditText)findViewById(R.id.editProfileAddress)).getText().toString();
		String idEdit = ((EditText)findViewById(R.id.editProfileIDNumber)).getText().toString();
		String ecnEdit = ((EditText)findViewById(R.id.editProfileEmergContName)).getText().toString();
		String ectEdit = ((EditText)findViewById(R.id.editProfileEmergContType)).getText().toString();
		String siteEdit = ((EditText)findViewById(R.id.editProfileSite)).getText().toString();
		String gradeEdit = ((EditText)findViewById(R.id.editProfileGrade)).getText().toString();
		String schoolEdit = ((EditText)findViewById(R.id.editProfileSchool)).getText().toString();

		ParseHandler.editStudent(extras.getString("name"), nameEdit, phoneEdit, 
				addressEdit, idEdit, ecnEdit, ectEdit, siteEdit, gradeEdit, schoolEdit);

		finish();
	}
}
