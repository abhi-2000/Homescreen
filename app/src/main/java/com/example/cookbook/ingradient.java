package com.example.cookbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.security.AccessController.getContext;

public class ingradient<dish1> extends AppCompatActivity {
    private TextView res_name;
    private TextView cat_area;
    private ImageView cover_img;
    private TextView ingra;
    private TextView meas;
    private TextView desc;
    ImageView play1;
    private Button sourcebtn;
    RelativeLayout ytback;
    public String yt = "";
    public String brolink = "";
    String dish1;
    ImageView iv_vip;
    ImageView yt_back;
    String meas_add="";
    String ing_add="";
    private ProgressDialog progressDialog;
    public String pass = "";
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingradient);
        dish1 = getIntent().getStringExtra("keyname");
        ytback=findViewById(R.id.ytlayout);
        res_name = (TextView) findViewById(R.id.res_name);
        cat_area = (TextView) findViewById(R.id.cat_area);
        meas = (TextView) findViewById(R.id.meas);
        desc = (TextView) findViewById(R.id.desc);
        iv_vip=findViewById(R.id.iv_vip);
        yt_back=findViewById(R.id.ytbutton_back);
        cover_img = (ImageView) findViewById(R.id.cover_img);
        play1=findViewById(R.id.play);
        ingra = (TextView) findViewById(R.id.ingra);
        sourcebtn = (Button) findViewById(R.id.sourcebtn);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        progressDialog = new ProgressDialog(this);

        sourcebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(brolink));
                startActivity(browserIntent);
            }
        });

CheckInternet();
    }
    private void CheckInternet() {
        ConnectivityManager manager = (ConnectivityManager)
                getApplicationContext() .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null!=activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                String name1 = "https://www.themealdb.com/api/json/v1/1/search.php?s="+dish1;
                FetchAnActivity faa = new FetchAnActivity(name1,cat_area,res_name,cover_img,ingra,meas,desc);
                faa.execute();

            }
        }
        else{
            dialogboxfun();

        }
    }

    private void dialogboxfun() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(ingradient.this);
        builder3.setMessage("No internet Connection");
        builder3.setCancelable(false);
        builder3.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CheckInternet();
                    }
                });

        AlertDialog alert13 = builder3.create();
        alert13.show();

    }



    public void ytback(View view) {
     youTubePlayerView.setVisibility(View.GONE);
       youTubePlayerView.release();
        iv_vip.setVisibility(View.VISIBLE);
        play1.setVisibility(View.VISIBLE);
        res_name.setVisibility(View.VISIBLE);
        cat_area.setVisibility(View.VISIBLE);

    }


    public void playbtn(View view) {
                iv_vip.setVisibility(View.INVISIBLE);
                play1.setVisibility(View.INVISIBLE);
                res_name.setVisibility(View.INVISIBLE);
                cat_area.setVisibility(View.INVISIBLE);
                int cout = 0;
                for (int j = 0; j < yt.length(); j++) {
                    if (yt.charAt(j) == '=') {
                        cout = j+1;
                        break;
                    }
                }
                while (cout != yt.length()) {
                    pass = pass + yt.charAt(cout);
                    cout++;
                }

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.setVisibility(View.VISIBLE);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

             String videoId=pass;
                youTubePlayer.loadVideo(videoId,0);
            }
        });
    }

    public void sharebtn(View view) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, "Youtube link : "+"\n"+
                yt+"\n\n"+"Source link : "+"\n"+brolink+"\n\n"+"Credit : COOKBOOK");
        startActivity(Intent.createChooser(txtIntent ,"Share"));
    }



    public void back(View view) {
        finish();
    }
    /////////////////////////////


    public class FetchAnActivity extends AsyncTask<Void,Void,String> {
        TextView txt;
        String url1;
        private TextView res_name;
        private TextView cat_area;
        private ImageView cover_img;
        private TextView ingra;
        private TextView meas;
        private TextView desc;

        public FetchAnActivity(String url1,TextView cat_area, TextView res_name, ImageView cover_img,
                               TextView ingra, TextView meas, TextView desc) {
            this.cat_area = cat_area;
            this.res_name = res_name;
            this.cover_img = cover_img;
            this.ingra = ingra;
            this.meas = meas;
            this.desc = desc;
            this.url1 = url1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
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
            progressDialog.dismiss();
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
                        for(int j=1;j<=20;j++){
                            String ingval = "strIngredient" + Integer.toString(j);
                            String meas_val = "strMeasure" + Integer.toString(j);
                            String all_ing = meals.getString(ingval);
                            String all_meas = meals.getString(meas_val);
                            if(all_ing == "null"){
                                all_ing = "";
                            }
                            if (all_meas == "null"){
                                all_meas = "";
                            }
                            ing_add = ing_add + "\n\n"+ all_ing;
                            meas_add = meas_add + "\n\n" + all_meas;

                        }

                        String descrip = meals.getString("strInstructions");

                        Picasso.get().load(pho).into(cover_img);
                        res_name.setText(recipe_name);
                        cat_area.setText(cata+"/"+area);
                        desc.setText(descrip);
                        ingra.setText(ing_add.trim());
                        meas.setText(meas_add.trim());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }            }
        }

    }
}