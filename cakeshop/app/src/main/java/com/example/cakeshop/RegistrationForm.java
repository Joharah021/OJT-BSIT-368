package com.example.cakeshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegistrationForm extends AppCompatActivity{
    EditText editUser, editPass, editRepass,editEmail,editAddress,editCon,editFname,editLname;
    TextView login;
    Button bntSignup;

    DataBase dbutilities;
    boolean[] formGroup;
    private void initWidgets(){
        /**
         *
         *  initialize widgets eg: textformfields | buttons
         *
         * */
        editUser    = (EditText)findViewById(R.id.editUserN);
        editPass    = (EditText)findViewById(R.id.editPass2);
        editRepass  = (EditText)findViewById(R.id.editRePass);
        editEmail   = (EditText)findViewById(R.id.editEmail);
        editAddress = (EditText)findViewById(R.id.editAddress);
        editCon     = (EditText)findViewById(R.id.editContact);
        editFname   = (EditText)findViewById(R.id.editFname);
        editLname   = (EditText)findViewById(R.id.editLname);
        bntSignup   = (Button)findViewById(R.id.btnOnSignupy);
        login       = (TextView)findViewById(R.id.loginy);
        this.formGroup = new boolean[8];
        this.dbutilities = new DataBase(this,null);
    }
    private void showToast(String message){
        /**
         * display's notification in a short period of time
         * */
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private boolean isFormHasEmpty(EditText ...textfields){
        //checks all EditText if it is empty
        for (EditText txtf : textfields){
            if(txtf.getText().toString().isEmpty()){
                return true;
            }else {
                System.out.println(">>>> "+txtf.getText().toString());
            }
        }
        return false;
    }
    private boolean isValidForm(){
        for(boolean valid:this.formGroup){
            if(!valid){
                // if has 1 false
                return false;
            }
        }
        // if its all true
        return true;
    }

    private void  resetFields(EditText...textfields){
        // sets Edit text to empty!
        for(EditText txtf:textfields){
            txtf.setText("");
        }
    }
    private EditText[] remoteAt(EditText[] source,int index){
        // removes index N from EditText array(source)
        ArrayList<EditText> list = new ArrayList<EditText>();
        for(EditText item:source){
            list.add(item);
        }
        list.remove(index);
        EditText[] newItems = new EditText[list.size()];
        int count = 0;
        for(int item = 0;item<list.size();item++){
            newItems[item] = (EditText) list.get(item);
        }
        return newItems;
    }
    private  void toLogin(){
        // transfers to login activity | main activity
        Intent login = new Intent(RegistrationForm.this,MainActivity.class);
        startActivity(login);
    }
    private void onLogin(View v){
        /**
        *  on login clicked | already have an account
        * */
        this.toLogin();
    }
    private  void onSignup(View v){
        /**
         *
         *  on signup clicked | already have an account
         *
         * */
        EditText[] ifCustomer = {this.editUser,this.editPass,this.editRepass,this.editFname,this.editLname,this.editEmail,this.editCon,this.editAddress};
        if(!this.isValidForm() || this.isFormHasEmpty(ifCustomer)){
            // if not valid form | form field is not filled up!
            // please modify error message
            this.showToast("Please Fill up all the fields!");
        }else{
            // if its valid form
            //ORDER IF USER/CUSTOMER : editUser,editPass,editFname,editLname,editEmail,editCon,editAddress
            //ORDER IF VENDOR : editUser,editPass,editFname,editLname,editEmail,editCon,editAddress,editShop
            ifCustomer = this.remoteAt(ifCustomer,2);
            boolean isSuccessfulLogin = this.dbutilities.registerAccount(ifCustomer);
            if(isSuccessfulLogin){
                //please modify message
                this.showToast("Registration successful!");
                this.toLogin();
                //this.resetFields((TemporaryState.isCustomer)?ifCustomer:ifVendor);
            }else{
                //please modify message
                this.showToast("Registration unsuccessful!");
            }
        }
    }
    private void addListeners(){
        /**
         *
         *  adds action event listener to widgets
         *
         **/
        this.editUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                // check if username already been used
                boolean isUsernameExist = dbutilities.usernameExist(s.toString());
                if(isUsernameExist){
                    // invalid email | email is used

                    // please modify error message
                    editUser.setError("username is already taken");
                    formGroup[0] = false;
                }else{
                    formGroup[0] = true;
                }
            }
        });
        this.editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                // if checks if password character lent/size is greater than or equal to 8
                if(s.toString().length()>=8){
                    formGroup[1] = true;
                }else{
                    // please modify error message
                    editPass.setError("Password must contains  at least 8 character");
                    formGroup[1] = false;
                }
            }
        });
        this.editRepass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                // checks if re typed passworrd matches with editPassword text
                if(s.toString().equals(editPass.getText().toString())){
                        formGroup[2] = true;
                }else{
                    // please modify error message
                    editRepass.setError("Password not matched!");
                    formGroup[2] = false;
                }
            }
        });
        this.editFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    formGroup[3] = true;
                }else{
                    // please modify error message
                    editFname.setError("Please enter your firstname!");
                    formGroup[3] = false;
                }
            }
        });
        this.editLname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    formGroup[4] = true;
                }else{
                    // please modify error message
                    editLname.setError("Please enter your lastname!");
                    formGroup[4] = false;
                }
            }
        });
        this.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){};

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // check if its valid email
                if(Pattern.compile("^(.+)@(.+)$").matcher(s.toString()).matches()){
                    // check if email is already used!
                    boolean isEmailExist = dbutilities.emailExist(s.toString());
                    if(!isEmailExist){
                        formGroup[5] = true;
                    }else{
                        // please modify error message
                        editEmail.setError("Email is already taken");
                        formGroup[5] = false;
                    }

                }else{
                    // please modify error message
                    editEmail.setError("Check your email / Invalid email");
                    formGroup[5] = false;
                }
            }
        });
        this.editCon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                // check if contact number is greater than or equal to 6
                if(s.toString().length()>=6){
                    // check if contact number already exist
                    boolean isContactNumberExist = dbutilities.contactNumberExist(s.toString());
                    if(!isContactNumberExist){
                        formGroup[6] = true;
                    }else{
                        // please modify error message
                        editCon.setError("Contact num already taken");
                        formGroup[6] = false;
                    }

                }else{
                    // please modify error message
                    editCon.setError("Invalid contact number");
                    formGroup[6] = false;
                }
            }
        });
        this.editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                // check if address is too short | minimum character is 6
                if(s.toString().length()>=6){
                    formGroup[7] = true;
                }else{
                    // please modify error message
                    editAddress.setError("Address too short!");
                    formGroup[7] = false;
                }
            }
        });
        this.login.setOnClickListener(this::onLogin);
        this.bntSignup.setOnClickListener(this::onSignup);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form);
        this.initWidgets();
        this.addListeners();
    }
    @Override
    protected void onDestroy() {
        this.dbutilities.destroy();
        super.onDestroy();
    }
}
