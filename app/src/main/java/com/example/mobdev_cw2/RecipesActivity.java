package com.example.mobdev_cw2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class RecipesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {



    private TableLayout table_layout;
    private TableLayout return_table_layout;
    private ProductsData products;
    private ArrayList<Pair> pairList = new ArrayList<>();
    private ArrayList<CheckBox> cbList = new ArrayList<>();
    private SQLiteDatabase db;

    private ArrayList<String> chosenProducts = new ArrayList<>();


    private TextView recipeTitle;
    private ArrayList<Food2ForkAPI> recipeList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);


        recipeTitle = (TextView) findViewById(R.id.titleText);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db = RegisterActivity.getDB();

                table_layout = (TableLayout) findViewById(R.id.searchTableLayout);

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

                    // UNCOMMENT TO SHOW THE ID IN THE TABLE
                    //TextView tv1 = new TextView(this);
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4f);
                    //tv1.setLayoutParams(params2);
                    //row.setPadding(10, 10, 10, 10);
                    //tv1.setTextSize(15);
                    //tv1.setPadding(10, 10, 10, 10);
                    //tv1.setText("ID");

                    TextView tv2 = new TextView(this);
                    TableRow.LayoutParams params3 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 20f);
                    tv2.setLayoutParams(params3);
                    //row.setPadding(10, 10, 10, 10);
                    tv2.setTextSize(15);
                    tv2.setPadding(10, 10, 10, 10);
                    tv2.setText("Product");

                    TextView tv6 = new TextView(this);
                    TableRow.LayoutParams params4 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 8f);
                    tv6.setLayoutParams(params4);
                    tv6.setTextSize(15);
                    tv6.setPadding(10, 10, 10, 10);
                    tv6.setText("Selected Items");

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


                                    //cb.setChecked(true);

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


                    TableRow row3 = new TableRow(this);
                    row.setLayoutParams(params1);
                    //row3.setGravity(1);

                    table_layout.addView(row3);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addProductsToList(int index) {
        Cursor cursor;
        String strSQL;
        System.out.println("st  + " + index);
        strSQL = "SELECT ingredient FROM products WHERE " + _ID + " = " + index;

        if (!db.isOpen()) db = products.getReadableDatabase();

        cursor = db.rawQuery(strSQL, null);
        if (cursor.moveToFirst()) {
            String data = cursor.getString(0);
            chosenProducts.add(data);
            System.out.println(data);
        }
        cursor.close();

    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows

        table.removeViews(0, childCount);

    }

    private boolean isAvailable(int id) {

        id++;
        String strSQL = "SELECT availability FROM PRODUCTS WHERE " + _ID + " = " + id;

        if (!db.isOpen()) {
            db = products.getReadableDatabase();
        }

        Cursor cursor = db.rawQuery(strSQL, null);
        cursor.moveToLast();
        String bool = cursor.getString(0);
        db.close();

        System.out.println(bool);
        if (bool.equals("1")) {
            //System.out.println("returning true");
            return true;
        } else {
            //System.out.println("returning false");
            return false;

        }
    }

    /*public class WorkerThread implements Runnable{
        private View view;
        public WorkerThread(View view){
            super();
            this.view = view;
        }
        @Override
        public void run() {

        }
    }*/

    //onClick method
    public void searchRecipes(View view) {

        synchronized (this) {
            captureCheckboxes();
            connectToServer(view);

        }
        cleanTable(table_layout);
        chosenProducts.clear();
        db.close();
        pairList.clear();
        cbList.clear();
        recipeList.clear();
        BuildTable(); // MUST BE THE LAST OF ALL CLOSES AND CLEARS otherwise will clear created LISTS!


        Toast.makeText(RecipesActivity.this, "Selected", Toast.LENGTH_SHORT).show();
        if (db.isOpen()) db.close();

    }


    private void generateResult() {

        return_table_layout = findViewById(R.id.returnTableLayout);

        TableRow row = new TableRow(this);
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100f);
        row.setLayoutParams(params1);
        row.setPadding(10, 10, 10, 10);
        row.setBackgroundColor(Color.WHITE);

        TableRow.LayoutParams params3 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 20f);

        return_table_layout.addView(row);

        boolean changeColor = false;

        System.out.println(recipeList.size() + "SIZE");
        for (int j = 0; j < recipeList.size(); j++) {
            System.out.println("STEP j = " + j);
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

            TextView tv = new TextView(this);
            tv.setPadding(10, 10, 10, 10);
            tv.setLayoutParams(params3);




            tv.setTextSize(15);
            tv.setText(recipeList.get(j).getTitle());

            tv.setClickable(true);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "";

            text = text + "<a href='" + recipeList.get(j).getF2f_url() + "'> "
                    + recipeList.get(j).getTitle() + " </a>";

            tv.setText(Html.fromHtml(text));

            row2.addView(tv);

            return_table_layout.addView(row2);
        }



    }


    private void connectToServer(View view) {
        //String queryString = recipesET.getText().toString();
        String queryString = "";
        for (int i = 0; i < chosenProducts.size(); i++) {
            if (i == chosenProducts.size() - 1) {
                queryString = queryString + chosenProducts.get(i) + "";
            } else {
                queryString = queryString + chosenProducts.get(i) + ", ";
            }

        }
        System.out.println(queryString.toString());
        System.out.println(chosenProducts.toString());
        // TAKEN FROM https://github.com/google-developer-training/android-fundamentals-apps-v2/blob/master/WhoWroteItLoader/app/src/main/java/com/example/android/whowroteitloader/MainActivity.java
        // Hide the keyboard on button click
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        //Check the network connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }


        // If the network is available, connected, and the search field is not empty,
        // start a RecipeLoader Async Task
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);


            recipeTitle.setText("Loading");

            // Otherwise update the TextView to tell the user there is no
            // connection, or no search term.
        } else {
            if (queryString.length() == 0) {
                recipeTitle.setText("No Search Term");
            } else {
                recipeTitle.setText("No Network");
            }
        }
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if (args != null) {
            queryString = args.getString("queryString");
        }

        return new RecipeLoader(this, queryString);
    }

    // the end of google's dev code ***************************************************//
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {


        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(data);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("recipes");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;


            // Look for results in the items array, exiting
            // when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length()) {
                // Get the current item information.
                JSONObject recipe = itemsArray.getJSONObject(i);
                System.out.println(i);
                Food2ForkAPI f2f = new Food2ForkAPI();

                String recipeTitle = recipe.getString("title");
                String recipeId = recipe.getString("recipe_id");
                String f2fUrl = recipe.getString("f2f_url");
                String publisher = recipe.getString("publisher");
                String source_url = recipe.getString("source_url");
                String image_url = recipe.getString("image_url");
                String social_rank = recipe.getString("social_rank");
                String publisher_url = recipe.getString("publisher_url");

                f2f.setTitle(recipeTitle);
                f2f.setRecipe_id(recipeId);
                f2f.setF2f_url(f2fUrl);
                f2f.setPublisher(publisher);
                f2f.setSource_url(source_url);
                f2f.setImage_url(image_url);
                f2f.setSocial_rank(social_rank);
                f2f.setPublisher_url(publisher_url);


                // System.out.println(recipe.toString());
                recipeList.add(f2f);

                i++;
            }

            System.out.println(recipeList.toString());

            // If both are found, display the result.
            if (!recipeList.isEmpty()) {
                recipeTitle.setText(title);
                recipeTitle.setText("Found:");
            } else {
                // If none are found, update the UI to
                // show failed results.
                recipeTitle.setText("No results");
                //mIdText.get().setText("");
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            e.printStackTrace();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            recipeTitle.setText("Failed...");
        }


        generateResult();
    }

    //https://github.com/google-developer-training/android-fundamentals-apps-v2/blob/master/WhoWroteItLoader/app/src/main/java/com/example/android/whowroteitloader/MainActivity.java
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Do nothing.  Required by interface.
    }

    // The end of taken code

    private void captureCheckboxes() {
        //chosenProducts.clear();

        for (int i = 0; i < cbList.size(); i++) {
            if (cbList.get(i).isChecked()) pairList.get(i).setBoolValue(true);
            else if (!cbList.get(i).isChecked()) pairList.get(i).setBoolValue(false);
        }


        for (int i = 0; i < pairList.size(); i++) {
            if (pairList.get(i).isBoolValue()) {
                //System.out.println("STEP 1; i=" + i);
                i++;
                addProductsToList(i);
                i--;
            } else {
                //System.out.println("STEP 1 FAILED; i =" + 1);
            }
        }
    }

}