package com.example.cakeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationMenu extends AppCompatActivity {
    private Button btncus,btnven;

    private void initWidgets(){
        /**
        *  initialize buttons | widget(s)
        * */
        this.btncus = (Button) findViewById(R.id.bntcus);
        this.btnven = (Button) findViewById(R.id.bntven);
    }


    private void toCustomerRegistrationForm(View v){
        TemporaryState.isCustomer = true;
        Intent toCostumer = new Intent(RegistrationMenu.this,RegistrationForm.class);
        startActivity(toCostumer);
    }
    private void toVendorRegistrationForm(View v){
        //redirects to vendor registration form
        TemporaryState.isCustomer = false;
        Intent registervendor = new Intent(RegistrationMenu.this,VendorRegistrationForm.class);
        startActivity(registervendor);
    }

    private void addListeners(){
        /**
        *  adds click event to buttons
        * */
        this.btncus.setOnClickListener(this::toCustomerRegistrationForm);
        this.btnven.setOnClickListener(this::toVendorRegistrationForm);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_menu);
        this.initWidgets();
        this.addListeners();
    }

}
