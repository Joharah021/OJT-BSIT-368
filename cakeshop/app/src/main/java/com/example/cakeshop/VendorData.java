package com.example.cakeshop;

import androidx.annotation.NonNull;

import java.util.HashMap;

class VendorData{
    /*
     * VendorData class | arranges data easily
     *
     * */
    private HashMap<String,String> data;
    public VendorData(){
        this.data = new HashMap<String, String>();
    }
    public void feed(@NonNull String key, String value){
        this.data.put(key,value);
    }
    public String get(String key){
        return this.data.get(key);
    }
    public boolean isEmpty(){
        //checks if VendorData is empty
        return (this.data.size()<=0)?true:false;
    }
}
