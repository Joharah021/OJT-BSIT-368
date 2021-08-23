package com.example.cakeshop;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class VendorStore extends AppCompatActivity {
    TextView shopProfileName;
    TabLayout tabs ;
    ViewPager viewPager;
    PageAdapter adapter;
    DataBase dbutilities;
    String vendorid;
    VendorData vendorData;

    private void initWidgets(){
        /**
        * initialize widgets | intent extras
        **/
        this.vendorid = getIntent().getStringExtra("VENDORID");
        System.out.println("VENDOR STORE RECIEVES: "+this.vendorid);
        this.shopProfileName = (TextView) findViewById(R.id.shopProfileName);
        this.tabs = (TabLayout) findViewById(R.id.tabs);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.adapter = new PageAdapter(getSupportFragmentManager(),this.tabs.getTabCount());
        this.adapter.addStoreID(this.vendorid);
        this.viewPager.setAdapter(this.adapter);
        this.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
        this.dbutilities = new DataBase(this,null);
        this.vendorData = this.dbutilities.getVendorById(this.vendorid);

    }
    private void initTimeline(){
        //initialize timeline data | shopName
        this.shopProfileName.setText(this.vendorData.get("Usr_ShopName"));// sets shopname
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_store);
        this.initWidgets();
        this.initTimeline();
    }

    @Override
    protected void onDestroy() {
        this.dbutilities.destroy();
        super.onDestroy();
    }
}
