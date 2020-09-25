package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.lang.reflect.Array;
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
    RequestQueue mQueue;
    ImageView img_cat_items;
    String[] cat_item ={};
    String[] random_item={};
    TextView txt_cat_items;
    List<modleClass> modleClassList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search1  = findViewById(R.id.search);
        recyclerView = findViewById(R.id.category_recycler);
        imageView = findViewById(R.id.top_img);
        imgcat = findViewById(R.id.imageview);
        txtcat = findViewById(R.id.txtview);
        btn1  = findViewById(R.id.testbtn);
        progressDialog = new ProgressDialog(this);
        //for category items
        img_cat_items = findViewById(R.id.img_cat_items);
        txt_cat_items = findViewById(R.id.txt_cat_items);
        //
        topcard=findViewById(R.id.top_card);
        mQueue = Volley.newRequestQueue(this);

        GetData getData = new GetData();
        getData.execute();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ingradient.class);
                String name1 = search1.getText().toString();
                i.putExtra("keyname",name1);
                startActivity(i);
            }
        });
    }

    //


    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(this,catagory.class);
        final String clickedItem=modleClassList.get(position).toString();
        String ca = cat_item[position].toString();
        intent.putExtra("keyname",ca);
        Toast.makeText(MainActivity.this, cat_item[position].toString(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void randomfun(View view) {
        Intent i = new Intent(MainActivity.this, ingradient.class);
        String name1 = random_item[0].toString();
        i.putExtra("keyname",name1);
        startActivity(i);
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
                                String item_name = meals.getString("strMeal");
                                random_item = Arrays.copyOf(random_item, cat_item.length+1);
                                random_item[random_item.length -1] = item_name;
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

//        fetch_main_activity fetch1 = new fetch_main_activity(imgcat, txtcat);
//        fetch1.execute();

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
                                cat_item = Arrays.copyOf(cat_item, cat_item.length+1);
                                cat_item[cat_item.length -1] = cat;
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