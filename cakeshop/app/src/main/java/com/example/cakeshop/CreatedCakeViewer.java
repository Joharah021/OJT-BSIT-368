package com.example.cakeshop;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreatedCakeViewer extends AppCompatActivity {
    private TextView cakeName;
    private TextView cakeShape;
    private TextView cakeFlavour;
    private TextView cakeSize;
    private TextView cakeDescription;

    private void initWidgets(){
        this.cakeName = (TextView) findViewById(R.id.cakeName);
        this.cakeShape = (TextView) findViewById(R.id.cakeShape);
        this.cakeFlavour = (TextView) findViewById(R.id.cakeFlavour);
        this.cakeSize = (TextView) findViewById(R.id.cakeSize);
        this.cakeDescription = (TextView) findViewById(R.id.cakeDescription);

        String[] data = getIntent().getStringArrayExtra("CREATEDCAKEDATA");
        /*
         * ORDER:
         *   id , user_id , cakename , cakeshape , cake flavour, cake size , cake description
         *   0  ,    1    ,    2     ,     3     ,      4      ,     5     ,         6
         *
         * */
        this.cakeName.setText(data[2]);
        this.cakeShape.setText(data[3]);
        this.cakeFlavour.setText(data[4]);
        this.cakeSize.setText(data[5]);
        this.cakeDescription.setText(data[6]);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_cake_view);
        this.initWidgets();
    }
}
