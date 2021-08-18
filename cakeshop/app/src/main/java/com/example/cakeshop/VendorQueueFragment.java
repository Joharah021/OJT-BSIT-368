package com.example.cakeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class VendorQueueFragment extends Fragment {
    private ArrayList<HashMap<String, String>> list;
    private ListView acceptedtList                 ;
    private SimpleAdapter adapter                  ;
    private DataBase dbutilities                   ;
    private HashMap<String,HashMap<String,String>> positionTracker ;
    private void initWidgets(View v){
        /**
         *
         *  initialize widgets eg: list
         *
         **/
        this.positionTracker  = new HashMap<String,HashMap<String,String>>() ;
        this.dbutilities      = new DataBase(getContext(),null)          ;
        this.list             = new ArrayList<HashMap<String, String>>()     ;
        this.acceptedtList    = (ListView) v.findViewById(R.id.acceptedtList);
        this.adapter          = new SimpleAdapter(getContext(), list,R.layout.list_tile,new String[]{ "line1", "line2" },new int[]{ android.R.id.text1, android.R.id.text2 });
        this.acceptedtList.setAdapter(this.adapter);
        this.acceptedtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productviewer = new Intent(getContext(),Accepted_Order_Viewer.class);
                HashMap<String,String> args = positionTracker.get(Integer.toString(position));
                productviewer.putExtra("ORDERID",args.get("ORDERID"));
                productviewer.putExtra("COSTUMERID",args.get("COSTUMERID"));
                productviewer.putExtra("PRODUCTID",args.get("PRODUCTID"));
                productviewer.putExtra("PAYMENTMETHOD",args.get("PAYMENTMETHOD"));
                productviewer.putExtra("ORDERSTATUS",args.get("ORDERSTATUS"));
                productviewer.putExtra("QUANTITY",args.get("QUANTITY"));
                startActivity(productviewer);
            }
        });
    }
    private void getOrderList()
    {
        // [orderid,customer_id,product_id,payment_method,status,quantity]
        //     0        1           2            3           4      5
        String data[][] = this.dbutilities.getOrdersByVendorId(TemporaryState.userID,Status.ACCEPTED);
        int count       = 0;
        for(String s[]:data){
            HashMap<String,String> args = new HashMap<>()            ;
            args.put("ORDERID",s[0])                                 ;
            args.put("COSTUMERID",s[1])                              ;
            args.put("PRODUCTID",s[2])                               ;
            args.put("PAYMENTMETHOD",s[3])                           ;
            args.put("ORDERSTATUS",s[4])                             ;
            args.put("QUANTITY",s[5])                                ;
            this.positionTracker.put(Integer.toString(count),args)   ;
            String orderId   = s[0]                                  ;
            String productId = s[1]                                  ;
            System.out.println()                                     ;
            ProductData pData = this.dbutilities.getProductById(s[2]);
            HashMap<String,String> line = new HashMap<>()            ;
            line.put("line1",pData.get("Prod_Name"))                 ;
            line.put("line2","Quantity: "+s[5])                      ;
            this.list.add(line)                                      ;
            count++                                                  ;
        }                                                            ;
        this.adapter.notifyDataSetChanged()                          ;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.vendor_queue_fragment,container,false);
        this.initWidgets(v);
        this.getOrderList();
        return v;
    }
}
