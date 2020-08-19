package edu.upenn.cis350.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import edu.upenn.cis350.ActivitiesListActivity;
import com.jayway.android.robotium.solo.Solo;

public class EditProfileActivityTest extends ActivityInstrumentationTestCase2 <
ActivitiesListActivity> {

	
	private Solo solo;

	
	public EditProfileActivityTest () {
		super("edu.upenn.cis350", ActivitiesListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testEditProfile  (){
		solo.clickInList(0);
		solo.clickOnButton("Edit Roster");
		solo.clickInList(0);
		solo.clickOnButton("Edit");
		solo.waitForActivity("EditProfileActivity");
		ArrayList<EditText> texts = solo.getCurrentEditTexts();
		solo.typeText(texts.get(6), "testtext");
		solo.clickOnButton ("Done");
		solo.clickOnButton("Refresh");
		solo.goBack();
		solo.goBack();
		solo.clickOnButton("Edit Roster");
		solo.clickInList(0);
//		solo.clickOnButton("Edit Roster");
		assertTrue(solo.searchText("testtext"));

	}
	
	

}

