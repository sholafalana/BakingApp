package com.uproject.shola.udacitybakingapp.ParseModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: shola
 * Created by: ModelGenerator on 10/10/2018
 */
public class ParseRecipe implements Parcelable {
    private long id;
    private String name;
    private List<IngredientsItem> ingredients = null;
    private List<Step> steps = null;
    private int servings;
    private String image;

    public ParseRecipe() {

    }

    private ParseRecipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readList(ingredients, IngredientsItem.class.getClassLoader());
        steps = new ArrayList<>();
        in.readList(steps, Step.class.getClassLoader());
        servings = in.readInt();
        image = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientsItem> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsItem> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Creator<ParseRecipe> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Creator<ParseRecipe> CREATOR = new Creator<ParseRecipe>() {
        @Override
        public ParseRecipe createFromParcel(Parcel in) {
            return new ParseRecipe(in);
        }

        @Override
        public ParseRecipe[] newArray(int size) {
            return new ParseRecipe[size];
        }
    };

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", servings=" + servings +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParseRecipe)) return false;

        ParseRecipe recipe = (ParseRecipe) o;

        if (id != recipe.id) return false;
        if (servings != recipe.servings) return false;
        if (!name.equals(recipe.name)) return false;
        if (!ingredients.equals(recipe.ingredients)) return false;
        if (!steps.equals(recipe.steps)) return false;
        return image.equals(recipe.image);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + ingredients.hashCode();
        result = 31 * result + steps.hashCode();
        result = 31 * result + servings;
        result = 31 * result + image.hashCode();
        return result;
    }
}