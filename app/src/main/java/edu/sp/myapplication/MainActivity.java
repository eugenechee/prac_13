package edu.sp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    RecyclerView recyclerView;
    ProductListAdapter mAdapter;
    JSONObject items = new JSONObject();
    // store RequestQueue as static to be shared in this app
    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        setupRecycler();
        connectToInternet();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFAB();
            }
        });
    }

    public void openFAB(){
        Intent intent = new Intent(this, FAB.class);
        startActivity(intent);
    }

    void connectToInternet(){
        // Instantiate the RequestQueue.
        // RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://mapp2019-45b7b.firebaseio.com/products.json";

        //Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response is: " + response);

                        parseData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "That didn't work!");
            }
        });

        // Add the request to the RequestQueue
        queue.add(stringRequest);

    }

    void parseData(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            // once data is loaded, update adapter for RecyclerView
            mAdapter.setItems(jsonObject);
            mAdapter.notifyDataSetChanged();

        } catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    void setupRecycler(){
        recyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ProductListAdapter(this, items);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void sendData(String name, double price) {
        String url = "https://mapp2019-45b7b.firebaseio.com/products.json";
        final String n = name;
        final double p = price;
        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "That didn't work!");
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("name", n);
                    obj.put("price", p);

                    Log.d(TAG, obj.toString());
                    return obj.toString().getBytes();
                }
                catch(Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                return super.getBody();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        MainActivity.queue.add(stringRequest);
    }
}

