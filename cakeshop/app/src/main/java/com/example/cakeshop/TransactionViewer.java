package com.example.cakeshop;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class TransactionViewer extends AppCompatActivity {
    private LinearLayout orderDeliveryfeeGroup;
    private TextView oOrderValue,oOrderStockValue,oOrderPriceValue,oQtyValue,oServicefeeValue,oPaymenMethodValue,oOrderSubtotalValue,oDeliveryfee,oStatusValue;
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
        this.oOrderValue   = (TextView) findViewById(R.id.oOrderValue)              ;
        this.oOrderStockValue    = (TextView) findViewById(R.id.oOrderStockValue)   ;
        this.oOrderPriceValue    = (TextView) findViewById(R.id.oOrderPriceValue)   ;
        this.oQtyValue           = (TextView) findViewById(R.id.oQtyValue)          ;
        this.oServicefeeValue    = (TextView) findViewById(R.id.oServicefeeValue)   ;
        this.oPaymenMethodValue  = (TextView) findViewById(R.id.oPaymenMethodValue) ;
        this.oDeliveryfee        = (TextView) findViewById(R.id.oDeliveryfee)       ;
        this.oOrderSubtotalValue = (TextView) findViewById(R.id.oOrderSubtotalValue);
        this.oStatusValue        = (TextView) findViewById(R.id.oStatusValue)       ;
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
    private void confirmCancel()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confiirm cancel?");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbutilities.updateOrderStatus(getIntent().getStringExtra("ORDERID"),Status.CANCELED);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    private void ifWaiting(View v)
    {
        this.confirmCancel();
        finish();
    }

    private void setTextViewText(){
        /**
         *initialize textview text|label
         **/
        this.oOrderValue.setText(this.productData.get("Prod_Name"))          ;
        this.oOrderStockValue.setText(this.productData.get("Prod_Stock"))    ;
        this.oOrderPriceValue.setText(this.productData.get("Prod_Price"))    ;
        this.oQtyValue.setText(getIntent().getStringExtra("QUANTITY"));
        this.oServicefeeValue.setText(Double.toString(TemporaryState.Servicefee));
        this.oPaymenMethodValue.setText(getIntent().getStringExtra("PAYMENTMETHOD"));
        this.oStatusValue.setText(getIntent().getStringExtra("ORDERSTATUS"));
        if(getIntent().getStringExtra("ORDERSTATUS").equals(Status.WAITING))
        {
            this.oOrderSubtotalValue.setText("("+this.productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+")"+" + "+this.oServicefeeValue.getText()+" = "+Double.toString((Double.parseDouble(this.productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY")))+Double.parseDouble(this.oServicefeeValue.getText().toString())));
            this.btnAction.setText("Cancel");
            this.btnAction.setOnClickListener(this::ifWaiting);
        }
        else if(getIntent().getStringExtra("ORDERSTATUS").equals(Status.ON_ITS_WAY))
        {
            this.btnAction.setVisibility(View.GONE);
            this.orderDeliveryfeeGroup.setVisibility(View.VISIBLE);
            this.oDeliveryfee.setText(getIntent().getStringExtra("DELIVERYFEE"));
            this.oOrderSubtotalValue.setText("("+this.productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+")"+" + "+"("+this.oServicefeeValue.getText()+" + "+this.oDeliveryfee.getText()+")"+" = "+Double.toString((Double.parseDouble(this.productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY")))+(Double.parseDouble(this.oServicefeeValue.getText().toString())+Double.parseDouble(this.oDeliveryfee.getText().toString()))));
        }
        else
        {
            this.btnAction.setVisibility(View.GONE);
            this.oOrderSubtotalValue.setText("("+this.productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+")"+" + "+this.oServicefeeValue.getText()+" = "+Double.toString((Double.parseDouble(this.productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY")))+Double.parseDouble(this.oServicefeeValue.getText().toString())));
        }
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
        setContentView(R.layout.activity_transaction_viewer);
        this.initWidgets();
        this.setTextViewText();
    }

    @Override
    protected void onDestroy() {
        this.dbutilities.destroy();
        super.onDestroy();
    }
}
