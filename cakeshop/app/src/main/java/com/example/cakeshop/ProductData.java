package com.example.cakeshop;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class ProductData {
    /*
     * ProductData class | arranges data easily
     *
     * */
    private HashMap<String,String> data;
    private String[] images;
    public ProductData(){
        this.data = new HashMap<String, String>();
    }
    public void feed(@NonNull String key, String value){
        this.data.put(key,value);
    }
    public void addImage(String[] images){
        this.images = images;
    }
    public String[] getImages(){
        return this.images;
    }
    public String get(String key){
        return this.data.get(key);
    }
    public boolean isEmpty(){
        //checks if VendorData is empty
        return (this.data.size()<=0)?true:false;
    }
    public String getKeys()
    {
        String k = "";
        for(String s:this.data.keySet())
        {
            k += s+" --- ";
        }
        return  k;
    }

}
