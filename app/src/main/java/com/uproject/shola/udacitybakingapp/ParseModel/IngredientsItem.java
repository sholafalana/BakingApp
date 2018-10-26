package com.uproject.shola.udacitybakingapp.ParseModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: shola
 * Created by: ModelGenerator on 10/10/2018
 */
public class IngredientsItem implements Parcelable {
    private Float quantity;
    private String measure;
    private String ingredient;

    public Float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    public IngredientsItem() {
    }

    protected IngredientsItem(Parcel in) {
        this.quantity = (Float) in.readValue(Float.class.getClassLoader());
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Parcelable.Creator<IngredientsItem> CREATOR = new Parcelable.Creator<IngredientsItem>() {
        @Override
        public IngredientsItem createFromParcel(Parcel source) {
            return new IngredientsItem(source);
        }

        @Override
        public IngredientsItem[] newArray(int size) {
            return new IngredientsItem[size];
        }
    };
}