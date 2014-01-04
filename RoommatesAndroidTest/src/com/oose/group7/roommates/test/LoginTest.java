package com.oose.group7.roommates.test;

import com.oose.group7.roommates.Login;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

public class LoginTest extends android.test.ActivityUnitTestCase<Login> {

	private Login loginActivity;
	
	public LoginTest() {
		super(Login.class);
	}
	
	@Override
	protected void setUp() throws Exception {
	    super.setUp();

	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	            Login.class);
	    startActivity(intent, null, null);
	    loginActivity = getActivity();

	    //username = loginActivity.findViewById(R.id.login_username_input);

	  } // end of setUp() method definition

	public void testLayout() {
		Button signin = (Button) loginActivity.findViewById(
	    		com.oose.group7.roommates.R.id.login_signin_button);
	    assertNotNull("Button not allowed to be null", signin);
	    assertEquals("Incorrect label of the button", "Sign In", signin.getText());
	}

	public void testIntentTriggerViaOnClick() {
		Button signin = (Button) loginActivity.findViewById(
	    		com.oose.group7.roommates.R.id.login_signin_button);
		assertNotNull("Button not allowed to be null", signin);
		signin.performClick();
	    
	    // TouchUtils cannot be used, only allowed in 
	    // InstrumentationTestCase or ActivityInstrumentationTestCase2 
	  
	    // Check the intent which was started
	    Intent triggeredIntent = getStartedActivityIntent();
	    assertNotNull("Intent was null", triggeredIntent);
	}
}
