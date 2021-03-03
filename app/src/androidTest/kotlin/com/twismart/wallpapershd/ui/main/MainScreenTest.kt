/*
 * Copyright (C) 2017 Sneyder Angulo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twismart.wallpapershd.ui.main

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.twismart.wallpapershd.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainScreenTest {

    @Rule @JvmField
    val mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun test1SwipeDown_ClickWallpaper_SwitchFavorite_ReturnMainActivity() {
        onView(allOf(withId(R.id.swipeRefresh), isDisplayed())).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))

        clickItemWallpaperInRecyclerView()

        clickSwitchFavoriteInWallpaper()

        returnToMainActivity()
    }

    private fun clickItemWallpaperInRecyclerView() {
        val recyclerView = onView(
                allOf(withId(R.id.recyclerViewWallpapers),
                        childAtPosition(
                                allOf(withId(R.id.swipeRefresh),
                                        withParent(withId(R.id.mViewPager))),
                                0),
                        isDisplayed()))
        recyclerView.perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
    }

    private fun clickSwitchFavoriteInWallpaper() {
        val appCompatImageView = onView(
                allOf(withId(R.id.imageSwitchFavorite),
                        childAtPosition(
                                allOf(withId(R.id.frameWrapper),
                                        childAtPosition(
                                                withClassName(`is`("android.support.design.widget.CoordinatorLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView.perform(click())
    }

    private fun returnToMainActivity() {
        val appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())
    }

    @Test
    fun test2clickMostPopularInTabLayout() {
        val tabView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tabLayout),
                                0),
                        1),
                        isDisplayed()))
        tabView.perform(click())

        val viewPager = onView(
                allOf(withId(R.id.mViewPager),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                0),
                        isDisplayed()))
        viewPager.perform(swipeLeft())
    }

    @Test
    fun test3ClickFavoriteInTabLayout() {
        val tabView2 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tabLayout),
                                0),
                        2),
                        isDisplayed()))
        tabView2.perform(click())

        val viewPager2 = onView(
                allOf(withId(R.id.mViewPager),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                0),
                        isDisplayed()))
        viewPager2.perform(swipeLeft())

    }


    /**
     * It's useful to avoid boilerplate code
     * */
    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    /**
     * It's necessary to use swipeDown() function in SwipeRefreshLayout
     * */
    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {

        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController, view: View) {
                action.perform(uiController, view)
            }
        }
    }
}