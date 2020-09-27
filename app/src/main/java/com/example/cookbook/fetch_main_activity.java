package com.example.cookbook;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fetch_main_activity extends AsyncTask<Void,Void,String> {
    RecyclerView recyclerView;
    ImageView imageView, imgcat;
    TextView txtcat;
    CardView topcard;
    Button btn1;
//    RequestQueue mQueue;
    ImageView img_cat_items;
    String[] cat_item ={};
    TextView txt_cat_items;
    List<modleClass> modleClassList = new ArrayList<>();
//    ProgressBar progressBar;

    public fetch_main_activity(ImageView imgcat,TextView txtcat) {
        this.imgcat = imgcat;
        this.txtcat = txtcat;
//        this.res_name = res_name;
//        this.cover_img = cover_img;
//        this.ingra = ingra;
//        this.meas = meas;
//        this.desc = desc;
//        this.yt = yt;
//        this.brolink = brolink;
//        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("https://www.themealdb.com/api/json/v1/1/categories.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            if(inputStream==null)
                return "data is not fetched";
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line=br.readLine())!=null)
            {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equalsIgnoreCase("Data is not fetched")){
//            txtcat.setText("data not fetched");
        }
        else{
            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonArray = object.getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject meals = jsonArray.getJSONObject(i);
                    String cat = meals.getString("strCategory");
                    String pho = meals.getString("strCategoryThumb");
                    cat_item = Arrays.copyOf(cat_item, cat_item.length+1);
                    cat_item[cat_item.length -1] = cat;
                    modleClassList.add(new modleClass(pho, cat));
//                    Adapter adapter = new Adapter(modleClassList);
//                    recyclerView.setAdapter(adapter);
//                    adapter.setOnItemClickListener(MainActivity.this);
                }

//                JSONObject meals = new JSONObject(s);
//                String txt1= meals.getString("type");
//                txt.setText(txt1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
