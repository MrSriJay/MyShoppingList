package com.example.jayangapalihena.thegrocerylist.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jayangapalihena.thegrocerylist.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView itemQty;
    private TextView itemAdded;
    private int idGrocery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemNamedet);
        itemQty = (TextView) findViewById(R.id.itemQtydet);
        itemAdded = (TextView) findViewById(R.id.itemAdddet);

        Bundle bundle =getIntent().getExtras();
        if(bundle !=null){
            itemName.setText(bundle.getString("name"));
            itemQty.setText(bundle.getString("qty"));
            itemAdded.setText(bundle.getString("dateAdded"));
            idGrocery=bundle.getInt("id");
        }


    }
}
