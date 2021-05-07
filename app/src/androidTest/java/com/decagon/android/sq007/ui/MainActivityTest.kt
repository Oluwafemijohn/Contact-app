package com.decagon.android.sq007.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.decagon.android.sq007.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
//import com.decagon.android.sq007.R
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{
        companion  object {
            private const val NAME = "Oluwafemi"
            private const val PHONENUMBER = "08030675563"

        }
    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_isActivityInView() {
        Espresso.onView(withId(R.id.implementation_one_list_recycler))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
//
//    @Test
//    fun test_recyclerView() {
//        Espresso.onView(withId(R.id.recycler_view))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }
}