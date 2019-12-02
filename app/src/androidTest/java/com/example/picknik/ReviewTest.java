package com.example.picknik;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)

public class ReviewTest {
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
    public void reviewTest() {

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
        onView(withId(R.id.nav_search)).perform(click());
        sleep1S();


        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_list_search),
                        childAtPosition(
                                withId(R.id.container),
                                3)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        sleep1S();

        ViewInteraction tabView = onView(
                allOf(withContentDescription("Review"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tabs),
                                        0),
                                2),
                        isDisplayed()));
        tabView.perform(click());

        sleep1S();


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_seeAll), withText("See All"),
                        childAtPosition(
                                withParent(withId(R.id.container_details)),
                                7),
                        isDisplayed()));
        appCompatButton3.perform(click());

        sleep1S();

        onView(withId(R.id.btn_writeReviews)).perform(click());
        sleep1S();
        onView(withId(R.id.edt_title_review)).perform(typeText("Ayamnya Enak"));
        sleep1S();
        onView(withId(R.id.edt_reviewAdd)).perform(typeText("Sangat tidak diragukan !"));
        sleep1S();
        onView(withId(R.id.btn_addReview)).perform(click());
        sleep1S();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
