package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class product extends AppCompatActivity {

    Bundle uname;
    String sname;
    Button btnAdd;
    EditText prodName, prodDesc, prodPrice;
    utilities util;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_product);

        prodName = (EditText)findViewById(R.id.editTitle);
        prodDesc = (EditText)findViewById(R.id.editDes);
        prodPrice = (EditText)findViewById(R.id.editPrice);
        btnAdd = (Button)findViewById(R.id.bntSignup);

        util = new utilities(this,null);

        uname= getIntent().getExtras();
        if(uname.equals("")){
            Toast.makeText(getApplicationContext(), "no vendor name", Toast.LENGTH_SHORT).show();
        }else {
            sname = uname.getString("uname");
       //     Toast.makeText(getApplicationContext(), sname, Toast.LENGTH_SHORT).show();
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (prodName.getText().toString().equals("") || prodDesc.getText().toString().equals("") || prodPrice.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Fillup all the fields dumbass!", Toast.LENGTH_SHORT).show();
                    } else {
                        util.setUsername(sname);
                        util.setProdName(prodName.getText().toString());
                        util.setProdDesc(prodDesc.getText().toString());
                        util.setProdPrice(prodPrice.getText().toString());
                        util.SaveProd();
                        Toast.makeText(product.this, "Successfully Added!", Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){Toast.makeText(getApplicationContext(),"Cannot connect to DB",Toast.LENGTH_SHORT).show();}

            }
        });
    }

}
