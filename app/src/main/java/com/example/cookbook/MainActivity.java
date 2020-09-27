package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.OnItemClickListener {
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ImageView imageView, imgcat;
    TextView txtcat;
    CardView topcard;
    RequestQueue mQueue;
    ImageView img_cat_items;
    TextView txt_cat_items;
    List<modleClass> modleClassList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.category_recycler);
        imageView = findViewById(R.id.top_img);
        imgcat = findViewById(R.id.imageview);
        txtcat = findViewById(R.id.txtview);
        progressDialog = new ProgressDialog(this);
        //for category items
        img_cat_items = findViewById(R.id.img_cat_items);
        txt_cat_items = findViewById(R.id.txt_cat_items);
        //
        topcard=findViewById(R.id.top_card);
        mQueue = Volley.newRequestQueue(this);

        GetData getData = new GetData();
        getData.execute();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(this,second_activity.class);
        startActivity(intent);

    }


    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonParse();
            HomeScreen();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Toast.makeText(MainActivity.this, "Loaded Successfully!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void HomeScreen() {
        String url = "https://www.themealdb.com/api/json/v1/1/random.php";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject meals = jsonArray.getJSONObject(i);
                                String pho = meals.getString("strMealThumb");
                                Picasso.get().load(pho).into(imageView);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void jsonParse() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        String url = "https://www.themealdb.com/api/json/v1/1/categories.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject meals = jsonArray.getJSONObject(i);
                                String cat = meals.getString("strCategory");
                                String pho = meals.getString("strCategoryThumb");
                                modleClassList.add(new modleClass(pho, cat));
                                Adapter adapter = new Adapter(modleClassList);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(MainActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        Adapter adapter = new Adapter(modleClassList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}