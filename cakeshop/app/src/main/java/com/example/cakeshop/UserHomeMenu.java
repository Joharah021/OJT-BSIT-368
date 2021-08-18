package com.example.cakeshop;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class UserHomeMenu extends AppCompatActivity{

    private Button bntcreate, bntrecom,bntnear,bnttrans;

    private void initWidgets(){
        /**
         *
         *  initialize widgets eg: buttons
         *
         **/
        bntcreate = (Button) findViewById(R.id.bntcreate);
        bntrecom = (Button) findViewById(R.id.bntrecom);
        bntnear = (Button) findViewById(R.id.bntnear);
        bnttrans = (Button) findViewById(R.id.bnttrans);
    }
    private void showToast(String message){
        /**
         * display's notification in a short period of time
         **/
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void onCreateClick(View v){
        // implement activity
        //this.showToast("To create");
        Intent toCreateCake = new Intent(UserHomeMenu.this,CreateYourCake.class);
        startActivity(toCreateCake);
    }
    private void onRecommendedClick(View v){
        Intent toRecommendedList = new Intent(UserHomeMenu.this,RecommendedList.class);
        startActivity(toRecommendedList);
    }
    private void onNearbyClick(View v){
        // implement activity
        //this.showToast("To nearby");
        if(this.isLocationEnabled(getApplicationContext())){
            Intent mapsearch = new Intent(UserHomeMenu.this,MapSearch.class);
            startActivity(mapsearch);
        }else{
            this.showToast("Please enable location!");
        }

    }
    private void onTransactionClick(View v){
        // implement activity
        //this.showToast("To transaction");
        Intent toTransaction = new Intent(UserHomeMenu.this,Transaction_new.class);
        startActivity(toTransaction);
    }

    private void addListeners(){
        /**
         *
         *  adds action event listener to widgets eg: click
         *
         **/
        this.bntcreate.setOnClickListener(this::onCreateClick);
        this.bntrecom.setOnClickListener(this::onRecommendedClick);
        this.bntnear.setOnClickListener(this::onNearbyClick);
        this.bnttrans.setOnClickListener(this::onTransactionClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_menu);
        this.initWidgets();
        this.addListeners();
    }
    @SuppressWarnings("deprecation")
    private boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This was deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
