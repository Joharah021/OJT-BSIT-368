package com.example.cakeshop;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.widget.Toast;

public class utilities {

    public utilities() {}

    private Context myContext;
    private String accType;
    private String myID;
    private String myUsername;
    private String myPassword;
    private String myRePassword;
    private String myFname;
    private String myMname;
    private String myLname;
    private String myEmail;
    private String myConNum;
    private String myAddress;
    private String prodName;
    private String prodDesc;
    private String prodPrice;

    public void setProdName(String value){prodName = value;}
    public String getProdName(){return prodName;}
    public void setProdDesc(String value){prodDesc = value;}
    public String getProdDesc(){return prodDesc;}
    public void setProdPrice(String value){prodPrice = value;}
    public String getProdPrice(){return prodPrice;}

    public void setUsername(String value){myUsername = value;}
    public String getUsername(){return myUsername;}
    public void setPassword(String value){myPassword = value;}
    public String getPassword(){return myPassword;}
    public void setMyRePassword(String value){myRePassword = value;}
    public String getMyRePassword(){return myRePassword;}
    public void setMyFname(String value){myFname = value;}
    public String getMyFname(){return myFname;}
    public void setMyMname(String value){myMname = value;}
    public String getMyMname(){return myMname;}
    public void setMyLname(String value){myLname = value;}
    public String getMyLname(){return myLname;}
    public void setMyEmail(String value){myEmail = value;}
    public String getMyEmail(){return myEmail;}
    public void setMyConNum(String value){myConNum = value;}
    public String getMyConNum(){return myConNum;}
    public void setMyAddress(String value){myAddress = value;}
    public String getMyAddress(){return myAddress;}
    public void setAccType(String value){accType = value;}
    public String getAccType(){return accType;}


    public void setContext(Context value){myContext = value;}
    public Context getContext(){return myContext;}
    public void setID(String value){myID = value;}
    public String getID(){return myID;}

   // public utilities() {}

    public utilities(Context context, String id){
        if (context != null)
            setContext(context);
        if (id != null)
            setID(id);
    }


    public SQLiteDatabase OpenDB(){
        SQLiteDatabase sqlDB = null;
        sqlDB = this.getContext().openOrCreateDatabase("dbAccount.db", Context.MODE_PRIVATE, null);
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS tblUsers(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Usr_Username VARCHAR(50),Usr_Password VARCHAR(50),Usr_RePass VARCHAR(50),Usr_Fname VARCHAR(50),Usr_Mname VARCHAR(50),Usr_Lname VARCHAR(50),Usr_Email VARCHAR(50),Usr_ConNum VARCHAR(50),Usr_Address VARCHAR(50))");
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS tblVendors(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Usr_Username VARCHAR(50),Usr_Password VARCHAR(50),Usr_RePass VARCHAR(50),Usr_Fname VARCHAR(50),Usr_Lname VARCHAR(50),Usr_ShopName VARCHAR(50),Usr_ConNum VARCHAR(50),Usr_Address VARCHAR(50))");
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS tblProducts(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Ven_Name VARCHAR(50),Prod_Name VARCHAR(50),Prod_Description VARCHAR(50),Prod_Price VARCHAR(50))");
        return sqlDB;
    }

    public void Save(){
        SQLiteDatabase sqLiteDatabase = OpenDB();
        if (this.getID() == null){
            String SQL = "INSERT INTO tblUsers(Usr_Username,Usr_Password,Usr_RePass,Usr_Fname,Usr_Mname,Usr_Lname,Usr_ConNum,Usr_Address,Usr_Email) VALUES(?,?,?,?,?,?,?,?,?)";
            sqLiteDatabase.execSQL(SQL,
                    new String[] {this.getUsername(), this.getPassword(), this.getMyRePassword(),this.getMyFname(),this.getMyMname(),this.getMyLname(),this.getMyConNum(),this.getMyAddress(),this.getMyEmail()});
        }else{
            String SQL = "UPDATE tblUsers1 SET Usr_Username = ?, Usr_Password = ? WHERE _id = ?";
            sqLiteDatabase.execSQL(SQL,
                    new String[] {this.getUsername(), this.getPassword(), this.getID()});
        }
        sqLiteDatabase.close();
    }
    public void SaveProd(){
        SQLiteDatabase sqLiteDatabase = OpenDB();
        if (this.getID() == null){
            String SQL = "INSERT INTO tblProducts(Ven_Name,Prod_Name,Prod_Description,Prod_Price) VALUES(?,?,?,?)";
            sqLiteDatabase.execSQL(SQL,
                    new String[] {this.getUsername(), this.getProdName(), this.getProdDesc(),this.getProdPrice()});
        }else{
            String SQL = "UPDATE tblUsers1 SET Usr_Username = ?, Usr_Password = ? WHERE _id = ?";
            sqLiteDatabase.execSQL(SQL,
                    new String[] {this.getUsername(), this.getPassword(), this.getID()});
        }
        sqLiteDatabase.close();
    }
    public void SaveVen(){
        SQLiteDatabase sqLiteDatabase = OpenDB();
        if (this.getID() == null){
            String SQL = "INSERT INTO tblVendors(Usr_Username,Usr_Password,Usr_RePass,Usr_Fname,Usr_Lname,Usr_ConNum,Usr_Address,Usr_Shopname) VALUES(?,?,?,?,?,?,?,?)";
            sqLiteDatabase.execSQL(SQL,
                    new String[] {this.getUsername(), this.getPassword(), this.getMyRePassword(),this.getMyFname(),this.getMyLname(),this.getMyConNum(),this.getMyAddress(),this.getMyEmail()});
        }else{
            String SQL = "UPDATE tblVendors SET Usr_Username = ?, Usr_Password = ? WHERE _id = ?";
            sqLiteDatabase.execSQL(SQL,
                    new String[] {this.getUsername(), this.getPassword(), this.getID()});
        }
        sqLiteDatabase.close();
    }

    public SimpleCursorAdapter GetAllRecords(){
        SimpleCursorAdapter cursorAdapter = null;
        SQLiteDatabase sqLiteDatabase = OpenDB();
        String SQL = "SELECT * FROM tblVendors WHERE Usr_Address LIKE ?";
        String wild = "%" + this.getMyAddress() + "%";
        Cursor cursor = sqLiteDatabase.rawQuery(SQL, new String[]{wild});
        cursorAdapter = new SimpleCursorAdapter(this.getContext(),
                android.R.layout.simple_expandable_list_item_2, cursor,
                new String[]{"Usr_ShopName", "Usr_Address"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);

        return cursorAdapter;
    }
public Cursor getProduct()
{
    SQLiteDatabase sqLiteDatabase = OpenDB();
    String SQL = "SELECT * FROM tblProducts  ";
    Cursor cursor = sqLiteDatabase.rawQuery(SQL, null);
    cursor.moveToFirst();
    if(cursor.getCount() > 0) {
        this.setUsername(cursor.getString(cursor.getColumnIndex("Ven_Name")));
        this.setPassword(cursor.getString(cursor.getColumnIndex("Prod_Name")));
    }


   return cursor;

}
    public void getRecord()
    {
        SQLiteDatabase sqLiteDatabase = OpenDB();
        String SQL = "SELECT * FROM tblUsers WHERE Usr_Username = ? ";
        Cursor cursor = sqLiteDatabase.rawQuery(SQL, new String[]{this.getUsername()});
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            this.setUsername(cursor.getString(cursor.getColumnIndex("Usr_Username")));
            this.setPassword(cursor.getString(cursor.getColumnIndex("Usr_Password")));
            this.setAccType("1");
        }
        else if(cursor.getCount() == 0) {
            String SQL1 = "SELECT * FROM tblVendors WHERE Usr_Username = ? ";
            Cursor cursor1 = sqLiteDatabase.rawQuery(SQL1, new String[]{this.getUsername()});
            cursor1.moveToFirst();
            if(cursor1.getCount() > 0) {
                this.setUsername(cursor1.getString(cursor1.getColumnIndex("Usr_Username")));
                this.setPassword(cursor1.getString(cursor1.getColumnIndex("Usr_Password")));
                this.setAccType("2");
        }else this.setUsername("N");
            cursor1.close();
            cursor1 = null;
        }
        else this.setUsername("N");
        cursor.close();
        cursor = null;

    }
}
