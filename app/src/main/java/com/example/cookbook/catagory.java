package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class catagory extends AppCompatActivity  {
    List<modelclassCategory_inside> modelclassCategory_insideList = new ArrayList<>();
    RecyclerView recyclerView;
    String cat1;
    String[] cat_item2 ={};
    private RequestQueue mQueue;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.category_recycler_inside);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

//
        cat1 = getIntent().getStringExtra("keyname");
        mQueue = Volley.newRequestQueue(this);
        GetData getData = new GetData();
        getData.execute();
    }

    ///////// recycller key
//    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(this, ingradient.class);
        final String clickedItem=modelclassCategory_insideList.get(position).toString();
        String ca = cat_item2[position].toString();
        intent.putExtra("keyname",ca);
//        Toast.makeText(MainActivity.this, cat_item2[position].toString(), Toast.LENGTH_SHORT).show();
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

            jsonParse(cat1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(catagory.this, "Loaded Successfully!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public void jsonParse(String cat1) {
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c="+cat1;
//        String khana = sea.get
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject meals = jsonArray.getJSONObject(i);
                                String id = meals.getString("idMeal");
                                String cat = meals.getString("strMeal");
                                String pho = meals.getString("strMealThumb");
                                cat_item2 = Arrays.copyOf(cat_item2, cat_item2.length+1);
                                cat_item2[cat_item2.length -1] = cat;
                                modelclassCategory_insideList.add(new modelclassCategory_inside(pho, cat));

                                Adapter_category_inside adapter = new Adapter_category_inside(modelclassCategory_insideList);
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