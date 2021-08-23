package com.example.cakeshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String vendorID;
    private ArrayList<HashMap<String, String>> list;
    private ListView productList;
    private SimpleAdapter adapter;
    private DataBase dbutilities;
    private HashMap<String,String> positionTracker;

    public VendorProductFragment(String _vendorID) {
        // accepts vendor id as parameter
        System.out.println("VENDOR ID INITIALIZED: "+_vendorID);
        this.vendorID= _vendorID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorProductFragment newInstance(String param1, String param2) {
        VendorProductFragment fragment = new VendorProductFragment(vendorID);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private void addVendorsProductToList(){
        /**
        * adds vendors product to list view based on store id
        **/
        ArrayList<ProductData> productData = this.dbutilities.getVendorProductsByVendorID(this.vendorID);
        System.out.println("PRODUCTDATA SIZE: "+productData.size());
        int counter = 0;
        for(ProductData pData:productData){
            HashMap<String,String> product = new HashMap<String, String>();
            this.positionTracker.put(Integer.toString(counter),pData.get("_id")); // product id and its position

            // line1 is for title
            product.put("line1",pData.get("Prod_Name"));
            // line2 is for subtitle
            product.put("line2",pData.get("Prod_Description"));
            this.list.add(product);
            this.adapter.notifyDataSetChanged();
            System.out.println("ADDED TO FRAGMENT LIST"+pData.get("Prod_Description"));
            counter++;
        }



    }
    private void initWidgets(View v){
        /**
         *
         *  initialize widgets eg: list
         *
         * */
        this.positionTracker = new HashMap<String, String>();
        this.dbutilities = new DataBase(getContext(),null);
        this.list = new ArrayList<HashMap<String, String>>();
        this.productList = (ListView) v.findViewById(R.id.productList);
        this.adapter = new SimpleAdapter(getContext(), list,R.layout.list_tile,new String[]{ "line1", "line2" },new int[]{ android.R.id.text1, android.R.id.text2 });
        this.productList.setAdapter(this.adapter);
        this.productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productviewer = new Intent(getContext(),ProductViewer.class);
                productviewer.putExtra("PRODUCTID",positionTracker.get(Integer.toString(position))); // get product id using its position on list
                startActivity(productviewer);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.vendor_product_fragment, container, false);
        this.initWidgets(v);
        this.addVendorsProductToList();
        return v;
    }

    @Override
    public void onDestroy() {
        // destroy database link always
        this.dbutilities.destroy();
        super.onDestroy();
    }
}
