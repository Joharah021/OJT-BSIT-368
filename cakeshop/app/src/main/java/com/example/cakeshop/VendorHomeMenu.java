package com.example.cakeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class VendorHomeMenu extends AppCompatActivity {
    private Button btnprod,btnOrders,btnRecords;

    public void initWidgets(){
        /**
         *
         *  initialize widgets eg: textformfields | buttons
         *
         * */
        this.btnprod   = (Button) findViewById(R.id.bntprod);
        this.btnOrders = (Button) findViewById(R.id.btnOrders);
        this.btnRecords = (Button) findViewById(R.id.btnRecords);
    }
    private void onAddProduct(View v){
        //redirect to add product | ProductAdder
        Intent addproduct = new Intent(VendorHomeMenu.this,ProductAdder.class);
        startActivity(addproduct);
    }
    private void onOrders(View v)
    {
        System.out.println("TO ORDERS");
        Intent orders = new Intent(VendorHomeMenu.this, VendorOrders_Queue_Verify.class);
        startActivity(orders);
    }
    private void onRecords(View v)
    {
        Intent record = new Intent(VendorHomeMenu.this,Records.class);
        startActivity(record);
    }
    private void addListeners(){
        /**
         *
         *  adds action event listener to widgets
         *
         **/
        this.btnprod.setOnClickListener(this::onAddProduct);
        this.btnOrders.setOnClickListener(this::onOrders)  ;
        this.btnRecords.setOnClickListener(this::onRecords);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_home_menu);
        this.initWidgets();
        this.addListeners();
    }
}
