package com.example.cakeshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class activity_verify_viewer extends AppCompatActivity {
    private LinearLayout orderDeliveryfeeGroup;
    private TextView oFPersonName,oAddValue,oContactValue,oOrderValue,oOrderStockValue,oOrderPriceValue,oQtyValue,oServicefeeValue,oPaymenMethodValue,oOrderSubtotalValue,oDeliveryfee;
    private Button btnAction;
    private Thread t1;
    private ImageView ImagesSlide;
    private  String productID;
    private CostumerData costumerData;
    private ProductData productData;
    private DataBase dbutilities;
    private boolean running;
    private int index;
    private  String selectedOp;
    private  String deliveryfee;

    private void initWidgets(){
        /**
         *  initialize TextView widgets | initialize data
         **/
        this.dbutilities   = new DataBase(this,null)            ;
        this.ImagesSlide   = (ImageView) findViewById(R.id.ImagesSlide)     ;
        this.productID     = getIntent().getStringExtra("PRODUCTID") ;
        this.costumerData  = this.dbutilities.getCostumerById(getIntent().getStringExtra("COSTUMERID"));
        this.productData   = this.dbutilities.getProductById(this.productID);
        this.orderDeliveryfeeGroup = (LinearLayout) findViewById(R.id.orderDeliveryfeeGroup);
        this.oFPersonName  = (TextView) findViewById(R.id.oFPersonName)             ;
        this.oAddValue     = (TextView) findViewById(R.id.oAddValue)                ;
        this.oContactValue = (TextView) findViewById(R.id.oContactValue)            ;
        this.oOrderValue   = (TextView) findViewById(R.id.oOrderValue)              ;
        this.oOrderStockValue    = (TextView) findViewById(R.id.oOrderStockValue)   ;
        this.oOrderPriceValue    = (TextView) findViewById(R.id.oOrderPriceValue)   ;
        this.oQtyValue           = (TextView) findViewById(R.id.oQtyValue)          ;
        this.oServicefeeValue    = (TextView) findViewById(R.id.oServicefeeValue)   ;
        this.oPaymenMethodValue  = (TextView) findViewById(R.id.oPaymenMethodValue) ;
        this.oDeliveryfee        = (TextView) findViewById(R.id.oDeliveryfee)       ;
        this.oOrderSubtotalValue = (TextView) findViewById(R.id.oOrderSubtotalValue);
        this.btnAction     = (Button) findViewById(R.id.btnAction)                  ;
        this.running       = true  ;
        this.index         = 0     ;
        this.deliveryfee   = "0.00";
        this.ImagesSlide.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    private void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG);
    }

    private void ifSuccessful(View v)
    {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year  = Calendar.getInstance().get(Calendar.YEAR);
        this.dbutilities.updateOrderStatus(getIntent().getStringExtra("ORDERID"),Status.SUCCESS);
        this.dbutilities.onOrderAccepted(this.productData,Integer.parseInt(getIntent().getStringExtra("QUANTITY")));
        this.dbutilities.addTransactionHistory(TemporaryState.userID,month,day,year,getIntent().getStringExtra("ORDERID"));
        finish();
    }

    private void setTextViewText(){
        /**
         *initialize textview text|label
         **/
        this.oFPersonName.setText(this.costumerData.get("Usr_Username"))     ;
        this.oAddValue.setText(this.costumerData.get("Usr_Address"))         ;
        this.oContactValue.setText(this.costumerData.get("Usr_ConNum"))      ;
        this.oOrderValue.setText(this.productData.get("Prod_Name"))          ;
        this.oOrderStockValue.setText(this.productData.get("Prod_Stock"))    ;
        this.oOrderPriceValue.setText(this.productData.get("Prod_Price"))    ;
        this.oQtyValue.setText(getIntent().getStringExtra("QUANTITY"));
        this.oServicefeeValue.setText(Double.toString(TemporaryState.Servicefee));
        this.oPaymenMethodValue.setText(getIntent().getStringExtra("PAYMENTMETHOD"));

        if(getIntent().getStringExtra("ORDERSTATUS").equals(Status.ON_ITS_WAY))
        {
            this.orderDeliveryfeeGroup.setVisibility(View.VISIBLE);
            this.oDeliveryfee.setText(getIntent().getStringExtra("DELIVERYFEE"));
            this.oOrderSubtotalValue.setText("("+this.productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+")"+" + "+"("+this.oServicefeeValue.getText()+" + "+this.oDeliveryfee.getText()+")"+" = "+Double.toString((Double.parseDouble(this.productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY")))+(Double.parseDouble(this.oServicefeeValue.getText().toString())+Double.parseDouble(this.oDeliveryfee.getText().toString()))));
            this.btnAction.setText("Successfully delivered?");
        }
        else
        {
            this.oOrderSubtotalValue.setText("("+this.productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+")"+" + "+this.oServicefeeValue.getText()+" = "+Double.toString((Double.parseDouble(this.productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY")))+Double.parseDouble(this.oServicefeeValue.getText().toString())));
            this.btnAction.setText("Picked up?");
        }
        this.btnAction.setOnClickListener(this::ifSuccessful);
        String[] images = this.productData.getImages();

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
        setContentView(R.layout.activity_verify_viewer);
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
