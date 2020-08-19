	package edu.upenn.cis350.test;
	
	import java.util.ArrayList;
	
	import android.test.ActivityInstrumentationTestCase2;
	import android.widget.EditText;
	import edu.upenn.cis350.RegistrationScreenActivity;
	import com.jayway.android.robotium.solo.Solo;
	
	public class RegistrationScreenActivityTest extends ActivityInstrumentationTestCase2<
	RegistrationScreenActivity>{
	
	private Solo solo;
	
	public RegistrationScreenActivityTest() {
	super("edu.upenn.cis350", RegistrationScreenActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
	super.setUp();
	solo = new Solo(getInstrumentation(), getActivity());
	}
	
	// This test tests to make sure that the app successfully registers a new user to the
	// online database and then moves back to the Login page
	public void testRegister() {
	ArrayList<EditText> texts = solo.getCurrentEditTexts();
	solo.typeText(texts.get(0), "newuser");
	solo.typeText(texts.get(1), "password");
	solo.typeText(texts.get(2), "password");
	solo.clickOnButton("Register New Account");
	
	//checks if it made it to the login page because teh login page has
	//the 'Register' button
	assertTrue(solo.searchText("Register"));

	}
	}