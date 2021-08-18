package com.example.cakeshop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class VendorOrderAndQueueAdapter extends FragmentPagerAdapter {
    private int numberoftabs;
    private  String storeid;
    public VendorOrderAndQueueAdapter(FragmentManager fm, int _numberoftabs){
        super(fm,_numberoftabs);
        this.numberoftabs = _numberoftabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new VendorOrederFragment();
            case 1:
                return new VendorQueueFragment();
            case 2:
                return new VendorVerifyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberoftabs;
    }
}
