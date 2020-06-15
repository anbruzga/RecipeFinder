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

public class SearchActivity extends AppCompatActivity {

    private TableLayout table_layout;
    private ProductsData products;
    private Button btn;
    private SQLiteDatabase db;
    private String strSQL = "";
    private EditText etValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db = RegisterActivity.getDB();
                etValue = findViewById(R.id.searchET);
                table_layout = (TableLayout) findViewById(R.id.searchTable);
                btn = (Button) findViewById(R.id.searchBtn);
                BuildTable();
            }
        });
    }


    private void BuildTable() {
        if (!strSQL.equals("")) {
            try {
                products = new ProductsData(this);
                //strSQL = "SELECT * FROM PRODUCTS ORDER BY ingredient ASC";
                db = products.getReadableDatabase();
                Cursor cursor = db.rawQuery(strSQL, null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {


                        TableRow row = new TableRow(this);
                        TableRow.LayoutParams params1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100f);
                        row.setLayoutParams(params1);
                        row.setPadding(10, 10, 10, 10);
                        row.setBackgroundColor(Color.WHITE);

                        // TO DISPLAY ID
                        //TextView tv1 = new TextView(this);
                        //TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 10f);
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
                        tv2.setPadding(10,10,10,10);

                        TextView tv5 = new TextView(this);
                        //TableRow.LayoutParams params6 = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.5f);
                        tv5.setLayoutParams(params3);
                        tv5.setTextSize(15);
                        tv5.setText("Description");
                        tv5.setPadding(10,10,10,10);



                        row.addView(tv2);

                        row.addView(tv5);


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
                            for (int j = 0; j < cols; j++) {
                                if (j == 0|| j == 2 || j == 3 || j == 5) { // not to display ID
                                    continue;
                                }

                                TextView tv = new TextView(this);
                                tv.setLayoutParams(params3);
                                tv.setGravity(Gravity.LEFT);
                                tv.setTextSize(15);
                                tv.setBackground(null);
                                tv.setPadding(0, 10, 0, 10);
                                tv.setPadding(10,10,10,10);
                                tv.setText(cursor.getString(j));
                                row2.addView(tv);



                            }

                            if (row2.getParent() != null) {
                                ((ViewGroup) row2.getParent()).removeView(row); // <- fix
                            }
                            table_layout.addView(row2);


                        } while (cursor.moveToNext());


                        TableRow row3 = new TableRow(this);
                        row.setLayoutParams(params1);
                        row3.setGravity(1);

                        table_layout.addView(row3);
                    }
                }


                if (db.isOpen()) db.close();

            } catch (SQLException mSQLException) {
                throw mSQLException;
            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Select id from sometable where name like '%omm%
                if (etValue.getText().toString().equals("")){
                    strSQL = "";
                }
                else {
                    strSQL = "SELECT * FROM PRODUCTS WHERE ingredient like '%" + etValue.getText().toString() + "%' OR description like '%" + etValue.getText().toString() + "%' ORDER BY ingredient COLLATE NOCASE ASC";
                }
                // caputure all the edittext values and put them to the array
                //strSQL = "SELECT * FROM PRODUCTS ORDER BY ingredient ASC";
                table_layout.removeAllViews(); // test line
                BuildTable();
                Toast.makeText(SearchActivity.this, "Added", Toast.LENGTH_SHORT).show();
            }
        });

    }
}