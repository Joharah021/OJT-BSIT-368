package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class reg extends AppCompatActivity {

    Button bntcus, bntven;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        getSupportActionBar().hide();

        bntcus = (Button) findViewById(R.id.bntcus);
        bntven = (Button) findViewById(R.id.bntven);
    }
    public void Bntcus(View v) {
        Intent create = new Intent(reg.this, Register.class);
        startActivity(create);
    }
    public void bnteven(View v) {
        Intent create = new Intent(reg.this, venform.class);
        startActivity(create);
    }


}
