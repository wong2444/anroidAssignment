package com.example.wong.fourm;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddArticleActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(AddArticleActivity.class);

    @Test
    public void checkAddArticle() throws InterruptedException {


        // 標題名,內容是空，点击add
        onView(allOf(withId(R.id.et_title), isDisplayed())).perform(replaceText(""));
        onView(allOf(withId(R.id.et_body), isDisplayed())).perform(replaceText(""));
        onView(allOf(withId(R.id.btn_submit), isDisplayed())).perform(click());
        //会弹出一个文本为error的Toast
        Thread.sleep(1000);
        onView(withText("error"))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
        Thread.sleep(3000);

        // 標題名,內容不是空，点击add
        onView(allOf(withId(R.id.et_title), isDisplayed())).perform(replaceText("testTitle"));
        onView(allOf(withId(R.id.et_body), isDisplayed())).perform(replaceText("testBody"));
        onView(allOf(withId(R.id.btn_submit), isDisplayed())).perform(click());
        //会弹出一个文本为clicked的Toast
//        Thread.sleep(1000);
        onView(withText("Created article success"))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

    }
}
