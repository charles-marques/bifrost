package edu.upenn.cis350.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import edu.upenn.cis350.ActivitiesListActivity;
import com.jayway.android.robotium.solo.Solo;

public class ActivitiesListActivityTest extends ActivityInstrumentationTestCase2<
	ActivitiesListActivity>{
	
	private Solo solo;

	public ActivitiesListActivityTest() {
		super("edu.upenn.cis350", ActivitiesListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	// This test tests to make sure that the app successfully adds an activity to the
	// online database and then the activity shows up only once the list is refreshed.
	public void testAddActivity() {
		solo.clickOnButton("Add Activity");
		ArrayList<EditText> texts = solo.getCurrentEditTexts();
		solo.typeText(texts.get(0), "test");
		solo.clickOnButton("Add");
		solo.waitForDialogToClose(2);
		//assertTrue(true);
		assertFalse(solo.searchText("test"));
		solo.clickOnButton("Refresh");
		assertTrue(solo.searchText("test"));
		
	}
}
