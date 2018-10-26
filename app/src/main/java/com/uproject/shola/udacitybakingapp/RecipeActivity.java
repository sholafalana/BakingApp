package com.uproject.shola.udacitybakingapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uproject.shola.udacitybakingapp.ParseModel.ParseRecipe;
import com.uproject.shola.udacitybakingapp.utils.NetworkSingleton;
import com.uproject.shola.udacitybakingapp.utils.SimpleIdlingResource;
import com.uproject.shola.udacitybakingapp.viewadapter.RecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uproject.shola.udacitybakingapp.StoredData.INTENT_EXTRA_NAME_RECIPE_DETAILS;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    @BindView(R.id.error_tv)
    TextView mErrorTextView;
    @BindView(R.id.loading_pb)
    ProgressBar mLoadingBar;
    @BindView(R.id.bake_rv)
    RecyclerView mBakeRecyclerView;
    @BindString(R.string.network_url)
    String mRecipeURL;
    @BindString(R.string.error_message)
    String mNetworkError;
    @BindString(R.string.not_connected)
    String mNoNetwork;
    private static ArrayList<ParseRecipe> mDataSource;
    private RecipeAdapter mBakeRecyclerViewAdapter;
    @Nullable
    private SimpleIdlingResource idlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);
        mDataSource = new ArrayList<>();
        if (findViewById(R.id.tablet_layout) != null) {
            //Grid layout on tablet
            mBakeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            //linear layout phone
            mBakeRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));
            mBakeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mBakeRecyclerViewAdapter = new RecipeAdapter(mDataSource, this);
        mBakeRecyclerView.setHasFixedSize(true);
        mBakeRecyclerView.setAdapter(mBakeRecyclerViewAdapter);
        if(isConnected()){
            applyIdlingConfiguration();
            fetchRecipeData();

        }else{
            mErrorTextView.setVisibility(View.VISIBLE);
            mErrorTextView.setText(mNoNetwork);}
    }

    private void fetchRecipeData() {
        mLoadingBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = NetworkSingleton.getInstance(this).getRequestQueue();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, mRecipeURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        ArrayList<ParseRecipe> recipes = new ArrayList<>();
                        try {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject recipeObject = array.getJSONObject(i);
                                Gson gson = new GsonBuilder().create();
                                ParseRecipe r = gson.fromJson(String.valueOf(recipeObject), ParseRecipe.class);
                                recipes.add(r);
                            }
                            mLoadingBar.setVisibility(View.INVISIBLE);
                            mDataSource = recipes;
                            mBakeRecyclerViewAdapter.setDataSource(mDataSource);
                            if (idlingResource != null) {
                                idlingResource.setIdleState(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLoadingBar.setVisibility(View.INVISIBLE);
                        mErrorTextView.setVisibility(View.VISIBLE);
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("VisibleForTests")
    private void applyIdlingConfiguration() {
        RecipeActivity recipeActivity = RecipeActivity.this;
        idlingResource = (SimpleIdlingResource) recipeActivity.getIdlingResource();
        idlingResource.setIdleState(false);
    }

    protected boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return  (netInfo != null && netInfo.isConnectedOrConnecting()) ;}
        return true;
    }

    @Override
    public void onListItemClick(ParseRecipe recipe) {

            Intent detailIntent = new Intent(this, RecipeDetailedActivity.class);
            detailIntent.putExtra(INTENT_EXTRA_NAME_RECIPE_DETAILS, recipe);
            startActivity(detailIntent);
        

    }
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

}

