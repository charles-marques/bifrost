package edu.upenn.cis350.test;


import android.test.ActivityInstrumentationTestCase2;
import edu.upenn.cis350.ActivitiesListActivity;
import com.jayway.android.robotium.solo.Solo;

public class StudentRosterActivityTest extends ActivityInstrumentationTestCase2<
ActivitiesListActivity>{
	
	private Solo solo;

	public StudentRosterActivityTest () {
		super("edu.upenn.cis350", ActivitiesListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	

	public void testAddRemoveStudent (){
		solo.clickInList(0);
		solo.clickOnButton("Edit Roster");
		solo.clickOnButton("Add Student");
		solo.clickInList(2);
		solo.clickOnButton("Add");
		solo.waitForDialogToClose(2);
		solo.goBack();
		solo.clickOnButton("Refresh");
		assertTrue(solo.searchText("Sarah"));
		solo.clickOnButton("Remove Student");
		solo.clickInList(1);
		solo.clickOnButton("Remove");
		solo.waitForDialogToClose(2);
		solo.goBack();
		solo.clickOnButton("Refresh");
		assertFalse(solo.searchText("Sarah"));

	}

}