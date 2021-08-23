package com.example.cakeshop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String vendorID;
    private DataBase dbutilities;
    private TextView storeOwner,storeName,storeAddress,storeContact;

    public VendorHomeFragment(String _vendorID) {
        // accepts store id as parameter
        this.vendorID = _vendorID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorHomeFragment newInstance(String param1, String param2) {
        VendorHomeFragment fragment = new VendorHomeFragment(vendorID);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void initWidgets(View v)
    {
        this.storeOwner = (TextView) v.findViewById(R.id.storeOwner);
        this.storeName = (TextView) v.findViewById(R.id.storeName);
        this.storeAddress = (TextView) v.findViewById(R.id.storeAddress);
        this.storeContact = (TextView) v.findViewById(R.id.storeContact);
        this.dbutilities = new DataBase(v.getContext(),null);
        VendorData vData = this.dbutilities.getVendorById(this.vendorID);

        //id INTEGER PRIMARY KEY AUTOINCREMENT," +
        //                "Usr_Username VARCHAR(50),Usr_Password VARCHAR(50),Usr_Fname VARCHAR(50),
        //                Usr_Lname VARCHAR(50),Usr_Email VARCHAR(50),
        //                Usr_ShopName VARCHAR(50),
        //                Usr_ConNum VARCHAR(50),
        //                Usr_Address VARCHAR(50))

        this.storeOwner.setText(vData.get("Usr_Lname") +" "+vData.get("Usr_Fname"));
        this.storeName.setText(vData.get("Usr_ShopName"));
        this.storeAddress.setText(vData.get("Usr_Address"));
        this.storeContact.setText(vData.get("Usr_ConNum"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.vendor_home_fragment, container, false);
        this.initWidgets(v);
        return v;
    }
}
