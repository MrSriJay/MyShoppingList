package com.example.jayangapalihena.thegrocerylist.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jayangapalihena.thegrocerylist.Data.DatabaseHandler;
import com.example.jayangapalihena.thegrocerylist.Model.Grocery;
import com.example.jayangapalihena.thegrocerylist.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText groceryQuantity;
    private Spinner selectQtyType;
    private Button saveBtn;
    private DatabaseHandler db;
    private Button viewAllbutton;
    public String newGrrocertItemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db=new DatabaseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
            }
        });

        viewAllbutton=(Button) findViewById(R.id.viewAllButton);
        viewAllbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopupDialog(){


        dialogBuilder = new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        groceryItem= (EditText) view.findViewById(R.id.groceryItem);
        groceryQuantity=(EditText) view.findViewById(R.id.groceryQuantity);
        saveBtn=(Button) view.findViewById(R.id.saveBtn);
        selectQtyType=(Spinner) view.findViewById(R.id.selectQtyType) ;


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.qtyType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        selectQtyType.setAdapter(adapter);



        dialogBuilder.setView(view);
        dialog=dialogBuilder.create();
        dialog.show();


        selectQtyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newGrrocertItemType=String.valueOf(selectQtyType.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this,"Please Complete The QtyType",Toast.LENGTH_LONG).show();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!groceryItem.getText().toString().isEmpty()
                        && !groceryQuantity.getText().toString().isEmpty()) {
                    saveGroceryToDB(v);
                }
                else {
                    Toast.makeText(MainActivity.this,"Please Complete The Field",Toast.LENGTH_LONG).show();
                }
            }

        });


    }


    private void saveGroceryToDB(View v) {

        Grocery grocery = new Grocery();


        String newGrocery=groceryItem.getText().toString();
        String newGroceryQuantity=groceryQuantity.getText().toString()+" "+newGrrocertItemType;


        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //save to db
        db.addGrocery(grocery);
        Snackbar.make(v,"Item Saved",Snackbar.LENGTH_LONG).show();

        //Log.d("Item added it",String.valueOf(db.getGroceryCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1200);


    }
}
