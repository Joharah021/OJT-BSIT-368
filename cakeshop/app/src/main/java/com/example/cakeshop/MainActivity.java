package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editUser, editPass;
    private TextView textReg;
    private Button bntLogin;
    private DataBase dbutilities;

    private  void initWidgets(){
        /**
         *
         *  initialize widgets eg: textformfields | buttons
         *
         **/
        this.dbutilities = new DataBase(this,null);
        this.editUser = (EditText)findViewById(R.id.editUser) ;
        this.editPass = (EditText)findViewById(R.id.editPass) ;
        this.bntLogin = (Button)findViewById(R.id.bntLogin)   ;
        this.textReg = (TextView) findViewById(R.id.textReg)  ;
    }
    private void showToast(String message){
        /**
         * display's notification in a short period of time
         **/
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private boolean isFormHasEmpty(){
        /**
         *
         * returns true if both/either 1 of them email and password field is empty!
         *
         **/
        return (this.editUser.getText().toString().isEmpty() && this.editPass.getText().toString().isEmpty())?
                true:
                (this.editUser.getText().toString().isEmpty()||this.editPass.getText().toString().isEmpty())?true:false;
    }
    private void validateForm(){
        if(this.isFormHasEmpty()){
            // if not filled
           if(this.editUser.getText().toString().isEmpty()){
               //please modify message
               editUser.setError("Fill up this form");
           }
           if(this.editPass.getText().toString().isEmpty()){
               //please modify message
               editPass.setError("Fill up this form");
           }
            //please modify message
            this.showToast("Please Fillup all the fields!");
        }else{
            // if filled
            String username = this.editUser.getText().toString();
            String password = this.editPass.getText().toString();
            boolean isUserExist = this.dbutilities.tryLogin(username,password);
            try {
                if(!isUserExist){
                    // user does not exist
                    this.showToast("Account Not Found!");
                }else{
                    //user exist | redirect to its homepage
                    Intent home;
                    if(TemporaryState.accountType == TemporaryState.USER){
                        // if user/customer
                        home = new Intent(MainActivity.this, UserHomeMenu.class);
                        startActivity(home);
                    }else if(TemporaryState.accountType == TemporaryState.VENDOR){
                        // if vendor
                        home = new Intent(MainActivity.this, VendorHomeMenu.class);
                        startActivity(home);
                    }
                }
            }catch (Exception e){
                //please modify message
                this.showToast("MainActivityClassError > validateForm: "+e.toString());
            }
        }
    }
    private void onRegister(View v){
        /**
         *  if register btn is clicked
         **/
        Intent registration_menu = new Intent(getApplicationContext(),RegistrationMenu.class);
        startActivity(registration_menu);
    }private void onLogin(View v){
        /**
         *  if login btn is clicked
         **/
        this.validateForm();
    }
    private void addListeners(){
        /**
         *
         *  adds action event listener to widgets
         *
         **/
        this.textReg.setOnClickListener(this::onRegister);
        this.bntLogin.setOnClickListener(this::onLogin)  ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)    ;
        setContentView(R.layout.activity_main);
        this.initWidgets() ;
        this.addListeners();
        this.testLogin()   ;

    }

    @Override
    protected void onDestroy() {
        this.dbutilities.destroy();
        super.onDestroy()         ;
    }
    private void testLogin(){
        /**
         * replace with your onw username and password to prevent typing upon login
         **/
        this.editUser.setText("");
        this.editPass.setText("");
    }
}
