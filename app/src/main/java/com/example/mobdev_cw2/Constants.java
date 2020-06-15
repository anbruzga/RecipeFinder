package com.example.mobdev_cw2;

import android.provider.BaseColumns;

public class Constants {

    public static class FeedEntry implements BaseColumns {
        public static final String PRODUCTS = "products";
        public static final String INGREDIENT = "ingredient";
        public static final String PRICE = "price";
        public static final String WEIGHT = "weight";
        public static final String DESCRIPTION = "description";
        public static final String AVAILABILITY = "availability";
    }
    private Constants(){}



}
