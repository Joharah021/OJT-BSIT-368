package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class venform extends AppCompatActivity {

    EditText editUser, editPass, editRepass,editAddress,editCon,editFname,editLname,editShop;
    Button bntSignup;
    utilities db;
    String verPass,verEmail,verFinal;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_venform);

        editUser = (EditText)findViewById(R.id.editUserN);
        editPass = (EditText)findViewById(R.id.editPass2);
        editRepass = (EditText)findViewById(R.id.editRePass);

        editAddress= (EditText)findViewById(R.id.editAddress);
        editCon = (EditText)findViewById(R.id.editContact);
        editFname = (EditText)findViewById(R.id.editFname);

        editLname = (EditText)findViewById(R.id.editLname);
        editShop = (EditText)findViewById(R.id.editShop);
        bntSignup = (Button)findViewById(R.id.bntSignup);
        textView2=(TextView)findViewById(R.id.textView2);

        try {
            db = new utilities(this,null);
        }catch(Exception e){
            Toast.makeText(venform.this, e+" ERROR ",Toast.LENGTH_LONG).show();}
        editUser = (EditText)findViewById(R.id.editUserN);
        editPass = (EditText)findViewById(R.id.editPass2);
        editRepass = (EditText)findViewById(R.id.editRePass);
     //   editEmail = (EditText)findViewById(R.id.editEmail);
        editAddress= (EditText)findViewById(R.id.editAddress);
        editCon = (EditText)findViewById(R.id.editContact);
        editFname = (EditText)findViewById(R.id.editFname);
       editShop = (EditText)findViewById(R.id.editShop);
        editLname = (EditText)findViewById(R.id.editLname);

        bntSignup = (Button)findViewById(R.id.bntSignup);
        textView2=(TextView)findViewById(R.id.textView2);
        editShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(editShop.getText().toString().equals(" "))
                {
                    verEmail = "";
                    // diri ibuutang ang code para change color kung mismatch ang pass
                    Toast.makeText(getApplicationContext(),"Check the damn Shopname bruh!",Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();
                }
                else if(!editShop.getText().toString().equals("")){
                    verEmail = "";
                    verEmail = "Y";
                    //   Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();
                    //diri kung match na ang password
                }
            }
        });
        editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editPass.getText().toString().equals(editRepass.getText().toString())){ //diri kung match na ang password
                    verPass = "";
                  //R4  Toast.makeText(getApplicationContext(),"Password dont matchs                                                                                                                                                                                                                                                                                                                                         ",Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();

                }
                else
                {verPass = "";verPass = "Y"; // Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editRepass.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!editRepass.getText().toString().equals(editPass.getText().toString()))
                {
                    verPass = "";
                    // diri ibuutang ang code para change color kung mismatch ang pass
                    Toast.makeText(getApplicationContext(),"Password dont match",Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();
                }
                else
                {verPass = "";verPass = "Y"; // Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(venform.this,MainActivity.class);
                startActivity(log);
            }
        });
        bntSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verPass == "Y" && verEmail == "Y")
                {
                    // Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();
                    verFinal = "Y";
                }
                if (editUser.getText().toString().equals("") || editPass.getText().toString().equals("") || editRepass.getText().toString().equals("") || editFname.getText().toString().equals("") || editLname.getText().toString().equals("") || editCon.getText().toString().equals("") || editAddress.getText().toString().equals("") || editShop.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Fillup all the fields!", Toast.LENGTH_SHORT).show();
                } else if (verPass != "Y" || verEmail != "Y") {
                    Toast.makeText(getApplicationContext(), "Please check all the fields!", Toast.LENGTH_SHORT).show();
                }
                else if(verFinal == "Y") {
                    try {
                        // Toast.makeText(getApplicationContext(),verEmail+verFinal+verPass,Toast.LENGTH_SHORT).show();
                        db.setUsername(editUser.getText().toString());
                        db.setPassword(editPass.getText().toString());
                        db.setMyRePassword(editRepass.getText().toString());
                        db.setMyFname(editFname.getText().toString());
                //        db.setMyMname(editMname.getText().toString());
                        db.setMyLname(editLname.getText().toString());
                        db.setMyConNum(editCon.getText().toString());
                        db.setMyEmail(editShop.getText().toString());
                        db.setMyAddress(editAddress.getText().toString());
                        db.SaveVen();
                        verEmail = "";
                        verPass = "";
                        editUser.setText("");
                        editPass.setText("");
                        editRepass.setText("");
                        editShop.setText("");
                        editAddress.setText("");
                        editCon.setText("");
                        editLname.setText("");
                    //    editMname.setText("");
                        editFname.setText("");

                        Toast.makeText(venform.this, "Successfully Register!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(venform.this, e + " WASSUP NIGGA BOBOk KA", Toast.LENGTH_LONG).show();
                    }
                }
                else{Toast.makeText(venform.this, " KABOBOHAN!", Toast.LENGTH_LONG).show();}

            }

        });

    }
}
