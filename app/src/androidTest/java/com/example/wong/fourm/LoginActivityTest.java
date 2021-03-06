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
public class LoginActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void checkLogin() throws InterruptedException {


        // 用户名,密碼是空，点击登录
        onView(allOf(withId(R.id.et_email), isDisplayed())).perform(replaceText(""));
        onView(allOf(withId(R.id.et_password), isDisplayed())).perform(replaceText(""));
        onView(allOf(withId(R.id.btn_login), isDisplayed())).perform(click());
        //会弹出一个文本为clicked的Toast
        Thread.sleep(1000);
        onView(withText("Auth failed"))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
        Thread.sleep(3000);

        // 用户名和密码都正确，点击登录
        onView(allOf(withId(R.id.et_email), isDisplayed())).perform(replaceText("newuser@gmail.com"));
        onView(allOf(withId(R.id.et_password), isDisplayed())).perform(replaceText("123456"));
        onView(allOf(withId(R.id.btn_login), isDisplayed())).perform(click());
        //会弹出一个文本为clicked的Toast
//        Thread.sleep(1000);
        onView(withText("Auth successful"))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

    }
}
