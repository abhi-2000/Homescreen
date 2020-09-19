package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView, imgcat;
    TextView txtcat;
    RequestQueue mQueue;
    ImageView img_cat_items;
    TextView txt_cat_items;
    List<modelclassCategory> modelclassCategoryList =new ArrayList<>();
    List<modleClass> modleClassList =new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.category_recycler);
        imageView = findViewById(R.id.top_img);
        imgcat = findViewById(R.id.imageview);
        txtcat = findViewById(R.id.txtview);
        //for category items
        img_cat_items = findViewById(R.id.img_cat_items);
        txt_cat_items = findViewById(R.id.txt_cat_items);
        //
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        Adapter adapter = new Adapter(modleClassList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void jsonParse() {
        String url = "https://www.themealdb.com/api/json/v1/1/categories.php";
//        String khana = sea.get
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject meals = jsonArray.getJSONObject(i);
                                String cat = meals.getString("strCategory");
//                                String area = meals.getString("strArea");
                                String pho = meals.getString("strCategoryThumb");
//                                Picasso.get().load(pho).into(imgs);

                                modleClassList.add(new modleClass(pho,cat));

                                Adapter adapter=new Adapter(modleClassList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
}