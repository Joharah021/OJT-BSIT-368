package com.example.cakeshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateYourCake  extends AppCompatActivity {
    private DataBase dbutilities;
    private SimpleAdapter adapter;
    private ListView listView;
    private ArrayList<HashMap<String,String>> list;
    private HashMap<String,String[]> positionTracker;
    private FloatingActionButton fabAdd;
    private int steps;
    private String cakename;
    private String cakeshape;
    private String cakeflavour;
    private String cakesize;
    private String cakedescription;

    private void initWidgets(){
        this.dbutilities = new DataBase(this,null);
        this.listView = (ListView) findViewById(R.id.orderLists);
        this.list = new ArrayList<>();
        this.positionTracker = new HashMap<>();
        this.adapter = new SimpleAdapter(this, this.list,android.R.layout.simple_list_item_2,new String[]{ "line1", "line2" },new int[]{ android.R.id.text1, android.R.id.text2 });
        this.listView.setAdapter(this.adapter);
        this.updateListFromDatabase();
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent createdcakeview = new Intent(CreateYourCake.this,CreatedCakeViewer.class);
                createdcakeview.putExtra("CREATEDCAKEDATA",positionTracker.get(Integer.toString(position)));
                startActivity(createdcakeview);
            }
        });

        this.fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        this.fabAdd.setOnClickListener(this::onAdd);
        this.steps = 1;
    }
    private void updateListFromDatabase(){
        this.list.clear();
        this.adapter.notifyDataSetChanged();
        /*
        * ORDER:
        *   id , user_id , cakename , cakeshape , cake flavour, cake size , cake description
        *   0  ,    1    ,    2     ,     3     ,      4      ,     5     ,         6
        *
        * */
        String[][] data = this.dbutilities.getCreatedCake();
        for(int i = 0; i < data.length; i++){
            this.positionTracker.put(Integer.toString(i),data[i]);
            HashMap<String,String> lst = new HashMap<>();
            lst.put("line1",data[i][2]);
            lst.put("line2",data[i][6]);
            this.list.add(lst);
        }
        this.adapter.notifyDataSetChanged();
    }
    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void showDescriptionOption(){
        // fifth popup TODO: fix this
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cake Additional info");
        EditText cdescription = new EditText(this);
        alertDialog.setView(cdescription);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cakedescription = cdescription.getText().toString();
                System.out.println(cakename + "-" + cakeshape + "-" + cakeflavour + "-" + cakesize+"-"+cakedescription);
                dbutilities.saveCreatedCake(cakename,cakeshape,cakeflavour,cakesize,cakedescription);
                updateListFromDatabase();
                reset();
                showToast("Cake created!");
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private  void showSizesOption(){
        // fourth popup
        String[] sizes = this.getResources().getStringArray(R.array.sizes); // makita ni sa res/values/strings.xml
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Choose flavor");
        alertDialog.setSingleChoiceItems(R.array.sizes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cakesize = sizes[which];
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cakesize != null){
                    showDescriptionOption();
                }else{
                    showToast("Please choose size!");
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void showFlavourOption(){
        // third popup
        String[] flavours = this.getResources().getStringArray(R.array.flavours); // makita ni sa res/values/strings.xml
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Choose flavour");
        alertDialog.setSingleChoiceItems(R.array.flavours, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cakeflavour = flavours[which];
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cakeflavour != null){
                    steps++;
                    showSizesOption();
                }else{
                    showToast("Please choose flavour!");
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    private  void showShapeOption(){
        // second popup
        String[] shapes = this.getResources().getStringArray(R.array.shapes); // makita ni sa res/values/strings.xml
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Choose shape");
        alertDialog.setSingleChoiceItems(R.array.shapes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cakeshape = shapes[which];
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cakeshape != null){
                    steps++;
                    showFlavourOption();
                }else{
                    showToast("Please choose shape!");
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void showDialog(){
        // first popup
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cake name");
        EditText caketitle = new EditText(this);
        alertDialog.setView(caketitle);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!caketitle.getText().toString().isEmpty()){
                    //if contains text
                    steps++;
                    cakename = caketitle.getText().toString();
                    showShapeOption();
                }else {
                    showToast("Please enter cake name!");
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    private void stepsLookup(){
        switch (this.steps){
            case 1: this.showDialog();break;
            case 2: this.showShapeOption();break;
            case 3: this.showFlavourOption();break;
            case 4:this.showSizesOption();break;
            default:
                this.showToast("Invalid steps");
                break;
        }
    }
    private void reset(){
        this.cakename = null;
        this.cakeshape =null;
        this.cakeflavour = null;
        this.cakesize = null;
        this.steps = 1;
    }
    private  void onAdd(View v){
       this.stepsLookup();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createyourcake_new);
        this.initWidgets();
    }
}
