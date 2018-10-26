package com.uproject.shola.udacitybakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Created by shola on 10/21/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityTest {

    @Rule
    public final ActivityTestRule<RecipeActivity> recipeActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = recipeActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void checkRecipeDetailFragmentContainer_RecipeDetailActivity() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.bake_rv)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_details_fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void checkText_RecipeDetailActivity() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.bake_rv)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

}