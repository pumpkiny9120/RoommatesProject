package com.oose2013.group7.roommates.impression;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Main activity for game impression. It will calls a new canvas and gives the key word.
 * @author lbowen5
 */

public class Impression extends Activity{

    MyView mAnimView=null;

    @Override
    public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	    
	    mAnimView=new MyView(this);
	    setContentView(mAnimView);
	    Toast.makeText(getApplicationContext(), "Please draw: Shark", Toast.LENGTH_LONG).show();
    }
}