package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    Button bntcreate, bntrecom, bnttrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        bntcreate = (Button) findViewById(R.id.bntcreate);
        bntrecom = (Button) findViewById(R.id.bntrecom);
        bnttrans = (Button) findViewById(R.id.bnttrans);
    }
 
    public void bntCreate(View v) {
//        Intent create = new Intent(MainActivity2.this, createyourcake.class);
//        startActivity(create);
    }

    public void bntRecom(View v) {
        Intent recom = new Intent(MainActivity2.this, Recommendation.class);
        startActivity(recom);
    }

    public void bnttrans(View v) {
        Intent trans= new Intent(MainActivity2.this, transaction.class);
        startActivity(trans);
    }
}
