package com.example.cakeshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderViewer extends AppCompatActivity {

    private  TextView oFPersonName,oAddValue,oContactValue,oOrderValue,oOrderStockValue,oOrderPriceValue,oQtyValue,oPaymenMethodValue,oOrderSubtotalValue;
    private Button btnAccept,btnDecline;
    private Thread t1;
    private ImageView ImagesSlide;
    private  String productID;
    private CostumerData costumerData;
    private ProductData productData;
    private DataBase dbutilities;
    private boolean running;
    private int index;
    private  String selectedOp;

    private void initWidgets(){
        /*
         *  initialize TextView widgets | initialize data
         * */
        this.dbutilities   = new DataBase(this,null);
        this.ImagesSlide   = (ImageView) findViewById(R.id.ImagesSlide);

        this.productID     = getIntent().getStringExtra("PRODUCTID");
        this.costumerData  = this.dbutilities.getCostumerById(getIntent().getStringExtra("COSTUMERID"));
        this.productData   = this.dbutilities.getProductById(this.productID);

        this.oFPersonName  = (TextView) findViewById(R.id.oFPersonName);
        this.oAddValue     = (TextView) findViewById(R.id.oAddValue);
        this.oContactValue = (TextView) findViewById(R.id.oContactValue);
        this.oOrderValue   = (TextView) findViewById(R.id.oOrderValue);
        this.oOrderStockValue   = (TextView) findViewById(R.id.oOrderStockValue);
        this.oOrderPriceValue   = (TextView) findViewById(R.id.oOrderPriceValue);
        this.oQtyValue          = (TextView) findViewById(R.id.oQtyValue);
        this.oPaymenMethodValue = (TextView) findViewById(R.id.oPaymenMethodValue);
        this.oOrderSubtotalValue= (TextView) findViewById(R.id.oOrderSubtotalValue);
        this.btnAccept     = (Button) findViewById(R.id.btnAction);
        this.btnDecline    = (Button) findViewById(R.id.btnDecline);
        this.running       = true;
        this.index         = 0;
        this.ImagesSlide.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    private void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG);
    }
    private void onAccept(View v)
    {
        this.dbutilities.updateOrderStatus(getIntent().getStringExtra("ORDERID"),Status.ACCEPTED);
        finish();
    }
    private void onDecline(View v)
    {
        this.dbutilities.updateOrderStatus(getIntent().getStringExtra("ORDERID"),Status.DECLINED);
        finish();
    }
    private void setTextViewText(){
        /*
         * initialize textview text|label
         * */

        // oFPersonName,oAddValue,oContactValue,oOrderValue,oOrderStockValue,oQtyValue;
        // _id INTEGER PRIMARY KEY AUTOINCREMENT," +
        //                "Ven_ID VARCHAR(11),Prod_Name VARCHAR(50),Prod_Description VARCHAR(50),Prod_Price VARCHAR(50),Prod_Stock INTEGER)
        //id INTEGER PRIMARY KEY AUTOINCREMENT," +
        //                "Usr_Username VARCHAR(50),Usr_Password VARCHAR(50),Usr_Fname VARCHAR(50),Usr_Mname VARCHAR(50),
        //                Usr_Lname VARCHAR(50),Usr_Email VARCHAR(50),Usr_ConNum VARCHAR(50),Usr_Address VARCHAR(50))

        this.oFPersonName.setText(this.costumerData.get("Usr_Username"))     ;
        this.oAddValue.setText(this.costumerData.get("Usr_Address"))         ;
        this.oContactValue.setText(this.costumerData.get("Usr_ConNum"))      ;
        this.oOrderValue.setText(this.productData.get("Prod_Name"))          ;
        this.oOrderStockValue.setText(this.productData.get("Prod_Stock"))    ;
        this.oOrderPriceValue.setText(this.productData.get("Prod_Price"))    ;
        this.oPaymenMethodValue.setText(getIntent().getStringExtra("PAYMENTMETHOD"));
        this.oOrderSubtotalValue.setText(this.productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+" = "+Double.toString(Double.parseDouble(this.productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY"))));
        this.oQtyValue.setText(getIntent().getStringExtra("QUANTITY")) ;
        this.btnAccept.setOnClickListener(this::onAccept)                    ;
        this.btnDecline.setOnClickListener(this::onDecline)                  ;
        String[] images = this.productData.getImages()                       ;

        if(images != null){
            this.t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] imgbytes     = Base64.decode(images[index],Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imgbytes, 0, imgbytes.length);
                                ImagesSlide.setImageBitmap(decodedImage);
                            }
                        });
                        try {
                            Thread.sleep(3000);
                        }catch (Exception ex){}
                        index++;
                        if(index == (images.length)){
                            index = 0;
                        }
                    }
                }
            });
            t1.start();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_viewer);
        this.initWidgets();
        this.setTextViewText();
    }

    @Override
    protected void onDestroy() {
        this.running = false;
        this.dbutilities.destroy();
        super.onDestroy();
    }
}
