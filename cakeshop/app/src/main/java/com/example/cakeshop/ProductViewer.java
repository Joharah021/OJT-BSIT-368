package com.example.cakeshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.widget.Toast;

public class ProductViewer extends AppCompatActivity {
    private TextView productName,productDescription,productPrice,productStock;
    private Button btnOrder;
    private Thread t1;
    private ImageView ImagesSlide;
    private  String productID;
    private ProductData productData;
    private DataBase dbutilities;
    private boolean running;
    private int index;
    private  String selectedOp;

    private void initWidgets(){
        /*
        *  initialize TextView widgets | initialize data
        * */
        this.dbutilities = new DataBase(this,null);
        this.ImagesSlide = (ImageView) findViewById(R.id.ImagesSlide);
        this.productName  = (TextView) findViewById(R.id.productName);
        this.productDescription = (TextView) findViewById(R.id.productDescription);
        this.productPrice = (TextView) findViewById(R.id.productPrice);
        this.productID = getIntent().getStringExtra("PRODUCTID");
        this.productData = this.dbutilities.getProductById(this.productID);
        this.productStock = (TextView) findViewById(R.id.productStock);
        this.btnOrder = (Button) findViewById(R.id.btnOrder);
        this.running = true;
        this.index = 0;
        this.ImagesSlide.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void setTextViewText(){
        /*
        * initialize textview text|label
        * */
        this.productName.setText(this.productData.get("Prod_Name"));
        this.productDescription.setText(this.productData.get("Prod_Description"));
        this.productPrice.setText(this.productData.get("Prod_Price"));
        this.productStock.setText(this.productData.get("Prod_Stock"));
        this.btnOrder.setOnClickListener(this::onOrder);
        String[] images = this.productData.getImages();

        if(images != null){
            this.t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] imgbytes = Base64.decode(images[index],Base64.DEFAULT);
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



    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    private void showDialog(){
        int stock = Integer.parseInt(this.productStock.getText().toString());
        if(stock <= 0){
            this.showToast("Product out of stock!");
        }else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Enter quantity");
            EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            alertBuilder.setView(editText);
            alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("Clicked -> OK");
                    if (editText.getText().toString().length() <= 0) {
                        showToast("Invalid quantity.");
                    } else {
                        int quantity = Integer.parseInt(editText.getText().toString());
                        if (quantity > stock) {
                            showToast("Quantity is higher than the remaining stock.");
                        } else {
                            if (quantity <= 0) {
                                showToast("Invalid quantity.");
                            } else {
                                onPreOrder(quantity);
                            }
                        }
                    }

                }

            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertBuilder.create();
            alertBuilder.show();
        }
    }

    private void onPreOrder(int quantity)
    {

        String[] pMethod = getResources().getStringArray(R.array.payment_method); // makita ni sa res/values/strings.xml
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Choose payment method");
        alertDialog.setSingleChoiceItems(R.array.payment_method, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedOp = pMethod[which];
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbutilities.onOrder(TemporaryState.userID,productData,selectedOp,Status.WAITING, quantity,0.00,0.00);
                showToast("added to cart");
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
    private void onOrder(View view){
        this.showDialog();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_viewer);
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
