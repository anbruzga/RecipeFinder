package com.example.mobdev_cw2;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class RecipeLoader extends AsyncTaskLoader<String> {

    private String mQueryString;

    RecipeLoader(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getRecipeInfo(mQueryString);
    }
}