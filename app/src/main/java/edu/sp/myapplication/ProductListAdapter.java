package edu.sp.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>{
    private JSONObject items;
    LayoutInflater inflater;
    private JSONArray names;
    Context context;

    public ProductListAdapter(Context ctx, JSONObject obj){
        // store a reference to the loaded JSONArray from internet
        //items = obj
        setItems(obj);

        // get inflater for later use
        inflater = LayoutInflater.from(ctx);
    }

    public JSONObject getItems(){
        return items;
    }

    public void setItems(JSONObject items){
        this.items = items;
        //store the key names for easy use later to lessen processing load
        names = items.names();
    }

    @NonNull
    @Override
    public  ProductListAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // inflate xml layout for single row item
        View itemView = inflater.inflate(R.layout.recycler_item, parent, false);
        // create view holder and return it
        return new ProductViewHolder(itemView, this);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ProductViewHolder holder, int position){
        // retrieve data from array
        try{
            //get the json object at this row position
            JSONObject obj = items.getJSONObject(names.getString(position));
            // display the name in textview
            holder.tv.setText(obj.getString("name"));
        }catch(Exception e){
            Log.e("ProductListAdapter", e.getMessage());
        }
    }

    @Override
    public int getItemCount(){
        return items.length();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv;
        ProductListAdapter mAdapter;
        public ProductViewHolder(View itemView, ProductListAdapter adapter){
            super(itemView);
            // get the textview for later use
            tv = itemView.findViewById(R.id.word);
            mAdapter = adapter;
            // set click listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            try{
                // get the json object at the clicked position
                JSONObject obj = items.getJSONObject(names.getString(this.getAdapterPosition()));
                // print dettails from json object in Logcat
                Log.d("ProductViewHolder", "Clicked: " +
                        this.getAdapterPosition() + " - " +
                        obj.getString("name") + " $" +
                        obj.getString("price"));
                Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
                Toast.makeText(itemView.getContext(), obj.getString("name")+ " $" + obj.getString("price"), Toast.LENGTH_LONG).show();

            }catch(Exception e){
                Log.e("ProductViewHolder", e.getMessage());
            }
        }
    }
}

