package com.example.cakeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class RecommendedList extends AppCompatActivity {
    private ListView listView;
    private SimpleAdapter adapter;
    private DataBase dbutilities;
    private ArrayList<HashMap<String, String>> list;
    private HashMap<String,String> positionTracker;

    private  void  initWidgets(){
        /**
         *
         *  initialize widgets eg: views | list | adapter
         *
         * */
        this.list = new ArrayList<HashMap<String, String>>();
        this.positionTracker = new HashMap<String, String>();
        this.dbutilities = new DataBase(this,null);
        this.listView = (ListView) findViewById(R.id.orderLists);
        TextView text1 = findViewById(android.R.id.text1);
        this.adapter = new SimpleAdapter(this, this.list,R.layout.list_tile,new String[]{ "line1", "line2" },new int[]{ android.R.id.text1, android.R.id.text2 });
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productviewer = new Intent(RecommendedList.this,ProductViewer.class);
                productviewer.putExtra("PRODUCTID",positionTracker.get(Integer.toString(position))); // get product id using its position on list
                startActivity(productviewer);
            }
        });
    }

    private void getRecommendation(){
        /**
        * gets all product from recommendation
        * */
        String[][] data = this.dbutilities.getAllProduct();
        for(int i=0;i<data.length;i++){
            this.positionTracker.put(Integer.toString(i),data[i][0]); // product id and its position
            HashMap<String , String> map = new HashMap<String, String>();
            map.put("line1",data[i][2]); // product name
            map.put("line2",data[i][3]); // product description
            this.list.add(map);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_list);
        this.initWidgets();
        this.getRecommendation();

    }
    @Override
    protected void onDestroy() {
        this.dbutilities.destroy();
        super.onDestroy();
    }
}
