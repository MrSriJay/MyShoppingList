package com.example.jayangapalihena.thegrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jayangapalihena.thegrocerylist.Activities.DetailsActivity;
import com.example.jayangapalihena.thegrocerylist.Activities.MainActivity;
import com.example.jayangapalihena.thegrocerylist.Data.DatabaseHandler;
import com.example.jayangapalihena.thegrocerylist.Model.Grocery;
import com.example.jayangapalihena.thegrocerylist.R;

import java.util.List;

/**
 * Created by Jayanga Palihena on 8/11/2018.
 */

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder>{

    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecylerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @Override
    public RecylerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecylerViewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());


    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(View view,Context ctx) {

            super(view);
            context=ctx;

            groceryItemName=(TextView) view.findViewById(R.id.name);
            quantity=(TextView) view.findViewById(R.id.quantity);
            dateAdded=(TextView) view.findViewById(R.id.dateAdded);

            editButton=(Button) view.findViewById(R.id.editbutton);
            deleteButton=(Button) view.findViewById(R.id.deletebutton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen Details Activity

                    int position = getAdapterPosition();

                    Grocery grocery=groceryItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name",grocery.getName());
                    intent.putExtra("qty",grocery.getQuantity());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("dateAdded",grocery.getDateItemAdded());

                    context.startActivity(intent);



                }
            });

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.editbutton:
                    int position = getAdapterPosition();
                    Grocery grocery =groceryItems.get(position);
                    edititem(grocery);

                    break;

                case R.id.deletebutton:
                    position = getAdapterPosition();
                    grocery =groceryItems.get(position);
                    deleteItem(grocery.getId());
                    break;
            }
        }

        public void deleteItem(final int id){


            // create our Alter Build

            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog,null);

            Button yesBtn= (Button) view.findViewById(R.id.yesBtn);
            Button noBtn= (Button) view.findViewById(R.id.noBtn);

            alertDialogBuilder.setView(view);
            dialog=alertDialogBuilder.create();
            dialog.show();

         noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item

                    DatabaseHandler db = new DatabaseHandler(context);
                    //Delete item
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });


        }

        public void edititem(final Grocery grocery){

            alertDialogBuilder= new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup,null);

            final EditText groceryItem=(EditText) view.findViewById(R.id.groceryItem);
            final EditText quantity=(EditText) view.findViewById(R.id.groceryQuantity);
            Button saveBtn = (Button) view.findViewById(R.id.saveBtn);

            alertDialogBuilder.setView(view);
            dialog=alertDialogBuilder.create();
            dialog.show();

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    //update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()){
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                        dialog.dismiss();
                    }else {

                        Snackbar.make(view,"Please Complete The Field",Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }


    }
}
