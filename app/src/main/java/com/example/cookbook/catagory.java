package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    CheckInternet();
    }
    private void CheckInternet() {
        ConnectivityManager manager = (ConnectivityManager)
                getApplicationContext() .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null!=activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                cat1 = getIntent().getStringExtra("keyname");
                pho_url = getIntent().getStringExtra("keypho_url");
                cat_des = getIntent().getStringExtra("key_desc");
                cat_de.setText(cat_des);
                toptxt.setText(cat1);
                Picasso.get().load(pho_url).into(cat_img);
                GetData getData = new GetData();
                getData.execute();
            }
        }
        else{
            dialogboxfun();

        }
    }

    private void dialogboxfun() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(catagory.this);
        builder2.setMessage("No internet Connection");
        builder2.setCancelable(false);
        builder2.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CheckInternet();
                    }
                });

        AlertDialog alert12 = builder2.create();
        alert12.show();

    }
    ///////// recycller key
     @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(catagory.this, ingradient.class);
        String ca = cat_item2[position];
        intent.putExtra("keyname", ca);
        startActivity(intent);
    }

    public void secBackOne(View view) {
        finish();
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
                    int orientation = getApplicationContext().getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);

                    }
                    else {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }

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
