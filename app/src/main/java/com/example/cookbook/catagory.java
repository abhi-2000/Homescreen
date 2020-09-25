package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
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


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class catagory extends AppCompatActivity  implements Adapter_category_inside.OnItemClickListener {
    List<modelclassCategory_inside> modelclassCategory_insideList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView toptxt;
    ImageView cat_img;
    TextView cat_de;
    String cat1,pho_url,cat_des;
    String[] cat_item2 = {};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);
        progressDialog = new ProgressDialog(this);

        cat_img = (ImageView) findViewById(R.id.category_img);
        cat_de = (TextView) findViewById(R.id.category_txt);
        toptxt = (TextView) findViewById(R.id.top);
        recyclerView = (RecyclerView) findViewById(R.id.category_recycler_inside);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

//
        cat1 = getIntent().getStringExtra("keyname");
        pho_url = getIntent().getStringExtra("keypho_url");
        cat_des = getIntent().getStringExtra("key_desc");
        cat_de.setText(cat_des);
        toptxt.setText(cat1);
        Picasso.get().load(pho_url).into(cat_img);
        GetData getData = new GetData();
        getData.execute();
    }

     @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(catagory.this, ingradient.class);
//        final String clickedItem = modelclassCategory_insideList.get(position).toString();
        String ca = cat_item2[position];
        intent.putExtra("keyname", ca);
        startActivity(intent);
    }

    public void secBackOne(View view) {
    Intent intent =new Intent(catagory.this,MainActivity.class);
    startActivity(intent);
    }

    private class GetData extends AsyncTask<Void, Void, String> {
        String out;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            out = jsonParse(cat1);
            return out;
        }

        @Override
        protected void onPostExecute(String st) {
            super.onPostExecute(st);
            if (!(st.equalsIgnoreCase("Data is not fetched"))) {
                try {
                    JSONObject object = new JSONObject(st);
                    JSONArray jsonArray = object.getJSONArray("meals");
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
                                adapter.setOnItemClickListener(catagory.this);
                                adapter.notifyDataSetChanged();

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public String jsonParse(String cat1) {
            try {
                String link = "https://www.themealdb.com/api/json/v1/1/filter.php?c=" + cat1;
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                if (inputStream == null)
                    return "data is not fetched";
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}