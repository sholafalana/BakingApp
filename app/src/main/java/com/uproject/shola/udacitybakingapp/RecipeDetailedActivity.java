package com.uproject.shola.udacitybakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.uproject.shola.udacitybakingapp.ParseModel.IngredientsItem;
import com.uproject.shola.udacitybakingapp.ParseModel.ParseRecipe;
import com.uproject.shola.udacitybakingapp.ParseModel.Step;
import com.uproject.shola.udacitybakingapp.fragment.RecipeDetailedFragment;
import com.uproject.shola.udacitybakingapp.fragment.StepDetailFragment;
import com.uproject.shola.udacitybakingapp.viewadapter.RecipeDetailedAdapter;

import java.util.ArrayList;

import butterknife.BindString;

import static com.uproject.shola.udacitybakingapp.StoredData.INTENT_EXTRA_NAME_STEP_DETAILS_INDEX;
import static com.uproject.shola.udacitybakingapp.StoredData.INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST;
import static com.uproject.shola.udacitybakingapp.StoredData.STEP_DETAILS_FRAGMENT_ARGUMENT;
import static com.uproject.shola.udacitybakingapp.StoredData.STEP_DETAILS_FRAGMENT_FULLSCREEN_ARGUMENT;
import static com.uproject.shola.udacitybakingapp.StoredData.STEP_DETAILS_FRAGMENT_VIDEO_POSITION_ARGUMENT;

public class RecipeDetailedActivity extends AppCompatActivity implements RecipeDetailedAdapter.ListItemClickListener {

    private ParseRecipe recipe;
    private FragmentManager fragmentManager;
    private RecipeDetailedFragment recipeDetailFragment;

    @BindString(R.string.toast_message_widget)
    String mWidgetToastString;
    private String mRecipeName;
    private ArrayList<IngredientsItem> mIngredients;



    private static final String SAVED_STEP_SELECTED_INDEX_KEY = "saved_step_selected_index";
    private static final String SAVED_RECIPE_KEY = "saved_recipe";
    private int stepSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detailed);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            loadDataFromExtras();
            return;
        }
        loadFromSavedInstanceState(savedInstanceState);
    }

    private void loadDataFromExtras() {
        Intent intent = getIntent();
        if (!intent.hasExtra(StoredData.INTENT_EXTRA_NAME_RECIPE_DETAILS)) {
            return;
        }
        Bundle data = intent.getExtras();
        assert data != null;
        recipe = data.getParcelable(StoredData.INTENT_EXTRA_NAME_RECIPE_DETAILS);
        mRecipeName = recipe.getName();
        mIngredients = (ArrayList<IngredientsItem>) recipe.getIngredients();
        updateActionBar();
        openRecipeDetailFragment();
        if (isLargeScreen()) {
            openStepDetailFragment(stepSelectedIndex);
        }
    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {
        recipe = savedInstanceState.getParcelable(SAVED_RECIPE_KEY);
        recipeDetailFragment = (RecipeDetailedFragment) fragmentManager.
                findFragmentById(R.id.recipe_details_fragment_container);
        stepSelectedIndex = savedInstanceState.getInt(SAVED_STEP_SELECTED_INDEX_KEY, 0);
        recipeDetailFragment.setSelectionIndex(stepSelectedIndex);
    }

    private void updateActionBar() {
        assert recipe != null;
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(recipe.getName());
    }

    private void openRecipeDetailFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(StoredData.RECIPE_DETAILS_FRAGMENT_ARGUMENT, recipe);
        recipeDetailFragment = new RecipeDetailedFragment();
        recipeDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_details_fragment_container, recipeDetailFragment)
                .commit();
    }

    @Override
    public void onListItemClick(int index) {
        if (isLargeScreen()) {
            this.stepSelectedIndex = index;
            openStepDetailFragment(index);
            return;
        }
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putParcelableArrayListExtra(INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST, new ArrayList<>(recipe.getSteps()));
        intent.putExtra(INTENT_EXTRA_NAME_STEP_DETAILS_INDEX, index);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.widget_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.widget_button:
                createWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createWidget(){

        String  ingredientsList ="";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mIngredients.size(); i++) {
            IngredientsItem ingredient = mIngredients.get(i);
            String name = ingredient.getIngredient();
            String measure = ingredient.getMeasure();
            float quantity = ingredient.getQuantity();
            String quantityString = Float.toString(quantity);
            String ing = String.format("%s %s %s \n", quantityString, measure, name);
            sb.append(ing);
            ingredientsList = sb.toString();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.baking_app_widget);
        ComponentName thisWidget = new ComponentName(this, AppWidget.class);
        remoteViews.setTextViewText(R.id.appwidget_head_text, mRecipeName);
        remoteViews.setTextViewText(R.id.appwidget_text, ingredientsList);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        Toast.makeText(this, mWidgetToastString,
                Toast.LENGTH_LONG).show();
    }

    private void openStepDetailFragment(int index) {
        Step step = recipe.getSteps().get(index);
        recipeDetailFragment.setSelectionIndex(index);
        Bundle args = new Bundle();
        args.putParcelable(STEP_DETAILS_FRAGMENT_ARGUMENT, step);
        args.putBoolean(STEP_DETAILS_FRAGMENT_FULLSCREEN_ARGUMENT, false);
        args.putLong(STEP_DETAILS_FRAGMENT_VIDEO_POSITION_ARGUMENT, C.TIME_UNSET);
        final StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.step_details_fragment_container, stepDetailFragment)
                .commit();
    }

    private boolean isLargeScreen() {
        return findViewById(R.id.activity_recipe_detail).getTag() != null &&
                findViewById(R.id.activity_recipe_detail).getTag().equals("sw600");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_RECIPE_KEY, recipe);
        outState.putInt(SAVED_STEP_SELECTED_INDEX_KEY, stepSelectedIndex);
        super.onSaveInstanceState(outState);
    }
}