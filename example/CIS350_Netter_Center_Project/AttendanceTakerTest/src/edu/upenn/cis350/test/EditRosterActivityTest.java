package edu.upenn.cis350.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import edu.upenn.cis350.EditRosterActivity;
import edu.upenn.cis350.ActivitiesListActivity;
import com.jayway.android.robotium.solo.Solo;

public class EditRosterActivityTest extends ActivityInstrumentationTestCase2<
ActivitiesListActivity>{
	
	private Solo solo;

	public EditRosterActivityTest() {
		super("edu.upenn.cis350", ActivitiesListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	// This test tests to make sure that the app successfully submits roster
	public void testSubmit() {
		solo.clickInList(0);
		solo.clickOnButton("Take Attendance");
		solo.clickOnButton("Submit");
		
		//checks if it submits and stays on submission page
		assertTrue(solo.searchText("Submit"));
	}
}