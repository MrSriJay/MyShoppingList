package com.example.jayangapalihena.thegrocerylist.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.jayangapalihena.thegrocerylist.Data.DatabaseHandler;
import com.example.jayangapalihena.thegrocerylist.Model.Grocery;
import com.example.jayangapalihena.thegrocerylist.R;
import com.example.jayangapalihena.thegrocerylist.UI.RecylerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecylerViewAdapter recylerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        db=new DatabaseHandler(this);
        recyclerView =(RecyclerView) findViewById(R.id.recylerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList= new ArrayList<>();
        listItems = new ArrayList<>();

        //Get Items from Database

        groceryList=db.getAllGrocery();

        for(Grocery c : groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Qty:  "+c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added On: "+c.getDateItemAdded());
            listItems.add(grocery);
        }

        recylerViewAdapter = new RecylerViewAdapter(this, listItems);
        recyclerView.setAdapter(recylerViewAdapter);
        recylerViewAdapter.notifyDataSetChanged();

    }

}
