package com.example.picknik;


import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ScrollToAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddPlaceTest {

    FirebaseUser cUser;
    FirebaseAuth mFirebaseAuth;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    public void sleep1S() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addPlaceTest() {

        //auth user
        mFirebaseAuth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            mFirebaseAuth.signOut();
        }
        sleep1S();
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

        onView(withId(R.id.nav_profile)).perform(click());
        sleep1S();
        onView(withId(R.id.btn_inputPlace)).perform(click());
        sleep1S();
        onView(withId(R.id.edt_pName)).perform(typeText("Playlist Live 2019"));
        sleep1S();
        onView(withId(R.id.edt_pPhone)).perform(typeText("081959621425"));
        sleep1S();
        onView(withId(R.id.edt_location)).perform(typeText("Jalan Dipatiukur"));
        sleep1S();
        onView(withId(R.id.rdo_event)).perform(click());
        sleep1S();
        onView(withId(R.id.type_spinner)).perform(scrollTo(),click());
        sleep1S();
        onData(anything()).atPosition(14).perform(scrollTo(), click());
        onView(withId(R.id.type_spinner)).check(matches(withSpinnerText(containsString("Music Festival"))));
        sleep1S();
//        onView(withId(R.id.time_open)).perform(scrollTo(), click(), PickerActions.setTime(10, 30));
        onView(withId(R.id.time_open)).perform(scrollTo(), typeText("10:30am"));
        sleep1S();
//        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(22, 00));
        onView(withId(R.id.time_close)).perform(typeText("10:00pm"));
        sleep1S();
        onView(withId(R.id.edt_cost)).perform(scrollTo(), typeText("150000"));
        sleep1S();
        onView(withId(R.id.rdo_yesOwn)).perform(scrollTo(), click());
        sleep1S();
        onView(withId(R.id.btn_ChoosePict)).perform(scrollTo(), click());
        sleep1S();

        final Intent data = new Intent();
        data.setData(Uri.parse("content://media/external/images/media/458")); // put here URI that you want select in gallery
//        Runnable runnable = new Runnable(){
//            @Override
//            public void run() {
//                getActivity().onActivityResult(3333,-1, data);
//                synchronized(this) {
//                    this.notify();
//                }
//            }
//        };
//        synchronized(runnable) {
//            mActivityTestRule.getActivity().runOnUiThread(runnable);
//            try {
//                runnable.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        onView(withId(R.id.edt_description)).perform(scrollTo(), typeText("Banyak Artis, buruan !"));
        sleep1S();
        onView(withId(R.id.btn_submitData)).perform(click());
        sleep1S();


    }

}
