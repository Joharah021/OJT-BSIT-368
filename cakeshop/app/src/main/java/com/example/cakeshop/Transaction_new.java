package com.example.cakeshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Transaction_new extends AppCompatActivity {
    private ListView listView;
    private SimpleAdapter adapter;
    private DataBase dbutilities;
    private ArrayList<HashMap<String, String>> list;
    private HashMap<String,HashMap<String,String>> positionTracker;

    private  void  initWidgets(){
        /**
         *
         *  initialize widgets eg: views | list | adapter
         *
         * */
        this.list = new ArrayList<HashMap<String, String>>();
        this.positionTracker = new HashMap<String,HashMap<String,String>> ();
        this.dbutilities = new DataBase(this,null);
        this.listView = (ListView) findViewById(R.id.transactionListView);
        TextView text1 = findViewById(android.R.id.text1);
        this.adapter = new SimpleAdapter(this, this.list,R.layout.list_tile,new String[]{ "line1", "line2" },new int[]{ android.R.id.text1, android.R.id.text2 });
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productviewer = new Intent(Transaction_new.this,TransactionViewer.class);
                HashMap<String,String> args = positionTracker.get(Integer.toString(position));
                productviewer.putExtra("ORDERID",args.get("ORDERID"));
                productviewer.putExtra("COSTUMERID",args.get("COSTUMERID"));
                productviewer.putExtra("PRODUCTID",args.get("PRODUCTID"));
                productviewer.putExtra("PAYMENTMETHOD",args.get("PAYMENTMETHOD"));
                productviewer.putExtra("ORDERSTATUS",args.get("ORDERSTATUS"));
                productviewer.putExtra("QUANTITY",args.get("QUANTITY"));
                productviewer.putExtra("SERVICEFEE",args.get("SERVICEFEE"));
                productviewer.putExtra("DELIVERYFEE",args.get("DELIVERYFEE"));
                startActivity(productviewer);
            }
        });
    }

    private void getOrderList(){
        /**
         * gets all product from recommendation
         * */
        String[][] data = this.dbutilities.getOrdersByCostumerId(TemporaryState.userID);
        int count = 0;
        for(String s[]:data) {
            HashMap<String,String> args = new HashMap<>()            ;
            args.put("ORDERID",s[0])                                 ;
            args.put("COSTUMERID",s[1])                              ;
            args.put("PRODUCTID",s[2])                               ;
            args.put("PAYMENTMETHOD",s[3])                           ;
            args.put("ORDERSTATUS",s[4])                             ;
            args.put("QUANTITY",s[5])                                ;
            args.put("SERVICEFEE",s[6])                              ;
            args.put("DELIVERYFEE",s[7])                             ;
            this.positionTracker.put(Integer.toString(count),args)   ;
            String orderId   = s[0]                                  ;
            String productId = s[1]                                  ;
            System.out.println()                                     ;
            ProductData pData = this.dbutilities.getProductById(s[2]);
            HashMap<String,String> line = new HashMap<>()            ;
            line.put("line1",pData.get("Prod_Name"))                 ;
            line.put("line2","Status: "+s[4])                        ;
            this.list.add(line)                                      ;
            count++                                                  ;
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);
        this.initWidgets();
        this.getOrderList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
