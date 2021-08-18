package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class transaction extends AppCompatActivity {

    Button bntprod;
    Bundle uname;
    String sname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().hide();
        bntprod = (Button) findViewById(R.id.bntprod);
        uname= getIntent().getExtras();
        sname = uname.getString("uname");
        Toast.makeText(getApplicationContext(),sname,Toast.LENGTH_SHORT).show();
    }

    public void bntCreate(View v) {
        Intent create = new Intent(transaction.this, product.class);
        create.putExtra("uname",sname);
        startActivity(create);
    }


}
