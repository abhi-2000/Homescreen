package com.example.categorylayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<modelclassCategory> modelclassCategoryList = new ArrayList<>();
    RecyclerView recyclerView;
    private RequestQueue mQueue;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.category_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

//

        mQueue = Volley.newRequestQueue(this);
        GetData getData = new GetData();
        getData.execute();
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Loaded Successfully!", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        }
    }

    private void jsonParse() {
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=vegetarian";
//        String khana = sea.get
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject meals = jsonArray.getJSONObject(i);
                                String cat = meals.getString("strMeal");
                                String pho = meals.getString("strMealThumb");

                                modelclassCategoryList.add(new modelclassCategory(pho, cat));

                                Adapter_category adapter = new Adapter_category(modelclassCategoryList);
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
