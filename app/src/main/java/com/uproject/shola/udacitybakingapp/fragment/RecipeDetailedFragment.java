package com.uproject.shola.udacitybakingapp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uproject.shola.udacitybakingapp.StoredData;
import com.uproject.shola.udacitybakingapp.ParseModel.ParseRecipe;
import com.uproject.shola.udacitybakingapp.R;
import com.uproject.shola.udacitybakingapp.RecipeDetailedActivity;
import com.uproject.shola.udacitybakingapp.viewadapter.RecipeDetailedAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shola on 10/12/2018.
 */

public class RecipeDetailedFragment extends Fragment {

    @BindView(R.id.recipe_details_recycler_view)
    RecyclerView recyclerView;

    private RecipeDetailedAdapter recipeDetailedAdapter;
    private Bundle savedInstanceState;
    private static final String SAVED_LAYOUT_MANAGER_KEY = "saved_layout_manager_detail";
    private int stepSelectedIndex = -1;

    public RecipeDetailedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detailed, container, false);
        ButterKnife.bind(this, rootView);
        this.savedInstanceState = savedInstanceState;
        applyConfiguration();
        return rootView;
    }

    private void applyConfiguration() {
        ParseRecipe recipe = getArguments().getParcelable(StoredData.RECIPE_DETAILS_FRAGMENT_ARGUMENT);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recipeDetailedAdapter = new RecipeDetailedAdapter(getContext(), (RecipeDetailedActivity) getActivity());
        recyclerView.setAdapter(recipeDetailedAdapter);
        recipeDetailedAdapter.setRecipeData(recipe);
        restoreViewState();
        restoreSelectionIndex();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_LAYOUT_MANAGER_KEY, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    private void restoreViewState() {
        if (savedInstanceState == null) {
            return;
        }
        Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER_KEY);
        recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }

    private void restoreSelectionIndex() {
        if (stepSelectedIndex != -1) {
            recipeDetailedAdapter.setSelectionIndex(stepSelectedIndex);
        }
    }

    public void setSelectionIndex(int index) {
        stepSelectedIndex = index;
        if (recipeDetailedAdapter != null) {
            recipeDetailedAdapter.setSelectionIndex(index);
        }
    }

}