package com.example.cakeshop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProductAdder extends AppCompatActivity {

    private final int REQUEST_CODE = 69;
    private ArrayList<String> imglist;
    private EditText prodName, prodDesc, prodPrice,prodStock;
    private Button buttonAttachImg,btnAdd;
    private DataBase dbutilities;
    private boolean[] formGroup;


    public void initWidgets(){
        /**
         *
         *  initialize widgets eg: textformfields | buttons
         *
         * */
        this.imglist = new ArrayList<>();
        this.prodName = (EditText) findViewById(R.id.editTitle);
        this.prodDesc = (EditText) findViewById(R.id.editDes);
        this.prodStock = (EditText) findViewById(R.id.editStock);
        this.prodPrice = (EditText) findViewById(R.id.editPrice);

        this.buttonAttachImg = (Button) findViewById(R.id.buttonAttachImg);
        this.btnAdd = (Button) findViewById(R.id.btnOnSignupy);
        this.dbutilities = new DataBase(this,null);
        this.formGroup = new boolean[3];
    }
    private void showToast(String message){
        /**
         * display's notification in a short period of time
         * */
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void addListeners(){
        /**
         *
         *  adds action event listener to widgets
         *
         **/
        this.prodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                // check if its empty
                if(!s.toString().isEmpty()){
                    //checks if product exist in vendors product list | shop
                    boolean productExist = dbutilities.productExist(s.toString());
                    if(!productExist){
                        formGroup[0] = true;
                    }else{
                        // Please modify message
                        prodName.setError("Product already exist in your shop!");
                        formGroup[0] = false;
                    }

                }else{
                    // Please modify message
                    prodName.setError("Invalid product name!");
                    formGroup[0] = false;
                }
            }
        });
        this.prodDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                //checks if description contains 30 characters or more | change if you want :)
              if(s.toString().length()>=30){
                  formGroup[1] = true;
              }else{
                  prodDesc.setError("Invalid character length");
                  formGroup[1] = false;
              }
            }
        });

        this.prodPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
               if(!s.toString().isEmpty()){
                    try {
                        boolean isValidPrice = (Integer.parseInt(s.toString())>0)?true:false;
                        if(isValidPrice){
                            formGroup[2] = true;
                        }else{
                            //Please modify message
                            prodPrice.setError("Invalid Price! must be greater than 0");
                            formGroup[2] = false;
                        }
                    }catch (NumberFormatException e){
                        // if the user, Enters a non-numeric character
                        prodPrice.setError("Invalid Price!");
                        formGroup[2] = false;
                    }

               }else{
                   //Please modify message
                   prodPrice.setError("Invalid Price!");
                   formGroup[2] = false;
               }
            }
        });

        this.buttonAttachImg.setOnClickListener(this::onAttachImage);
        this.btnAdd.setOnClickListener(this::onAddProduct);

    }
    private boolean isValidForm(){
        for(boolean valid:this.formGroup){
            if(!valid){
                return false;
            }
        }
        return true;
    }
    private  void onAttachImage(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),this.REQUEST_CODE);
    }
    
    

    private void onAddProduct(View v){
        //check if its a validForm
        if(!this.isValidForm()){
            //if not valid

            //Please modify message
            this.showToast("Please Fill up all the form fields correctly!");
        }else{
            //if valid

            if(imglist.size()<=0){

                showToast("Please insert images");
                return;
            }

            //Please modify message
            long id = this.dbutilities.addProduct(this.prodName.getText().toString(),this.prodDesc.getText().toString(),this.prodPrice.getText().toString(),this.prodStock.getText().toString());
            for(String b64:this.imglist){
                this.dbutilities.addProductImage(Long.toString(id),b64);
            }
            this.showToast("Product successfully added!");
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data.getClipData() != null){
                    int length = data.getClipData().getItemCount();
                    for(int i=0;i<length;i++){
                        try {
                            Uri imguri = data.getClipData().getItemAt(i).getUri();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imguri);
                            System.out.println("URI -> "+imguri.toString());
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            this.imglist.add(imageString);
                        }catch (IOException e ){
                            this.showToast("Invalid media location.");
                        }
                    }
                }else{
                   try {
                       Uri imguri = data.getData();
                       ByteArrayOutputStream baos = new ByteArrayOutputStream();
                       Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imguri);
                       System.out.println("URI -> "+imguri.toString());
                       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                       byte[] imageBytes = baos.toByteArray();
                       String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                       this.imglist.add(imageString);
                   }catch (IOException e ){
                       this.showToast("Invalid media location.");
                   }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_adder);
        this.initWidgets();
        this.addListeners();
    }
    @Override
    protected void onDestroy() {
        this.dbutilities.destroy();
        super.onDestroy();
    }

}
