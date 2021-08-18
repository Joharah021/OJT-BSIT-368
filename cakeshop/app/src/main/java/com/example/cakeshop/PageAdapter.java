package com.example.cakeshop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private int numberoftabs;
    private  String storeid;
    public PageAdapter(FragmentManager fm,int _numberoftabs){
        super(fm,_numberoftabs);
        this.numberoftabs = _numberoftabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new VendorHomeFragment(this.storeid);
            case 1:
                return new VendorProductFragment(this.storeid);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberoftabs;
    }
    public void addStoreID(String _storeid){
        this.storeid = _storeid;
    }
}
