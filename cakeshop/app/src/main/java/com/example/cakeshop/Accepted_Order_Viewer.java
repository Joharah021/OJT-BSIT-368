package com.example.cakeshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Accepted_Order_Viewer extends AppCompatActivity {

    private TextView oFPersonName,oAddValue,oContactValue,oOrderValue,oOrderStockValue,oOrderPriceValue,oQtyValue,oServicefeeValue,oPaymenMethodValue,oOrderSubtotalValue;
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
        /*
         *  initialize TextView widgets | initialize data
         * */
        this.dbutilities   = new DataBase(this,null)            ;
        this.ImagesSlide   = (ImageView) findViewById(R.id.ImagesSlide)     ;
        this.productID     = getIntent().getStringExtra("PRODUCTID")  ;
        this.costumerData  = this.dbutilities.getCostumerById(getIntent().getStringExtra("COSTUMERID"));
        this.productData   = this.dbutilities.getProductById(this.productID);

        this.oFPersonName  = (TextView) findViewById(R.id.oFPersonName)             ;
        this.oAddValue     = (TextView) findViewById(R.id.oAddValue)                ;
        this.oContactValue = (TextView) findViewById(R.id.oContactValue)            ;
        this.oOrderValue   = (TextView) findViewById(R.id.oOrderValue)              ;
        this.oOrderStockValue    = (TextView) findViewById(R.id.oOrderStockValue)   ;
        this.oOrderPriceValue    = (TextView) findViewById(R.id.oOrderPriceValue)   ;
        this.oQtyValue           = (TextView) findViewById(R.id.oQtyValue)          ;
        this.oServicefeeValue    = (TextView) findViewById(R.id.oServicefeeValue)   ;

        this.oPaymenMethodValue  = (TextView) findViewById(R.id.oPaymenMethodValue) ;
        this.oOrderSubtotalValue = (TextView) findViewById(R.id.oOrderSubtotalValue);
        this.btnAction     = (Button) findViewById(R.id.btnAction)                  ;
        this.running       = true;
        this.index         = 0   ;
        this.deliveryfee   = "0.00";
        this.ImagesSlide.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    private void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG);
    }
    private void askDeliveryFee()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Enter delivery fee");
        EditText Editdeliveryfee = new EditText(this);
        Editdeliveryfee.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialog.setView(Editdeliveryfee);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!(Editdeliveryfee.getText().toString().length()<=0))
                {
                    if(Double.parseDouble(Editdeliveryfee.getText().toString())<0)
                    {
                        showToast("Invalid input");
                        return;
                    }
                    deliveryfee = Editdeliveryfee.getText().toString();
                    dbutilities.updateOrderStatus(getIntent().getStringExtra("ORDERID"),Status.ON_ITS_WAY);
                    dbutilities.updateOrderFees(getIntent().getStringExtra("ORDERID"),oServicefeeValue.getText().toString(),deliveryfee);
                    finish();
                }
                else
                {
                    showToast("Invalid input");
                }
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
    private void ifCod(View v)
    {
        this.askDeliveryFee();
    }
    private void ifPop(View v)
    {
        this.dbutilities.updateOrderStatus(getIntent().getStringExtra("ORDERID"),Status.READY_TO_PICKUP);
        this.dbutilities.updateOrderFees(getIntent().getStringExtra("ORDERID"),this.oServicefeeValue.getText().toString(),"0.00");
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
        this.oServicefeeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count<=0)
                {
                    oServicefeeValue.setText("0");
                }
                else {
                    oOrderSubtotalValue.setText("("+productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+")"+" + "+oServicefeeValue.getText()+" = "+Double.toString((Double.parseDouble(productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY")))+Double.parseDouble(oServicefeeValue.getText().toString())));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.oPaymenMethodValue.setText(getIntent().getStringExtra("PAYMENTMETHOD"));
        this.oOrderSubtotalValue.setText("("+this.productData.get("Prod_Price")+" X "+getIntent().getStringExtra("QUANTITY")+")"+" + "+this.oServicefeeValue.getText()+" = "+Double.toString((Double.parseDouble(this.productData.get("Prod_Price"))*Integer.parseInt(getIntent().getStringExtra("QUANTITY")))+Double.parseDouble(this.oServicefeeValue.getText().toString())));
        if(getIntent().getStringExtra("PAYMENTMETHOD").equals(PaymentMethod.COD))
        {
            this.btnAction.setText("notify for delivering order");
            this.btnAction.setOnClickListener(this::ifCod)       ;
        }
        else
        {
            this.btnAction.setText("notify to pickup order");
            this.btnAction.setOnClickListener(this::ifPop)  ;
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
        setContentView(R.layout.activity_accepted__order__viewer);
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
