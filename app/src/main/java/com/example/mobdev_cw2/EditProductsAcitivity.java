package com.example.mobdev_cw2;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.mobdev_cw2.Constants.FeedEntry.AVAILABILITY;
import static com.example.mobdev_cw2.Constants.FeedEntry.DESCRIPTION;
import static com.example.mobdev_cw2.Constants.FeedEntry.INGREDIENT;
import static com.example.mobdev_cw2.Constants.FeedEntry.PRICE;
import static com.example.mobdev_cw2.Constants.FeedEntry.WEIGHT;

public class EditProductsAcitivity extends AppCompatActivity {

    TableLayout table_layout;
    ProductsData products;
    ArrayList<Product> productList = new ArrayList<>();
    ArrayList<ArrayList<EditText>> originalETValues = new ArrayList<>();

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products_acitivity);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db = RegisterActivity.getDB();

                table_layout = (TableLayout) findViewById(R.id.TableLayout);

                BuildTable();
            }
        });
    }


    private void BuildTable() {
        try {
            products = new ProductsData(this);
            String sql = "SELECT * FROM PRODUCTS ORDER BY ingredient COLLATE NOCASE ASC";
            db = products.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {


                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams params1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100f);
                    row.setLayoutParams(params1);
                    row.setPadding(10, 10, 10, 10);
                    row.setBackgroundColor(Color.WHITE);

                    // UNDO TO DISPLAY ID
                    //TextView tv1 = new TextView(this);
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 10f);
                    //tv1.setLayoutParams(params2);
                    //row.setPadding(10, 10, 10, 10);
                    //tv1.setTextSize(15);
                    //tv1.setText("ID");

                    TextView tv2 = new TextView(this);
                    TableRow.LayoutParams params3 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 20f);
                    tv2.setLayoutParams(params3);
                    //row.setPadding(10, 10, 10, 10);
                    tv2.setTextSize(15);
                    tv2.setText("Product");

                    TextView tv3 = new TextView(this);
                    //TableRow.LayoutParams params4 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.5f);
                    tv3.setLayoutParams(params3);
                    //row.setPadding(10, 10, 10, 10);
                    tv3.setTextSize(15);
                    tv3.setText("Price");

                    TextView tv4 = new TextView(this);
                    //TableRow.LayoutParams params5 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.5f);
                    tv4.setLayoutParams(params3);
                    //row.setPadding(10, 10, 10, 10);
                    tv4.setTextSize(15);
                    tv4.setText("Weight");

                    TextView tv5 = new TextView(this);
                    //TableRow.LayoutParams params6 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.5f);
                    tv5.setLayoutParams(params3);
                    tv5.setTextSize(15);
                    tv5.setText("Description");

                    TextView tv6 = new TextView(this);
                    TableRow.LayoutParams params4 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 11f);
                    tv6.setLayoutParams(params4);
                    tv6.setTextSize(15);
                    tv6.setText("Availability");

                    //row.addView(tv1);
                    row.addView(tv2);
                    row.addView(tv3);
                    row.addView(tv4);
                    row.addView(tv5);
                    row.addView(tv6);

                    table_layout.addView(row);

                    boolean changeColor = true;
                    int id = -1;
                    do {


                        id++;
                        TableRow row2 = new TableRow(this);
                        row2.setLayoutParams(params1);
                        row2.setPadding(10, 10, 10, 10);
                        ArrayList<EditText> rowOfET = new ArrayList<>();
                        if (changeColor) {
                            changeColor = false;
                            row2.setBackgroundColor(Color.argb(56,94,245,111));
                        } else {
                            row2.setBackgroundColor(Color.WHITE);
                            changeColor = true;
                        }
                        int cols = cursor.getColumnCount();
                        int counter = 0; // for organising setWeight()
                        for (int j = 0; j < cols; j++) {
                            if(j == 0){ // not to display ID
                                continue;
                            }

                            final EditText et = new EditText(this);

                            if (j == 0) {
                                et.setLayoutParams(params2);
                            } else if (j == cols - 1) et.setLayoutParams(params4);
                            else et.setLayoutParams(params3);

                            // for displaying availability instead of 0 and 1

                            counter++;

                            et.setGravity(Gravity.LEFT);
                            et.setTextSize(15);
                            et.setBackground(null);
                            et.setPadding(0, 10, 0, 10);
                            //et.setPadding(10,10,10,10);
                            et.setText(cursor.getString(j));

                            rowOfET.add(et);
                            row2.addView(et);

                            if (j == cols-1){
                                String availability = et.getText().toString();
                                if (availability.equals("1")) et.setText("Available");
                                else if (availability.equals("0")) et.setText("Not Available");
                                else if(availability.equals("-1")) {
                                    et.setTextColor(Color.RED);
                                    et.setText("Wrong Input");
                                }
                            }

                        }

                        if (row2.getParent() != null) {
                            ((ViewGroup) row2.getParent()).removeView(row); // <- fix
                        }
                        table_layout.addView(row2);
                        originalETValues.add(rowOfET);

                    } while (cursor.moveToNext());

                    Button btn = new Button(this);
                    btn.setText("Save Editing");
                    btn.setPadding(30,10,30,10);

                    btn.setVisibility(View.VISIBLE);
                    btn.setBackgroundColor(Color.argb(56,94,245,111));
                    TableRow row3 = new TableRow(this);
                    row.setLayoutParams(params1);
                    row3.addView(btn);
                    row3.setGravity(1);

                    table_layout.addView(row3);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String strSQL = "";
                            // caputure all the edittext values and put them to the array
                            for (int i = 0; i < originalETValues.size(); i++) { // stores the ID
                                for (int j = 0; j < originalETValues.get(i).size(); j++) { //stores attributes in row
                                    //if (j == 0) {
                                    //    continue;
                                    if (j == 0) {

                                        System.out.println(originalETValues.get(i).get(j).getText().toString() + " id " + i + ", element " + j);
                                        String location = originalETValues.get(i).get(j).getText().toString();
                                        i++;
                                        strSQL = "UPDATE products SET " + INGREDIENT + " = '" +
                                                location
                                                + "' WHERE " + _ID + " = " + i;
                                        i--;

                                    } else if (j == 1) {
                                        String location = originalETValues.get(i).get(j).getText().toString();
                                        i++;
                                        strSQL = "UPDATE products SET " + PRICE + " = " +
                                                location +
                                                " WHERE " + _ID + " = " + i;
                                        i--;
                                    } else if (j == 2) {
                                        String location = originalETValues.get(i).get(j).getText().toString();
                                        i++;
                                        strSQL = "UPDATE products SET " + WEIGHT + " = " +
                                                 location +
                                                " WHERE " + _ID + " = " + i;
                                        i--;
                                    } else if (j == 3) {
                                        String location = originalETValues.get(i).get(j).getText().toString();
                                        i++;
                                        strSQL = "UPDATE products SET " + DESCRIPTION + " = '" +
                                                 location +
                                                "' WHERE " + _ID + " = " + i;
                                        i--;
                                    } else {


                                        String location = originalETValues.get(i).get(j).getText().toString(); // prevents not integers
                                        int availability;
                                        if (location.equalsIgnoreCase("available")){
                                            availability = 1;
                                        }
                                        else if (location.equalsIgnoreCase("not available")){
                                            availability = 0;
                                        }
                                        else {
                                            availability = -1;
                                        }

                                        i++;
                                        strSQL = "UPDATE products SET " + AVAILABILITY + " = " +
                                                availability +
                                                " WHERE " + _ID + " = " + i;
                                        i--;
                                    }
                                    if (!db.isOpen()) {
                                        db = products.getWritableDatabase();

                                        db.execSQL(strSQL);
                                        db.close();
                                    } else {
                                        db.execSQL(strSQL);
                                        db.close();
                                    }


                                    // update the database with all the values


                                    //  if (productList.get(i).isBoolValue())
                                    // clear the lists

                                }


                                // }


                            }
                            originalETValues.clear();
                            table_layout.removeAllViews();
                            BuildTable(); // test line
                            Toast.makeText(EditProductsAcitivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (db.isOpen()) db.close();
                }
            }
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

    }
}