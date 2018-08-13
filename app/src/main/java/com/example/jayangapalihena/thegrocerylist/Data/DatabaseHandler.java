package com.example.jayangapalihena.thegrocerylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jayangapalihena.thegrocerylist.Model.Grocery;
import com.example.jayangapalihena.thegrocerylist.Util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jayanga Palihena on 8/11/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper{


    private  Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    String CREATE_GROCERY_TABLE ="CREATE TABLE groceryTable("
                +Constants.KEU_ID+" INTEGER PRIMARY KEY, "
                +Constants.KEY_GROCEY_ITEM+" TEXT, "
                +Constants.KEY_QTY_NUMBER+" TEXT, "
                +Constants.KEY_DATE_NAME+" LONG )";


        db.execSQL(CREATE_GROCERY_TABLE);
        Log.d("Created!!","Created Table");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        onCreate(db);
    }

    // add grocery
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_GROCEY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());


        //insert the row
        db.insert(Constants.TABLE_NAME,null,values);
        Log.d("Save!!","Saved to DB");


    }


    // get a grocery

    private  Grocery getGrocery(int id){

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME,new String[]{
                Constants.KEU_ID,Constants.KEY_GROCEY_ITEM,Constants.KEY_QTY_NUMBER,Constants.KEY_DATE_NAME
        },Constants.KEU_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor !=null)
            cursor.moveToFirst();

            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEU_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCEY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

            //convert timestamp to something readable
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
            .getTime());

            grocery.setDateItemAdded(formattedDate);


        return grocery;
    }

    // get all groceries

    public List<Grocery> getAllGrocery() {

        SQLiteDatabase db=this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor=db.query(Constants.TABLE_NAME,new String[]{
                Constants.KEU_ID,
                Constants.KEY_GROCEY_ITEM,
                Constants.KEY_QTY_NUMBER,
                Constants.KEY_DATE_NAME
        },null,null,null,null,Constants.KEY_DATE_NAME+" DESC");


        if(cursor.moveToFirst()){
            do{

                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEU_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCEY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime());

                grocery.setDateItemAdded(formattedDate);

                //add to the grocery list array

                groceryList.add(grocery);

            }while (cursor.moveToNext());
        }

        return groceryList;
    }

    //update Grocery

    public int updateGrocery(Grocery grocery){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_GROCEY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());

        //update row

        return db.update(Constants.TABLE_NAME,values,Constants.KEU_ID+"=?",new String[]{
                String.valueOf(grocery.getId())
        });
    }

    // delete Grocery

    public  void deleteGrocery(int id){

        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEU_ID+"=?",
                new String[]{
                String.valueOf(id)
                });

        db.close();

    }

    // get Count

    public int getGroceryCount(){

        String countQuery ="SELECT * FROM "+Constants.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery(countQuery,null);
        return cursor.getCount();
    }


}
