package com.example.cakeshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBase implements DataBaseFunction{

    private SQLiteDatabase database;
    private Context context;
    private  String id;



    public DataBase(Context context,String id){
        this.context = context;
        this.id = id;
        this.database = this.initDataBase();
    }
    @Override
    public SQLiteDatabase initDataBase(){
        this.database = this.context.openOrCreateDatabase("dbAccount12.db", Context.MODE_PRIVATE, null);
        // for regular users/customer
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblUsers(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Usr_Username VARCHAR(50),Usr_Password VARCHAR(50),Usr_Fname VARCHAR(50),Usr_Mname VARCHAR(50),Usr_Lname VARCHAR(50),Usr_Email VARCHAR(50),Usr_ConNum VARCHAR(50),Usr_Address VARCHAR(50))");
        // for created cake
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblcreatedcake(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id VARCHAR(11),cname VARCHAR(50),cshape VARCHAR(50),cflavour VARCHAR(50),csize VARCHAR(20),cdescription VARCHAR(255))");

        // for vendors
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblVendors(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Usr_Username VARCHAR(50),Usr_Password VARCHAR(50),Usr_Fname VARCHAR(50),Usr_Lname VARCHAR(50),Usr_Email VARCHAR(50),Usr_ShopName VARCHAR(50),Usr_ConNum VARCHAR(50),Usr_Address VARCHAR(50))");
        // for products
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblProducts(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Ven_ID VARCHAR(11),Prod_Name VARCHAR(50),Prod_Description VARCHAR(50),Prod_Price VARCHAR(50),Prod_Stock INTEGER)");
        // for product images
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblproductimg(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "user_id VARCHAR(11),product_id VARCHAR(11),image BLOB)");
        // for transaction
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblTransaction(_id INTEGER PRIMARY KEY AUTOINCREMENT,Ven_ID VARCHAR(11),Costumer_ID VARCHAR(11),Prod_ID VARCHAR(11),Qty VARCHAR(11))");
        // for orders
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblorders(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "user_id VARCHAR(11),product_id VARCHAR(11),payment_method VARCHAR(25),status VARCHAR(11),quantity VARCHAR(11),servicefee VARCHAR(11),deliveryfee VARCHAR(11))");
        // for orders record
        this.database.execSQL("CREATE TABLE IF NOT EXISTS tblorderrecords(_id INTEGER PRIMARY KEY AUTOINCREMENT,vendor_id INTEGER,month INTEGER,day INTEGER,year INTEGER,order_id INTEGER)");
        return  database;
    }
    @Override
    public void showToast(String message){
        /**
         * display's notification in a short period of time
         **/
        Toast.makeText(this.context,message,Toast.LENGTH_SHORT).show();
    }
    private String stringify(String s[]){
        String str="";
        for(String string:s){
            str+=string+"|";
        }
        return str;
    }
    @Override
    public boolean registerAccount(EditText...textfiels){
        /**
         *  converts EditText data into string | NOTE: order matters
         *  ORDER IF USER/CUSTOMER: editUser,editPass,editFname,editLname,editEmail,editCon,editAddress
         *  ORDER IF VENDOR: editUser,editPass,editFname,editLname,editEmail,editCon,editAddress,editShop
         * */
       try {
           String fields[] = new String[textfiels.length];
           for(int index = 0;index<textfiels.length;index++){
               fields[index] = textfiels[index].getText().toString();
               System.out.println("Saved Data ::>>"+fields[index]);
           }
           String QUERYCUSTOMER = "INSERT INTO tblUsers(Usr_Username,Usr_Password,Usr_Fname,Usr_Lname,Usr_Email,Usr_ConNum,Usr_Address) VALUES(?,?,?,?,?,?,?)";
           String QUERYVENDOR = "INSERT INTO tblVendors(Usr_Username,Usr_Password,Usr_Fname,Usr_Lname,Usr_Email,Usr_ConNum,Usr_Address,Usr_ShopName) VALUES(?,?,?,?,?,?,?,?)";
           System.out.println("Mode >> "+((TemporaryState.isCustomer)?QUERYCUSTOMER:QUERYVENDOR)+this.stringify(fields));
           this.database.execSQL((TemporaryState.isCustomer)?QUERYCUSTOMER:QUERYVENDOR,fields);
           return true;
       }catch (Exception e){
           this.showToast("DataBaseClassError > registerAccount: "+e.toString());
           return false;
       }

    }

    @Override
    public boolean tryLogin(String username, String password) {
        /**
         * tries to login on both account type: customer/vendor
         **/
        try {
            Cursor cursor;
            String QUERY = "SELECT * FROM tblUsers WHERE Usr_Username = ? AND Usr_Password = ?";
            cursor = this.database.rawQuery(QUERY,new String[]{username,password});
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                // if has 1 result | check if user exits as regular user | logged in succesfully
                TemporaryState.userID = cursor.getString(cursor.getColumnIndex("_id")).toString();
                TemporaryState.accountType = TemporaryState.USER;
                cursor.close();
                return true;
            }else{
                //checks if user exits as vendor
                QUERY = "SELECT * FROM tblVendors WHERE Usr_Username = ? AND Usr_Password = ?";
                cursor = this.database.rawQuery(QUERY,new String[]{username,password});
                cursor.moveToFirst();
                if(cursor.getCount() == 1){
                    TemporaryState.userID = cursor.getString(cursor.getColumnIndex("_id")).toString();
                    TemporaryState.accountType = TemporaryState.VENDOR;
                    cursor.close();
                    return true;
                }else{
                    // if result count has <0 or >1 | logged in unsuccessful | duplicate account
                    TemporaryState.userID = null;
                    TemporaryState.accountType = null;
                    cursor.close();
                    return false;
                }
            }
        }catch (Exception e){
            TemporaryState.userID = null;
            TemporaryState.accountType = null;
            this.showToast("DataBaseClassError > tryLogin: "+e.toString());
            return false;
        }
    }

    @Override
    public boolean usernameExist(String username){
        // check if username already exist in both user and vendors
        return (this.database.rawQuery("SELECT * FROM tblUsers WHERE Usr_Username = ?",new String[]{username}).getCount()>0 || this.database.rawQuery("SELECT * FROM tblVendors WHERE Usr_Username = ?",new String[]{username}).getCount()>0)?true:false;
    }
    @Override
    public boolean emailExist(String email){
        // check if email already exist in both user and vendors
        return (this.database.rawQuery("SELECT * FROM tblUsers WHERE Usr_Email = ?",new String[]{email}).getCount()>0 || this.database.rawQuery("SELECT * FROM tblVendors WHERE Usr_Email = ?",new String[]{email}).getCount()>0)?true:false;
    }
    @Override
    public boolean contactNumberExist(String contactnumber){
        // check if email already exist in both user and vendors
        return (this.database.rawQuery("SELECT * FROM tblUsers WHERE Usr_ConNum = ?",new String[]{contactnumber}).getCount()>0 || this.database.rawQuery("SELECT * FROM tblVendors WHERE Usr_ConNum = ?",new String[]{contactnumber}).getCount()>0)?true:false;
    }
    // For Product functions/methods
    @Override
    public boolean productExist(String productname) {
        //check if product is already exists in vendor's shop
        return (this.database.rawQuery("SELECT * FROM tblProducts WHERE _id = ? AND Prod_Name LIKE ?",new String[]{TemporaryState.userID,"%"+productname+"%"}).getCount()>0)?true:false;
    }

    // For Costumer
    @Override
    public CostumerData getCostumerById(String id){
        /**
         * Gets costumer data using vendor's id
         * requires vendor id as parameter
         **/
        System.out.println("COSTUMER ID: "+id);
        CostumerData costumerData = new CostumerData();
        String QUERY = "SELECT * FROM tblUsers WHERE _id = ?";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{id});
        boolean hasData = cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        if(hasData){
            int count=0;
            while (!cursor.isAfterLast()){
                for(int i=0;i<header.length;i++){
                    costumerData.feed(header[i],cursor.getString(cursor.getColumnIndex(header[i])));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return costumerData;
    }
    @Override
    public long addProduct(String productname, String productdescription, String price,String stock) {
        // adds product to current vendor
       try {
           //String INSERTQUERY = "INSERT INTO tblProducts(Ven_ID,Prod_Name,Prod_Description,Prod_Price) VALUES (?,?,?,?)";
           //long id = this.database.execSQL(INSERTQUERY,new String[]{TemporaryState.userID,productname,productdescription,price});
           ContentValues value = new ContentValues();
           value.put("Ven_ID",TemporaryState.userID);
           value.put("Prod_Name",productname);
           value.put("Prod_Description",productdescription);
           value.put("Prod_Price",price);
           value.put("Prod_Stock",stock);
           return this.database.insert("tblProducts",null,value);
       }catch (Exception e){
           this.showToast("DataBaseClassError > addProduct: "+e.toString());
       }
       return  -1;
    }

    @Override
    public void addProductImage(String prod_id,String base64ImageString) {
        try {
            System.out.println("Added base64 image -> "+base64ImageString.substring(0,5));
            String INSERTQUERY = "INSERT INTO tblproductimg(user_id,product_id,image) VALUES (?,?,?)";
            this.database.execSQL(INSERTQUERY,new String[]{TemporaryState.userID,prod_id,base64ImageString});
        }catch (Exception e){
            this.showToast("DataBaseClassError > addProductImage: "+e.toString());
        }
    }

    @Override
    public String[][] getAllVendorStore() {
        /**
        *  gets all registered store by the vendor
        * */
        String QUERY = "SELECT * FROM tblVendors";
        Cursor cursor = this.database.rawQuery(QUERY,null);
        boolean hasData = cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        String[][] vendors_data = new String[cursor.getCount()][header.length];
        if(hasData){
            // if number of rows is not 0
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i=0;i<header.length;i++){
                    vendors_data[count][i]=cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return vendors_data;
    }
    @Override
    public String[][] getAllProduct() {
        String QUERY = "SELECT * FROM tblProducts";
        Cursor cursor = this.database.rawQuery(QUERY,null);
        boolean hasData = cursor.moveToFirst();
        String[][] data = new String[cursor.getCount()][6];
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                data[count][0] = cursor.getString(cursor.getColumnIndex("_id"));
                data[count][1] = cursor.getString(cursor.getColumnIndex("Ven_ID"));
                data[count][2] = cursor.getString(cursor.getColumnIndex("Prod_Name"));
                data[count][3] = cursor.getString(cursor.getColumnIndex("Prod_Description"));
                data[count][4] = cursor.getString(cursor.getColumnIndex("Prod_Price"));
                data[count][5] = cursor.getString(cursor.getColumnIndex("Prod_Stock"));
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return data;
    }
    @Override
    public String[] getProductImage(String id){
        String QUERY   = "SELECT image FROM tblproductimg WHERE user_id = ? AND product_id = ?";
        Cursor cursor  = this.database.rawQuery(QUERY,new String[]{TemporaryState.userID,id});
        cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        String[] data = new String[cursor.getCount()];
        int count = 0;
        if(cursor.getCount()>0){
            while (!cursor.isAfterLast()){
                data[count] = cursor.getString(cursor.getColumnIndex("image"));
                cursor.moveToNext();
                count++;
            }
        }
        return data;
    }
    @Override
    public ProductData getProductById(String productid) {
        /**
         *  Gets product data using product id
         *  reuires product id as parameter
         **/
        ProductData productData = new ProductData();
        String QUERY = "SELECT * FROM tblProducts WHERE _id = ?";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{productid});
        boolean hasData = cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        if(hasData){
            while (!cursor.isAfterLast()){
                for(int i=0;i<header.length;i++){
                    productData.feed(header[i],cursor.getString(cursor.getColumnIndex(header[i])));
                }
                cursor.moveToNext();
            }
        }
        productData.addImage(this.getProductImage(productid));
        cursor.close();
        return productData;
    }
    @Override
    public ArrayList<ProductData> getVendorProductsByVendorID(String vendorID){
        System.out.println("VENDOR ID: "+vendorID);
        ArrayList<ProductData> productData = new ArrayList<ProductData>();
        String QUERY ="SELECT * FROM tblProducts WHERE Ven_ID = ?";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{vendorID});
        boolean hasData = cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        if(hasData){
            while (!cursor.isAfterLast()){
                ProductData data = new ProductData();
                for(String column : header) {
                    data.feed(column,cursor.getString(cursor.getColumnIndex(column)));
                }
                productData.add(data);
                cursor.moveToNext();
            }
            System.out.println("VENDOR ID NOT EMPTY DATA: "+vendorID);
        }else{
            System.out.println("VENDOR ID EMPTY DATA: "+vendorID);
        }
        return productData;
    }
    @Override
    public void saveCreatedCake(String cname, String cshape, String cflavour, String csize, String cdescription) {
        // tblcreatedcake
        String QUERY = "INSERT INTO tblcreatedcake(user_id,cname,cshape,cflavour,csize,cdescription) VALUES(?,?,?,?,?,?)";
        this.database.execSQL(QUERY,new String[]{TemporaryState.userID,cname,cshape,cflavour,csize,cdescription});
    }
    @Override
    public String[][] getCreatedCake() {
        String  QUERY = "SELECT * FROM tblcreatedcake WHERE user_id = ?";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{TemporaryState.userID});
        boolean hasData = cursor.moveToFirst();
        String [] headers = cursor.getColumnNames();
        String[][] data = new String[cursor.getCount()][headers.length];
        if(hasData){
            int countOuter;
            countOuter = 0;
            while (!cursor.isAfterLast()){
                for(int i = 0;i<headers.length;i++){
                    data[countOuter][i] = cursor.getString(cursor.getColumnIndex(headers[i]));
                    System.out.println(data[countOuter][i]);
                }
                cursor.moveToNext();
                countOuter++;
            }
        }
        cursor.close();
        return data;
    }

    @Override
    public void onOrder(String userId,ProductData productData,String payment_method,String status,int quantity,double servicefee,double deliveryfee) {
        String INSERTQUERYORDERS = "INSERT INTO tblorders(user_id,product_id,payment_method,status,quantity,servicefee,deliveryfee) VALUES(?,?,?,?,?,?,?)";
        this.database.execSQL(INSERTQUERYORDERS,new String[]{userId,productData.get("Ven_ID"),payment_method,status,Integer.toString(quantity),Double.toString(servicefee),Double.toString(deliveryfee)});
    }

    @Override
    public void onOrderAccepted(ProductData productData,int quantity) {
        int newstock = Integer.parseInt(productData.get("Prod_Stock")) - quantity;
        ContentValues cv = new ContentValues();
        cv.put("Prod_Stock",Integer.toString(newstock));
        this.database.update("tblProducts",cv,"_id = ?",new String[]{productData.get("_id")});
    }

    //For Product recommendation
    @Override
    public boolean isAccountHasEmptyRecommendation(){
        String QUERY = "SELECT * FROM tblRecommendation WHERE _id = ?";
        return (this.database.rawQuery(QUERY,new String[]{TemporaryState.userID}).getCount()<=0)?true:false;
    }

    // For Vendor
    @Override
    public VendorData getVendorById(String id){
        /**
        * Gets vendor data using vendor's id
        * requires vendor id as parameter
        **/
        System.out.println("VENDOR ID: "+id);
        VendorData vendorData = new VendorData();
        String QUERY = "SELECT * FROM tblVendors WHERE _id = ?";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{id});
        boolean hasData = cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        if(hasData){
            int count=0;
            while (!cursor.isAfterLast()){
                for(int i=0;i<header.length;i++){
                    vendorData.feed(header[i],cursor.getString(cursor.getColumnIndex(header[i])));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return vendorData;
    }

    //For transaction
    public String[][] getTransaction(String id){
        String TRANSACTIONQUERY = "SELECT * FROM tblTransaction WHERE Costumer_ID = ?";
        Cursor cursor = this.database.rawQuery(TRANSACTIONQUERY,new String[]{id});
        boolean hasData = cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()];
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return data;
    }
    // For orders
    public void updateOrderStatus(String orderid,String status)
    {
        String UPDATEQUERY = "UPDATE tblorders SET status = ? WHERE _id = ?";
        this.database.execSQL(UPDATEQUERY,new String[]{status,orderid});
    }
    // For orders
    public void updateOrderFees(String orderid,String servicefee,String deliveryfee)
    {
        String UPDATEQUERY = "UPDATE tblorders SET servicefee = ? ,deliveryfee = ? WHERE _id = ?";
        this.database.execSQL(UPDATEQUERY,new String[]{servicefee,deliveryfee,orderid});
    }
    // For orders
    public String[][] getOrdersByVendorId(String id,String filter1,String filter2)
    {
        //INSERT INTO tblorders(user_id,product_id,payment_method,status,quantity) VALUES(?,?,?,?,?)
        String QUERY  = "SELECT * FROM tblorders WHERE product_id = ? AND status IN (?,?)";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{id,filter1,filter2});
        boolean hasData = cursor.moveToFirst();
        String[] header = cursor.getColumnNames();
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()];
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return data;
    }
    // For orders
    public String[][] getOrdersByVendorId(String id,String filter)
    {
        //INSERT INTO tblorders(user_id,product_id,payment_method,status,quantity) VALUES(?,?,?,?,?)
        String QUERY  = "SELECT * FROM tblorders WHERE product_id = ? AND status = ?";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{id,filter})        ;
        boolean hasData = cursor.moveToFirst()                                       ;
        String[] header = cursor.getColumnNames()                                    ;
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()]     ;
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return data;
    }
    // For orders
    public String[][] getOrdersByCostumerId(String id)
    {
        //INSERT INTO tblorders(user_id,product_id,payment_method,status,quantity) VALUES(?,?,?,?,?)
        String QUERY  = "SELECT * FROM tblorders WHERE user_id = ?"                  ;
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{id})               ;
        boolean hasData = cursor.moveToFirst()                                       ;
        String[] header = cursor.getColumnNames()                                    ;
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()]     ;
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return data;
    }
    public String[] getOrderById(String orderid)
    {
        // id INTEGER PRIMARY KEY AUTOINCREMENT,"+
        //                "user_id VARCHAR(11),product_id VARCHAR(11),payment_method VARCHAR(25),status VARCHAR(11),quantity VARCHAR(11),servicefee VARCHAR(11),deliveryfee VARCHAR(11))
        String QUERY  = "SELECT * FROM tblorders WHERE _id = ?"                  ;
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{orderid})               ;
        boolean hasData = cursor.moveToFirst()                                       ;
        String[] header = cursor.getColumnNames()                                    ;
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()]     ;
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return data[0];
    }
    public void addTransactionHistory(String vendor_id,int month,int day,int year,String order_id)
    {
        // _id INTEGER PRIMARY KEY AUTOINCREMENT,vendor_id INTEGER,month INTEGER,day INTEGER,year INTEGER,order_id INTEGER
        String INSERTQUERY = "INSERT INTO tblorderrecords(vendor_id,month,day,year,order_id) VALUES(?,?,?,?,?)";
        String mm = Integer.toString(month);
        String dd = Integer.toString(day);
        String yy = Integer.toString(year);
        this.database.execSQL(INSERTQUERY,new String[]{vendor_id,mm,dd,yy,order_id});
    }

    public String[] getYearRecord(String ven_id)
    {
        String QUERY = "SELECT year FROM tblorderrecords WHERE vendor_id = ? GROUP BY year";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{ven_id})               ;
        boolean hasData = cursor.moveToFirst()                                       ;
        String[] header = cursor.getColumnNames()                                    ;
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()]     ;
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return (data.length ==1)?data[0]:new String[0];

    }
    public String[] getMonthRecord(String ven_id)
    {
        String QUERY = "SELECT month FROM tblorderrecords WHERE vendor_id = ? GROUP BY month";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{ven_id})               ;
        boolean hasData = cursor.moveToFirst()                                       ;
        String[] header = cursor.getColumnNames()                                    ;
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()]     ;
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return (data.length ==1)?data[0]:new String[0];
    }
    public String[][] getRecordsByVendorId(String ven_id,String mm,String yy)
    {
        // vendor_id INTEGER,month INTEGER,day INTEGER,year INTEGER,order_id INTEGER)
        String QUERY  = "SELECT * FROM tblorderrecords WHERE vendor_id = ? AND month = ? AND year = ?";
        Cursor cursor = this.database.rawQuery(QUERY,new String[]{ven_id,mm,yy})     ;
        boolean hasData = cursor.moveToFirst()                                       ;
        String[] header = cursor.getColumnNames()                                    ;
        String data[][] = new String[cursor.getCount()][cursor.getColumnCount()]     ;
        if(hasData){
            int count = 0;
            while (!cursor.isAfterLast()){
                for(int i  =0;i<header.length;i++){
                    data[count][i] = cursor.getString(cursor.getColumnIndex(header[i]));
                }
                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return data;
    }
    @Override
    public Context getContext(){
        return this.context;
    }
    @Override
    public String getId(){
        return this.id;
    }
    @Override
    public void destroy(){
        this.database.close();
    }
}
