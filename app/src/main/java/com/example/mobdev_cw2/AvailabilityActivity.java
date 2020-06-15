package com.example.mobdev_cw2;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

public class AvailabilityActivity extends AppCompatActivity {
    private TableLayout table_layout;
    private ProductsData products;
    private ArrayList<Pair> pairList = new ArrayList<>();
    private ArrayList<CheckBox> cbList = new ArrayList<>();
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

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

                    // UNCOMMENT TO SHOW THE ID IN THE TABLE
                    //TextView tv1 = new TextView(this);
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 4f);
                    //tv1.setLayoutParams(params2);
                    //row.setPadding(10, 10, 10, 10);
                    //tv1.setTextSize(15);
                    //tv1.setPadding(10, 10, 10, 10);
                    //tv1.setText("ID");

                    TextView tv2 = new TextView(this);
                    TableRow.LayoutParams params3 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 20f);
                    tv2.setLayoutParams(params3);
                    //row.setPadding(10, 10, 10, 10);
                    tv2.setTextSize(15);
                    tv2.setPadding(10, 10, 10, 10);
                    tv2.setText("Product");

                    TextView tv6 = new TextView(this);
                    TableRow.LayoutParams params4 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 8f);
                    tv6.setLayoutParams(params4);
                    tv6.setTextSize(15);
                    tv6.setPadding(10, 10, 10, 10);
                    tv6.setText("Availability");

                    //row.addView(tv1);
                    row.addView(tv2);

                    row.addView(tv6);

                    table_layout.addView(row);

                    boolean changeColor = true;
                    int id = -1;
                    do {


                        id++;
                        if (isAvailable(id)) {
                            TableRow row2 = new TableRow(this);
                            row2.setLayoutParams(params1);
                            row2.setPadding(10, 10, 10, 10);
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
                                if (counter == 0) {
                                    counter++;
                                    continue;
                                }
                                if (j == cols) {

                                    CheckBox cb = new CheckBox(this);
                                    cb.setLayoutParams(params2);


                                    cb.setChecked(true);

                                    //cb.setGravity(Gravity.LEFT);
                                    row2.addView(cb);
                                    cbList.add(cb);
                                    final int k = j;


                                    Pair pair = new Pair(id, false);
                                    pairList.add(pair);

                                    break;

                                }
                                if (counter != 5 && counter != 4 && counter != 3 && counter != 2) {
                                    TextView tv = new TextView(this);
                                    tv.setPadding(10, 10, 10, 10);
                                    if (counter == 0) {
                                        tv.setLayoutParams(params2);
                                    } else tv.setLayoutParams(params3);

                                    counter++;

                                    //tv.setGravity(Gravity.LEFT);
                                    tv.setTextSize(15);
                                    tv.setText(cursor.getString(j));
                                    tv.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                                    row2.addView(tv);
                                }
                            }


                            if (row2.getParent() != null) {
                                ((ViewGroup) row2.getParent()).removeView(row); // <- fix
                            }
                            table_layout.addView(row2);
                        } else {
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cbList.add(cb);
                            Pair pair = new Pair(id, false);
                            pairList.add(pair);
                        }


                    } while (cursor.moveToNext());

                    Button btn = new Button(this);
                    btn.setBackgroundColor(Color.argb(56,94,245,111));
                    btn.setText("Save");

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
                                } else {
                                    i++;
                                    String strSQL = "UPDATE products SET " + AVAILABILITY + " = 0 WHERE " + _ID + " = " + i;
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

                            }
                            pairList.clear();
                            cbList.clear();
                            cleanTable(table_layout);
                            BuildTable();
                            Toast.makeText(AvailabilityActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (db.isOpen()) db.close();
                }
            }
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows

        table.removeViews(0, childCount);

    }

    private boolean isAvailable(int id) {

        id++;
        String strSQL = "SELECT " + AVAILABILITY + " FROM PRODUCTS WHERE " + _ID + " = " + id;

        if (!db.isOpen()) {
            db = products.getReadableDatabase();
        }

        Cursor cursor = db.rawQuery(strSQL, null);
        cursor.moveToLast();
        String bool = cursor.getString(0);
        db.close();

        System.out.println(bool);
        if (bool.equals("1")) {
            System.out.println("returning true");
            return true;
        } else {
            System.out.println("returning false");
            return false;

        }
    }
}