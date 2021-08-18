package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;




public class Records extends AppCompatActivity {
    private HashMap<String,String> monthMapper;
    private HashMap<String,String> monthMapperRev;
    private  DataBase dbutilities;
    private Spinner selectMonth,selectYear;
    private TextView revenueValue;
    private ListView recordList;
    private ArrayAdapter<String> adapterMonth,adapterYear;
    private ArrayList<String> monthStorage,yearStorage;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> list;
    private String mm;
    private String yy;
    private HashMap<String,String> mmTracker;
    private HashMap<String,String> yyTracker;
    double revenue;


    private  void initWidget()
    {

        this.revenue = 0.00;
        this.monthMapper  = new HashMap<>();
        this.monthMapper.put("0","Jan");
        this.monthMapper.put("1","Feb");
        this.monthMapper.put("2","Mar");
        this.monthMapper.put("3","Apr");
        this.monthMapper.put("4","May");
        this.monthMapper.put("5","Jun");
        this.monthMapper.put("6","Jul");
        this.monthMapper.put("7","Aug");
        this.monthMapper.put("8","Sep");
        this.monthMapper.put("9","Oct");
        this.monthMapper.put("10","Nov");
        this.monthMapper.put("11","Dec");

        this.monthMapperRev  = new HashMap<>();
        this.monthMapperRev.put("Jan","0");
        this.monthMapperRev.put("Feb","1");
        this.monthMapperRev.put("Mar","2");
        this.monthMapperRev.put("Apr","3");
        this.monthMapperRev.put("May","4");
        this.monthMapperRev.put("Jun","5");
        this.monthMapperRev.put("Jul","6");
        this.monthMapperRev.put("Aug","7");
        this.monthMapperRev.put("Sep","8");
        this.monthMapperRev.put("Oct","9");
        this.monthMapperRev.put("Nov","10");
        this.monthMapperRev.put("Dec","11");

        this.mm = this.monthMapper.get(Calendar.getInstance().MONTH);
        this.yy = Integer.toString(Calendar.getInstance().YEAR);
        this.dbutilities  = new DataBase(this,null);
        this.monthStorage = new ArrayList<String>();
        this.yearStorage  = new ArrayList<String>();
        this.selectMonth  = (Spinner) findViewById(R.id.selectMonth);
        this.selectYear   = (Spinner) findViewById(R.id.selectYear);
        this.revenueValue = (TextView) findViewById(R.id.revenueValue);
        this.recordList   = (ListView) findViewById(R.id.recordList);
        this.adapterMonth = new ArrayAdapter<>(Records.this, R.layout.spinner_item,this.monthStorage);
        this.adapterYear  = new ArrayAdapter<>(Records.this,  R.layout.spinner_item,this.yearStorage);
        this.selectMonth.setAdapter(this.adapterMonth);
        this.selectMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mm = mmTracker.get(Integer.toString(position));
                yy = yyTracker.get(Integer.toString(position));
                reset();
                getRecords(monthMapperRev.get(mm),yy);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.selectYear.setAdapter(this.adapterYear);
        this.selectYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mm = mmTracker.get(Integer.toString(position));
                yy = yyTracker.get(Integer.toString(position));
                reset();
                getRecords(monthMapperRev.get(mm),yy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.list = new ArrayList<HashMap<String, String>>();
        this.dbutilities = new DataBase(this,null);
        this.adapter = new SimpleAdapter(this, this.list,R.layout.list_tile,new String[]{ "line1", "line2" },new int[]{ android.R.id.text1, android.R.id.text2 });
        this.recordList.setAdapter(this.adapter);
        this.mmTracker = new HashMap<String, String>();
        this.yyTracker = new HashMap<String, String>();
        this.revenueValue.setText(Double.toString(this.revenue));
    }
    private void getRecords(String mm,String yy)
    {
        String [][] records = this.dbutilities.getRecordsByVendorId(TemporaryState.userID,mm,yy);
        System.out.println(records.length);
        // vendor_id INTEGER,month INTEGER,day INTEGER,year INTEGER,order_id INTEGER)
        for(String[] s:records)
        {
            // user_id VARCHAR(11),product_id VARCHAR(11),payment_method VARCHAR(25),status VARCHAR(11),quantity VARCHAR(11),servicefee VARCHAR(11),deliveryfee VARCHAR(11))

            String[] order = this.dbutilities.getOrderById(s[5]);
            ProductData productData = this.dbutilities.getProductById(order[1]);

            HashMap<String,String> line = new HashMap<>();
            line.put("line1",productData.get("Prod_Name"));
            System.out.println(productData.getKeys());
            double price = Double.parseDouble(productData.get("Prod_Price"));
            int quantity = Integer.parseInt(order[5]);
            double servicefee = Double.parseDouble(order[6]);
            double deliveryfee = Double.parseDouble(order[7]);
            double total = (price*quantity)+(servicefee+deliveryfee);
            line.put("line2","Total: "+total);
            this.list.add(line);
            this.revenue += total;
        }
        this.adapter.notifyDataSetChanged();
        this.revenueValue.setText(Double.toString(this.revenue));
    }
    private void reset()
    {
        this.revenue = 0.00;
        this.list.clear();
        this.adapter.notifyDataSetInvalidated();
        this.adapter.notifyDataSetChanged();
    }
    private  void addContent()
    {
        String[] month = this.dbutilities.getMonthRecord(TemporaryState.userID);
        int mmIndex = 0;
        for(String mm:month)
        {
            this.monthStorage.add(this.monthMapper.get(mm));
            this.mmTracker.put(Integer.toString(mmIndex),this.monthMapper.get(mm));
            mmIndex++;
        }
        String[] year = this.dbutilities.getYearRecord(TemporaryState.userID);
        int yyIndex = 0;
        for(String yy:year)
        {
            this.yearStorage.add(yy);
            this.yyTracker.put(Integer.toString(yyIndex),yy);
            yyIndex++;
        }

        this.adapterMonth.notifyDataSetChanged();
        this.adapterYear.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        this.initWidget();
        this.addContent();

    }
}
