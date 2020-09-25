package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity implements Adapter.OnItemClickListener {
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ImageView imageView, imgcat;
    TextView txtcat;
    CardView topcard;
    Button btn1;
    EditText search1;
    String name1;
    ImageView img_cat_items;
    String[] cat_item = {};
    String[] cat_pho_url = {};
    String[] cat_desc = {};
    TextView txt_cat_items;
    List<modleClass> modleClassList = new ArrayList<>();
    String url1;
    String[] random_item = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search1 = findViewById(R.id.search);
        recyclerView = findViewById(R.id.category_recycler);
        imageView = findViewById(R.id.top_img);
        imgcat = findViewById(R.id.imageview);
        txtcat = findViewById(R.id.txtview);
        btn1 = findViewById(R.id.testbtn);
        progressDialog = new ProgressDialog(this);
        //for category items
        img_cat_items = findViewById(R.id.img_cat_items);
        txt_cat_items = findViewById(R.id.txt_cat_items);
        topcard = findViewById(R.id.top_card);

        GetData getData = new GetData();
        getData.link = "https://www.themealdb.com/api/json/v1/1/random.php";
        getData.link1 = "https://www.themealdb.com/api/json/v1/1/categories.php";
        getData.execute();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ingradient.class);
                String name1 = search1.getText().toString();
                i.putExtra("keyname", name1);
                startActivity(i);
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, catagory.class);
        final String clickedItem = modleClassList.get(position).toString();
        String ca = cat_item[position];
        String ph = cat_pho_url[position];
        String des = cat_desc[position];
        intent.putExtra("keypho_url", ph);
        intent.putExtra("key_desc", des);
        intent.putExtra("keyname", ca);
        Toast.makeText(MainActivity.this, cat_item[position].toString(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void randomfun(View view) {
        Intent i = new Intent(MainActivity.this, ingradient.class);
        String name1 = random_item[0].toString();
        i.putExtra("keyname", name1);
        startActivity(i);

    }


    private class GetData extends AsyncTask<Void, Void, String> {
        String link, link1;
        String json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(link);
                json = jsonpare();
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

        private String jsonpare() {
            try {
                URL url = new URL(link1);
//                URL url1=new URL("https://www.themealdb.com/api/json/v1/1/categories.php");
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!(s.equalsIgnoreCase("Data is not fetched") && json.equalsIgnoreCase("Data is not fetched"))) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray = object.getJSONArray("meals");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject meals = jsonArray.getJSONObject(i);
                        String pho = meals.getString("strMealThumb");
                        String item_name = meals.getString("strMeal");
                        Picasso.get().load(pho).into(imageView);
                        random_item = Arrays.copyOf(random_item, cat_item.length + 1);
                        random_item[random_item.length - 1] = item_name;
                    }
                    Toast.makeText(MainActivity.this, "Loaded Successfully!", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    JSONObject obj = new JSONObject(json);
                    JSONArray jsonArray = obj.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject meals = jsonArray.getJSONObject(i);
                        String cat = meals.getString("strCategory");
                        String pho = meals.getString("strCategoryThumb");
                        String descrip = meals.getString("strCategoryDescription");
                        cat_item = Arrays.copyOf(cat_item, cat_item.length + 1);
                        cat_item[cat_item.length - 1] = cat;
                        cat_pho_url = Arrays.copyOf(cat_pho_url, cat_pho_url.length + 1);
                        cat_pho_url[cat_pho_url.length - 1] = pho;
                        cat_desc = Arrays.copyOf(cat_desc, cat_desc.length + 1);
                        cat_desc[cat_desc.length - 1] = descrip;
                        modleClassList.add(new modleClass(pho, cat));
                        Adapter adapter = new Adapter(modleClassList);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(MainActivity.this);
                        adapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
