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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.mobdev_cw2.Constants.FeedEntry.AVAILABILITY;


public class DisplayProductsActivity extends AppCompatActivity {
    TableLayout table_layout;
    ProductsData products;
    ArrayList<Pair> pairList = new ArrayList<>();
    ArrayList<CheckBox> cbList = new ArrayList<>();
    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);


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
                    TableRow.LayoutParams params1 = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 100f);
                    row.setLayoutParams(params1);
                    row.setPadding(10, 10, 10, 10);
                    row.setBackgroundColor(Color.WHITE);


                    // UNCOMMENT TO ADD ID TO THE TABLE
                    //TextView tv1 = new TextView(this);
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 10f);
                    //tv1.setLayoutParams(params2);
                    //row.setPadding(10, 10, 10, 10);
                    //tv1.setTextSize(15);
                    //tv1.setText("ID");

                    TextView tv2 = new TextView(this);
                    TableRow.LayoutParams params3 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 20f);
                    params3.setMargins(0,-14,0,-14);
                    tv2.setLayoutParams(params3);
                    row.setPadding(10, 10, 10, 10);
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
                    TableRow.LayoutParams params4 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 11f);
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
                        row2.setPadding(30, 10, 10, 30);
                        if (changeColor) {
                            changeColor = false;
                            row2.setBackgroundColor(Color.argb(56,94,245,111));
                        } else {
                            row2.setBackgroundColor(Color.WHITE);
                            changeColor = true;
                        }
                        int cols = cursor.getColumnCount();
                        int counter = 0; // for organising setWeight()
                        for (int j = 0; j < cols + 1; j++) {

                            if (j == 0){ // to skip ID
                                counter++;
                                continue;
                            }
                            if (j == cols) {

                                CheckBox cb = new CheckBox(this);
                                cb.setLayoutParams(params2);
                                //cb.setGravity(Gravity.LEFT);
                                row2.addView(cb);
                                cbList.add(cb);
                                final int k = j;

                                Pair pair = new Pair(id, false);
                                pairList.add(pair);

                                break;

                            }
                            if (counter != 5) {
                                TextView tv = new TextView(this);
                                if (counter == 0) {
                                    tv.setLayoutParams(params2);
                                } else tv.setLayoutParams(params3);

                                counter++;

                                tv.setGravity(Gravity.START);
                                tv.setTextSize(15);
                                //tv.setPadding(10,30,10,30);
                                tv.setText(cursor.getString(j));

                                row2.addView(tv);
                            }
                        }

                        if (row2.getParent() != null) {
                            ((ViewGroup) row2.getParent()).removeView(row); // <- fix
                        }
                        table_layout.addView(row2);
                    } while (cursor.moveToNext());

                    Button btn = new Button(this);
                    btn.setBackgroundColor(Color.argb(75,94,245,111));
                    btn.setPadding(30,10,30,10);
                    btn.setText("Add to Kitchen");

                    btn.setVisibility(View.VISIBLE);
                    TableRow row3 = new TableRow(this);
                    row.setLayoutParams(params1);
                    row3.addView(btn);
                    row3.setGravity(1);

                    table_layout.addView(row3);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < cbList.size(); i++) {
                                if (cbList.get(i).isChecked()) pairList.get(i).setBoolValue(true);

                            }
                            for (int i = 0; i < pairList.size(); i++) {
                                System.out.println(pairList.get(i).getId() + "  " + pairList.get(i).isBoolValue() + ", i = " + i);


                                if (pairList.get(i).isBoolValue()) {
                                    i++;
                                    String strSQL = "UPDATE products SET " + AVAILABILITY + " = 1 WHERE " + _ID + " = " + i;
                                    if (!db.isOpen()) {
                                        db = products.getWritableDatabase();
                                        db.execSQL(strSQL);
                                        db.close();
                                    } else {
                                        db.execSQL(strSQL);
                                        db.close();
                                    }
                                    i--;
                                }
                                // Uncomenting will add 0 availability if checkbox is unchecked
                               /*else{
                                    i++;
                                    String strSQL = "UPDATE products SET "+ AVAILABILITY +" = 0 WHERE " + _ID + " = "+ i;
                                    if(!db.isOpen()) {
                                        db = products.getWritableDatabase();
                                        db.execSQL(strSQL);
                                        db.close();
                                    }
                                    else {
                                        db.execSQL(strSQL);
                                        db.close();
                                    }
                                    i--;
                                }*/

                            }
                            pairList.clear();
                            cbList.clear();
                            //BuildTable(); // test line
                            Toast.makeText(DisplayProductsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (db.isOpen()) db.close();
                }
            }
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
            throw mSQLException;
        }

    }
}
/*
ARRAYLIST OF OBJ PAIR(INT, BOOLEAN)
        1 INT STORES THE ID OF ROW
        2 BOOLEAN STORES CHECKBOX VALUE TRUE OR FALSE

ON BUTTON CLICK SCAN THE ARAYLIST AND CHECK ALL THE PAIRS.
    IF TRUE IS FOUND, SEND THE IDS TO UPDATE METHOD

*/
