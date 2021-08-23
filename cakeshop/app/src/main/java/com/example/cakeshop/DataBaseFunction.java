package com.example.cakeshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import org.apache.commons.math3.stat.descriptive.summary.Product;

import java.util.ArrayList;

public interface DataBaseFunction {
    /**
     *  Uses interface to create list of usable functions
     *  NOTE: used in DataBase class
     * */
    SQLiteDatabase initDataBase();
    void showToast(String message);
    boolean registerAccount(EditText...textfiels);
    boolean tryLogin(String username,String password);
    boolean usernameExist(String username);
    boolean emailExist(String email);
    boolean contactNumberExist(String contactnumber);

    CostumerData getCostumerById(String id);
    // product
    boolean productExist(String productname);
    long addProduct(String productname,String productdescription,String price,String stock);
    void addProductImage(String prod_id,String base64ImageString);
    String[][] getAllVendorStore();
    String[][] getAllProduct();
    String[] getProductImage(String id);
    ProductData getProductById(String productid);
    void onOrder(String useriD,ProductData productData,String payment_method,String status,int quantity,double servicefee,double deliveryfee);
    ArrayList<ProductData> getVendorProductsByVendorID(String vendorID);
    void onOrderAccepted(ProductData productData,int quantity);

    //creating your own cake
    void saveCreatedCake(String cname,String cshape,String cflavour,String csize,String cdescription);
    String[][] getCreatedCake();
    //recommendation
    boolean isAccountHasEmptyRecommendation();
    Context getContext();
    String getId();
    //vendor
    VendorData getVendorById(String id);
    void destroy();

}
