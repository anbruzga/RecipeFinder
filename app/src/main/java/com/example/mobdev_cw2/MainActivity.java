package com.example.mobdev_cw2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button btn1 = (Button) findViewById(R.id.registerProductBtn);
        Button btn2 = (Button) findViewById(R.id.displayProductsBtn);
        Button btn3 = (Button) findViewById(R.id.availabilityBtn);
        Button btn4 = (Button) findViewById(R.id.editProductsBtn);
        Button btn5 = (Button) findViewById(R.id.searchBtn);
        Button btn6 = (Button) findViewById(R.id.recipesBtn);


        btn1.setOnClickListener(this);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(i);
            }
        });

        btn2.setOnClickListener(this);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), DisplayProductsActivity.class);
                view.getContext().startActivity(i);
            }
        });

        btn3.setOnClickListener(this);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AvailabilityActivity.class);
                view.getContext().startActivity(i);
            }
        });

        btn4.setOnClickListener(this);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditProductsAcitivity.class);
                view.getContext().startActivity(i);
            }
        });
        btn5.setOnClickListener(this);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), SearchActivity.class);
                view.getContext().startActivity(i);
            }
        });
        btn6.setOnClickListener(this);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RecipesActivity.class);
                view.getContext().startActivity(i);
            }
        });

    }


    @Override
    public void onClick(View v) {

    }
}