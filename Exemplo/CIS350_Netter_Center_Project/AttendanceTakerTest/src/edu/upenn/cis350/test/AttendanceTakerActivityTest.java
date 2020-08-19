package edu.upenn.cis350.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import edu.upenn.cis350.AttendanceTakerActivity;
import com.jayway.android.robotium.solo.Solo;

public class AttendanceTakerActivityTest extends ActivityInstrumentationTestCase2<
AttendanceTakerActivity>{
	
	private Solo solo;

	public AttendanceTakerActivityTest() {
		super("edu.upenn.cis350", AttendanceTakerActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	// This test tests to make sure that the app successfully adds an activity to the
	// online database and then the activity shows up only once the list is refreshed.
	public void testLogin() {
		ArrayList<EditText> texts = solo.getCurrentEditTexts();
		solo.typeText(texts.get(0), "tester");
		solo.typeText(texts.get(1), "test");
		solo.clickOnButton("Login");
		
		//checks if it made it to the activities page
		assertTrue(solo.searchText("Activities"));
	}
}
