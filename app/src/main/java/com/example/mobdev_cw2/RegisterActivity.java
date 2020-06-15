package com.example.mobdev_cw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.mobdev_cw2.Constants.FeedEntry.AVAILABILITY;
import static com.example.mobdev_cw2.Constants.FeedEntry.DESCRIPTION;
import static com.example.mobdev_cw2.Constants.FeedEntry.INGREDIENT;
import static com.example.mobdev_cw2.Constants.FeedEntry.PRICE;
import static com.example.mobdev_cw2.Constants.FeedEntry.PRODUCTS;
import static com.example.mobdev_cw2.Constants.FeedEntry.WEIGHT;

public class RegisterActivity extends AppCompatActivity {

    protected ProductsData products;
    private EditText etName;
    private EditText etWeight;
    private EditText etPrice;
    private EditText etDescri;
    boolean[] filled = new boolean[3];
    private static SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        start();
    }

    private void start(){
        etName = findViewById(R.id.rgProductName);
        etWeight = findViewById(R.id.rgWeight);
        etPrice = findViewById(R.id.rgPrice);
        etDescri = findViewById(R.id.rgDescri);



    }

    private void addProduct (String productName, double weight, double price, String descri) {
        db = products.getWritableDatabase();

        //db.delete(PRODUCTS, null,null);
        /*try {
            db.execSQL("ALTER TABLE products ADD COLUMN availability INTEGER DEFAULT 0");
        }
        catch (android.database.sqlite.SQLiteException ex){
            // do nothing
        }*/

        ContentValues values = new ContentValues();
        values.put(INGREDIENT, productName);
        values.put(WEIGHT, weight);
        values.put(PRICE, price);
        values.put(AVAILABILITY, false);
        if (descri != null) values.put(DESCRIPTION, descri);
        values.put(AVAILABILITY, 0);
        db.insertOrThrow(PRODUCTS, null, values);

        System.out.println("Added succesfully!");
        Toast toast=Toast.makeText(getApplicationContext(),"Saved Successfuly!",Toast.LENGTH_LONG);
        toast.show();

        removeETValues();
        db.close();
    }

    private void removeETValues() {
        etName.setText("");
        etPrice.setText("");
        etWeight.setText("");
        etDescri.setText("");
    }

    public void rgBtnOnClick(View view) {

        products = new ProductsData(this);
        String productName = etName.getText().toString();
        double weight = 0.0;
        double price = 0.0;
        try {
            weight = Double.parseDouble(etWeight.getText().toString());
        }
        catch (java.lang.NumberFormatException ex){
            System.out.println(ex);
            provideErrorMsg();
        }
        try {
            price = Double.parseDouble(etPrice.getText().toString());

        }
        catch (java.lang.NumberFormatException ex){
            System.out.println(ex);
            provideErrorMsg();
        }


        String descri = etDescri.getText().toString();


        EditTextsFilled(productName, weight, price);

        if (filled[0] && filled[1] && filled[2]){
            try {
                addProduct(productName, weight, price, descri);
            } finally {
                products.close();
                // reset colors of eddittexts
                resetColorsET();
            }

        }
        else{
            provideErrorMsg(); // changes colors if user missed some input
        }
    }

    private void resetColorsET() {
        etName.setHintTextColor(Color.rgb(153,153,153));
        etWeight.setHintTextColor(Color.rgb(153,153,153));
        etPrice.setHintTextColor(Color.rgb(153,153,153));
    }

    private void provideErrorMsg() {
        if (!filled[0]) etName.setHintTextColor(Color.rgb(255,0,0));
        if (!filled[1]) etWeight.setHintTextColor(Color.rgb(255,0,0));
        if (!filled[2]) etPrice.setHintTextColor(Color.rgb(255,0,0));
    }

    // additionally could take description if needed to check it too
    private void EditTextsFilled(String name, double weight, double price) {

        if (name.equals(null) || name.equals("")) filled[0] = false;
        else filled[0] = true;
        if (weight == 0) filled[1] = false;
        else filled[1] = true;
        if (price == 0) filled[2] = false;
        else filled[2] = true;
    }

    public static SQLiteDatabase getDB(){
        return db;
    }
}
