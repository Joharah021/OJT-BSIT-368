package com.example.cakeshop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class VendorOrders_Queue_Verify extends AppCompatActivity {
    TabLayout tabs ;
    ViewPager viewPager;
    VendorOrderAndQueueAdapter adapter;

    private void initWidgets(){
        /**
         * initialize widgets | intent extras
         **/
        this.tabs = (TabLayout) findViewById(R.id.tabs);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.adapter = new VendorOrderAndQueueAdapter(getSupportFragmentManager(),this.tabs.getTabCount());
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_orders_and_queue);
        this.initWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
