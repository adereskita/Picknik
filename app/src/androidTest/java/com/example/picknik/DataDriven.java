package com.example.picknik;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.picknik.Login.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class DataDriven {

    FirebaseUser cUser;
    FirebaseAuth mFirebaseAuth;

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);


    public void sleep1S() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void automatedTestingUI() {


        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null){
            mFirebaseAuth.signOut();
        }

        onView(withId(R.id.btnSingIn)).perform(click());
        sleep1S();
        onView(withId(R.id.edtEmail)).perform(typeText(DataGiven.testUser2.getEmail()));
        sleep1S();
        onView(withId(R.id.edtPassword)).perform(typeText(DataGiven.testUser2.getPassword()));
        sleep1S();
        onView(withId(R.id.btnSingIn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
