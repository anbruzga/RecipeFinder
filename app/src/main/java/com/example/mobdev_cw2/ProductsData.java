package com.example.mobdev_cw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.mobdev_cw2.Constants.FeedEntry.AVAILABILITY;
import static com.example.mobdev_cw2.Constants.FeedEntry.DESCRIPTION;
import static com.example.mobdev_cw2.Constants.FeedEntry.INGREDIENT;
import static com.example.mobdev_cw2.Constants.FeedEntry.PRICE;
import static com.example.mobdev_cw2.Constants.FeedEntry.PRODUCTS;
import static com.example.mobdev_cw2.Constants.FeedEntry.WEIGHT;

public class ProductsData extends SQLiteOpenHelper {

    private static final String PRODUCTS_DB = "products.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PRODUCTS + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    INGREDIENT + " TEXT," +
                    PRICE + " INT," +
                    WEIGHT + " INT," +
                    DESCRIPTION + " TEXT," +
                    AVAILABILITY + " INT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PRODUCTS;

    public ProductsData(Context ctx) {
        super(ctx, PRODUCTS_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    Cursor getAllProducts() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PRODUCTS, null);
    }

    boolean updateProducts(int id, String name, int weight, int price, String descri) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INGREDIENT, name);
        contentValues.put(WEIGHT, weight);
        contentValues.put(PRICE, price);
        contentValues.put(DESCRIPTION, descri);
        return db.update(PRODUCTS, contentValues, _ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    boolean updateAvailability(int id, boolean boolValue) {
        SQLiteDatabase db = getWritableDatabase();
        if (boolValue)
            db.execSQL("UPDATE " + PRODUCTS + " SET availability=" + boolValue + " WHERE " + _ID + "=" + id + "");
        else
            db.execSQL("UPDATE " + PRODUCTS + " SET availability=" + boolValue + " WHERE " + _ID + "=" + id + "");
        return true;
    }


    boolean deleteProducts(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(PRODUCTS, _ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public void drop() {
        SQLiteDatabase db = getWritableDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        // db.execSQL("TRUNCATE table" + TABLE_NAME);
        db.close();
    }


}
