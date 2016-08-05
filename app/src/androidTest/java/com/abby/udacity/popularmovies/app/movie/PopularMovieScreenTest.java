/*
 * Copyright 2015, The Android Open Source Project
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

package com.abby.udacity.popularmovies.app.movie;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.espresso.DataInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.abby.udacity.popularmovies.app.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

/**
 * Tests for the notes screen, the main screen which contains a grid of all notes.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PopularMovieScreenTest {


    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p/>
     * <p/>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<PopularMovieActivity> mActivityTestRule =
            new ActivityTestRule<>(PopularMovieActivity.class);

    @Test
    public void testGridView() throws Exception {

        onRow(10).perform(click());


        // wait for activity finished
        while (!mActivityTestRule.getActivity().isFinishing()) {
            TimeUnit.SECONDS.sleep(1);
        }
    }


    @Test
    public void testLandscape() throws Exception {

        // landscape
        Activity activity = mActivityTestRule.getActivity();

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onRow(10).perform(click());

        // wait for activity finished
        while (!mActivityTestRule.getActivity().isFinishing()) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static DataInteraction onRow(Integer atPosition) {
        return onData(anything()).inAdapterView(withId(R.id.gridview_movie)).atPosition(atPosition);
    }
}