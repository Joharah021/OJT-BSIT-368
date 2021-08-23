package com.example.cakeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Recommendation extends AppCompatActivity {

    Bundle bundle;
    String vend;
    utilities util;
    ArrayList<String> arrayList,arrayList1,arrayList2;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recommendation);
        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        listView = (ListView)findViewById(R.id.orderLists);
        util = new utilities(Recommendation.this,null);

        bundle = getIntent().getExtras();
        vend = bundle.getString("vend");

test();
CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }
    public void test()
    {

        try{
            arrayList1.clear();
            arrayList.clear();
            arrayList2.clear();
          //  util = new utilities(Recommendation.this,null);
            String SQL = "SELECT * FROM tblProducts WHERE Ven_Name = ?";
            Cursor cursor = util.OpenDB().rawQuery(SQL, new String[]{vend});
            cursor.moveToFirst();
            Toast.makeText(getApplicationContext(),vend,Toast.LENGTH_LONG).show();
            if (cursor.getCount() == 0)
             {

                Toast.makeText(Recommendation.this,  cursor.getCount() + vend + " test", Toast.LENGTH_LONG).show();
             }
            else {
            do {
             //   Toast.makeText(Recommendation.this, vend + " newtest", Toast.LENGTH_LONG).show();
                String adds = (cursor.getString(cursor.getColumnIndex("Prod_Name")));
                String shops = (cursor.getString(cursor.getColumnIndex("Prod_Description")));
                String prices = (cursor.getString(cursor.getColumnIndex("Prod_Price")));
               //    Toast.makeText(Recommendation.this, adds + "hmm " + shops + prices, Toast.LENGTH_LONG).show();
                arrayList.add(adds);
                arrayList1.add(shops);
                arrayList2.add(prices);

            }while(cursor.moveToNext());
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),  " Cant connect to DB", Toast.LENGTH_SHORT).show();
        }
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        class ViewHolder {
           // ImageView face;
            TextView Title;
            TextView Price;
            TextView Description;

            ViewHolder(View view) {
                //  face = (ImageView) view.findViewById(R.id.imageView2);
                Title = view.findViewById(R.id.txtName);
                Description = view.findViewById(R.id.txtDesc);
                Price = view.findViewById(R.id.txtPrice);
            }
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listadapter, parent, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            //   viewHolder.face.setImageResource(image[i]);
            viewHolder.Title.setText(arrayList.get(position));
            viewHolder.Price.setText(arrayList2.get(position));
            viewHolder.Description.setText(arrayList1.get(position));
            return view;
        }
    }
}
