package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ingradient extends AppCompatActivity {
    private TextView res_name;
    private RequestQueue mQueue;
    private TextView cat_area;
    private ImageView cover_img;
    private TextView ingra;
    private TextView meas;
    private TextView desc;
//    private Button ytbtn;
    private Button sourcebtn;

    public String yt = "";
    public String brolink = "";
    private String dish1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingradient);

        String dish1 = getIntent().getStringExtra("keyname");
        res_name = (TextView) findViewById(R.id.res_name);
        cat_area = (TextView) findViewById(R.id.cat_area);
//        ytbtn = (Button) findViewById(R.id.youtubebtn);
        meas = (TextView) findViewById(R.id.meas);
        desc = (TextView) findViewById(R.id.desc);
        cover_img = (ImageView) findViewById(R.id.cover_img);
        ingra = (TextView) findViewById(R.id.ingra);
        sourcebtn = (Button) findViewById(R.id.sourcebtn);
        mQueue = Volley.newRequestQueue(this);

//        jsonParse(dish1);
        ////////////

//        mediaController.setAnchorView(videoView);
        //////

        sourcebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(brolink));
                startActivity(browserIntent);
            }
        });

        // youtube button event
//        ytbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(yt.toString())));
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(yt));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setPackage("com.google.android.youtube");
//                startActivity(intent);
//            }
//        });


        String name1 = "https://www.themealdb.com/api/json/v1/1/search.php?s="+dish1;
        FetchAnActivity faa = new FetchAnActivity(name1,cat_area,res_name,cover_img,ingra,meas,desc);
        faa.execute();
    }

    public void back(View view) {
        Intent intent = new Intent(ingradient.this, MainActivity.class);
        startActivity(intent);
    }

    public void sharebtn(View view) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, "Youtube link : "+"\n"+
                yt+"\n\n"+"Source link : "+"\n"+brolink+"\n\n"+"Credit : COOKBOOK");
        startActivity(Intent.createChooser(txtIntent ,"Share"));
    }

    public void playbtn(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(yt.toString())));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(yt));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }


    /////////////////////////////


    public class FetchAnActivity extends AsyncTask<Void,Void,String> {
        TextView txt;
        String url1;
        private TextView res_name;
        //    private RequestQueue mQueue;
        private TextView cat_area;
        private ImageView cover_img;
        private TextView ingra;
        private TextView meas;
        private TextView desc;
//    ProgressBar progressBar;

        public FetchAnActivity(String url1,TextView cat_area, TextView res_name, ImageView cover_img,
                               TextView ingra, TextView meas, TextView desc) {
            this.cat_area = cat_area;
            this.res_name = res_name;
            this.cover_img = cover_img;
            this.ingra = ingra;
            this.meas = meas;
            this.desc = desc;
//            this.yt = yt;
//            this.brolink = brolink;
            this.url1 = url1;
//        this.progressBar = progressBar;
        }


        @Override
        protected String doInBackground(Void... voids) {
            try {
//            URL url = new URL("https://www.themealdb.com/api/json/v1/1/search.php?s=dal");
                URL url = new URL(url1);
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
                txt.setText("data not fetched");
            }
            else{
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray = object.getJSONArray("meals");
//                JSONArray jsonArray = new object.getJSONArray("meals");
                    for (int i = 0; i < 1/*jsonArray.length()*/; i++) {
                        JSONObject meals = jsonArray.getJSONObject(i);
                        String recipe_name = meals.getString("strMeal");
                        String area = meals.getString("strArea");
                        String cata = meals.getString("strCategory");
                        String pho = meals.getString("strMealThumb");
                        String ytlink = meals.getString("strYoutube");
                        yt = ytlink;

                        String soulink = meals.getString("strSource");
                        brolink = soulink;
                        String ingrad1 = meals.getString("strIngredient1");
                        String ingrad2 = meals.getString("strIngredient2");
                        String ingrad3 = meals.getString("strIngredient3");
                        String ingrad4 = meals.getString("strIngredient4");
                        String ingrad5 = meals.getString("strIngredient5");
                        String ingrad6 = meals.getString("strIngredient6");
                        String ingrad7 = meals.getString("strIngredient7");
                        String ingrad8 = meals.getString("strIngredient8");
                        String ingrad9 = meals.getString("strIngredient9");
                        String ingrad10 = meals.getString("strIngredient10");
                        String ingrad11 = meals.getString("strIngredient11");
                        String ingrad12 = meals.getString("strIngredient12");
                        String ingrad13 = meals.getString("strIngredient13");
                        String ingrad14 = meals.getString("strIngredient14");
                        String ingrad15 = meals.getString("strIngredient15");
                        String ingrad16 = meals.getString("strIngredient16");
                        String ingrad17 = meals.getString("strIngredient17");
                        String ingrad18 = meals.getString("strIngredient18");
                        String ingrad19 = meals.getString("strIngredient19");
                        String ingrad20 = meals.getString("strIngredient20");

                        String meas1 = meals.getString("strMeasure1");
                        String meas2 = meals.getString("strMeasure2");
                        String meas3 = meals.getString("strMeasure3");
                        String meas4 = meals.getString("strMeasure4");
                        String meas5 = meals.getString("strMeasure5");
                        String meas6 = meals.getString("strMeasure6");
                        String meas7 = meals.getString("strMeasure7");
                        String meas8 = meals.getString("strMeasure8");
                        String meas9 = meals.getString("strMeasure9");
                        String meas10 = meals.getString("strMeasure10");
                        String meas11 = meals.getString("strMeasure11");
                        String meas12 = meals.getString("strMeasure12");
                        String meas13 = meals.getString("strMeasure13");
                        String meas14 = meals.getString("strMeasure14");
                        String meas15 = meals.getString("strMeasure15");
                        String meas16 = meals.getString("strMeasure16");
                        String meas17 = meals.getString("strMeasure17");
                        String meas18 = meals.getString("strMeasure18");
                        String meas19 = meals.getString("strMeasure19");
                        String meas20 = meals.getString("strMeasure20");

                        String descrip = meals.getString("strInstructions");


                        Picasso.get().load(pho).into(cover_img);
                        res_name.setText(recipe_name);
                        cat_area.setText(cata+"/"+area);
                        desc.setText(descrip);
                        String ingradients1 = ingrad1+"\n\n"+ingrad2+"\n\n"+ingrad3+"\n\n"+ingrad4+"\n\n"+
                                ingrad5+"\n\n"+ingrad6+"\n\n"+ingrad7+"\n\n"+ingrad8+"\n\n"+ingrad9+"\n\n"+ingrad10+"\n\n"+
                        ingrad11+"\n\n"+ingrad12+"\n\n"+ingrad13+"\n\n"+ingrad14+"\n\n"+
                                ingrad15+"\n\n"+ingrad16+"\n\n"+ingrad17+"\n\n"+ingrad18+"\n\n"+ingrad19+"\n\n"+ingrad20;

                        String measurement1 = meas1+"\n\n"+meas2+"\n\n"+meas3+"\n\n"+meas4+"\n\n"+
                                meas5+"\n\n"+meas6+"\n\n"+meas7+"\n\n"+meas8+"\n\n"+meas9+"\n\n"+meas10+"\n\n"
                                +meas11+"\n\n"+meas12+"\n\n"+meas13+"\n\n"+meas14+"\n\n"+
                                meas15+"\n\n"+meas16+"\n\n"+meas17+"\n\n"+meas18+"\n\n"+meas19+"\n\n"+meas20;
                        ingra.setText(ingradients1.trim());
                        meas.setText(measurement1.trim());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
