package com.uproject.shola.udacitybakingapp.viewadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uproject.shola.udacitybakingapp.ParseModel.ParseRecipe;
import com.uproject.shola.udacitybakingapp.R;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shola on 10/11/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {
    private ArrayList<ParseRecipe> mDataSource;
    private final ListItemClickListener mListItemClickListener;

    public RecipeAdapter(ArrayList<ParseRecipe> dataSource, ListItemClickListener listItemClickListener) {
        mDataSource = dataSource;
        mListItemClickListener = listItemClickListener;
    }

    public void setDataSource(ArrayList<ParseRecipe> dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    @Override
    public @NonNull
    RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainview_item, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mDataSource == null) {
            return 0;
        }
        return mDataSource.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(ParseRecipe recipe);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_tv)
        TextView mRecipeTextView;
        @BindView(R.id.serves_tv)
        TextView mServingsTextView;
        @BindString(R.string.serves)
        String mServes;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        void bind(int listIndex) {
            ParseRecipe recipe = mDataSource.get(listIndex);
            mRecipeTextView.setText(recipe.getName());
            String servingCount = Integer.toString(recipe.getServings());
            String servings = String.format("%s %s", mServes, servingCount);
            mServingsTextView.setText(servings);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ParseRecipe recipe = mDataSource.get(position);
            mListItemClickListener.onListItemClick(recipe);
        }
    }
}

