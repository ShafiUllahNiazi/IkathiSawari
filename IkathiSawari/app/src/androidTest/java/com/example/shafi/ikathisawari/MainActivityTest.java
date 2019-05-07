package com.example.shafi.ikathisawari;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Time;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule=new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity=null;

    @Before
    public void setUp() throws Exception {
        mainActivity=activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }

    @Test
    public void checklaunch() {

        View viewsignup=mainActivity.findViewById(R.id.email_Login);
        View viewsignin=mainActivity.findViewById(R.id.password_Login);
        View viewfindlabour=mainActivity.findViewById(R.id.signin_Btn);

        assertNotNull(viewsignup);
        assertNotNull(viewsignin);
        assertNotNull(viewfindlabour);
    }

    @Test
    public void checkloginButton() throws Throwable {
        final TextView login = mainActivity.findViewById(R.id.signin_Btn);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertTrue(login.performClick());
            }
        });
    }

    @Test
    public void checksignupButton() throws Throwable {
        final TextView signup = mainActivity.findViewById(R.id.signUp);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertTrue(signup.performClick());
            }
        });
    }

    @Test
    public void checktemail() {
        final EditText email= (EditText) mainActivity.findViewById(R.id.email_Login);

        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                email.setText("shafi@gmail.com");

                assertEquals("shafi@gmail.com", email.getText().toString());
            }
        });


    }

}