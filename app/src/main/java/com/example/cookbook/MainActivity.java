package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Adapter.OnItemClickListener, Adapter_card.OnItemClickListenercard {
    private ProgressDialog progressDialog;
    RecyclerView recyclerView, recyclerViewCard;
    ImageView imageView, imgcat;
    TextView txtcat;
    CardView topcard;
    ImageView iv_search;
    EditText search1;
    String name1;
    ImageView img_cat_items;
    String[] cat_item = {};
    String[] cat_pho_url = {};
    String[] cat_desc = {};
    NavigationView nav;
    Adapter_card adapter_card = null;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    //
    ImageView icon_search;
    //

    TextView txt_cat_items;
    List<modleClass> modleClassList = new ArrayList<>();
    String url1;
    String[] random_item = {};
    List<modleCard> modleCardList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search1 = findViewById(R.id.search);
        recyclerView = findViewById(R.id.category_recycler);
        imgcat = findViewById(R.id.imageview);
        txtcat = findViewById(R.id.txtview);
        recyclerViewCard = findViewById(R.id.top_card);
        progressDialog = new ProgressDialog(this);
        //for category items

        img_cat_items = findViewById(R.id.img_cat_items);
        txt_cat_items = findViewById(R.id.txt_cat_items);
        //

        topcard = findViewById(R.id.card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = (NavigationView) findViewById(R.id.navbar);
        iv_search = findViewById(R.id.iv_search);
//        iv_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), ingradient.class);
//                String name1 = search1.getText().toString();
//                i.putExtra("keyname", name1);
//                startActivity(i);
//            }
//        });
//        icon_search = findViewById(R.id.icon_search);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        LayoutInflater factory = getLayoutInflater();
//        View textEntryView = factory.inflate(R.layout.header, null);
//        ImageView home = (ImageView) textEntryView.findViewById(R.id.iv_search);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "SHOW", Toast.LENGTH_LONG).show();
//            }
//        });

//
//        nav.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "SHOW", Toast.LENGTH_LONG).show();
//            }
//        });


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.feedback:
                        Toast.makeText(getApplicationContext(), "feedback", Toast.LENGTH_LONG).show();
                        final Intent intent = new Intent(Intent.ACTION_VIEW)
                                .setType("plain/text")
                                .setData(Uri.parse("cookbook@gmail.com"))
                                .setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail")
                                .putExtra(Intent.EXTRA_EMAIL, new String[]{"cookbook@gmail.com"})
                                .putExtra(Intent.EXTRA_SUBJECT, "feedback")
                                .putExtra(Intent.EXTRA_TEXT, "Feedback for COOKBOOK");
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.recipe:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("CookBook is an android application built to provide you with recipes of top trending and your most desired food with a step by step process." +
                                "You can get all ingredients,instructions,source and videos at just one click.");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        CheckInternet();
//        GetData getData = new GetData();
//        getData.link = "https://www.themealdb.com/api/json/v1/1/search.php?f=E";
//        getData.link1 = "https://www.themealdb.com/api/json/v1/1/categories.php";
//        getData.execute();

    }
    private void CheckInternet() {
        ConnectivityManager manager = (ConnectivityManager)
                getApplicationContext() .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null!=activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                GetData getData = new GetData();
                getData.link = "https://www.themealdb.com/api/json/v1/1/search.php?f=e";
                getData.link1 = "https://www.themealdb.com/api/json/v1/1/categories.php";
                getData.execute();
            }
        }
        else{

            dialogboxfun();

        }
    }

    private void dialogboxfun() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("No internet Connection");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CheckInternet();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewCard.setAdapter(adapter_card);


    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, catagory.class);
        String ca = cat_item[position];
        String ph = cat_pho_url[position];
        String des = cat_desc[position];
        intent.putExtra("keypho_url", ph);
        intent.putExtra("key_desc", des);
        intent.putExtra("keyname", ca);
        Toast.makeText(MainActivity.this, cat_item[position].toString(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


    @Override
    public void onItemClick1(int position) {
        Intent i = new Intent(MainActivity.this, ingradient.class);
        String name1 = random_item[position];
        i.putExtra("keyname", name1);
        Toast.makeText(MainActivity.this, random_item[position].toString(), Toast.LENGTH_SHORT).show();
        startActivity(i);
    }

    //
    public void searchClk(View view) {
                Intent i = new Intent(getApplicationContext(), ingradient.class);
        String name1 = search1.getText().toString();
        i.putExtra("keyname", name1);
        startActivity(i);

    }

    //
//    public void iconSearchClk(View view) {
//   drawerLayout.openDrawer(GravityCompat.START);
//        //        drawerLayout.closeDrawer(GravityCompat.START);
//    }


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
                json = jsonpare();
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
                    LinearLayoutManager linearLayoutManager = null;

                    JSONObject object = new JSONObject(s);

                    JSONArray jsonArray = object.getJSONArray("meals");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject meals = jsonArray.getJSONObject(i);
                        String pho = meals.getString("strMealThumb");
                        String item_name = meals.getString("strMeal");
                        random_item = Arrays.copyOf(random_item, random_item.length + 1);
                        random_item[random_item.length - 1] = item_name;
                        modleCardList.add(new modleCard(pho));
                        adapter_card = new Adapter_card(modleCardList);
                        Log.d("msgabhishek", "onPostExecute: ");
                        recyclerViewCard.setAdapter(adapter_card);
                        adapter_card.setOnItemClickListener(MainActivity.this);
                        adapter_card.notifyDataSetChanged();
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewCard.setLayoutManager(linearLayoutManager);

                    }
                    progressDialog.dismiss();
                    final int time = 3000;
                    final Timer timer = new Timer();
                    final LinearLayoutManager finalLinearLayoutManager = linearLayoutManager;
                    final Adapter_card finalAdapter = adapter_card;
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {

                            if (finalLinearLayoutManager.findLastCompletelyVisibleItemPosition() < (finalAdapter.getItemCount() - 1)) {

                                finalLinearLayoutManager.smoothScrollToPosition(recyclerViewCard, new RecyclerView.State(), finalLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                            } else if (finalLinearLayoutManager.findLastCompletelyVisibleItemPosition() == (finalAdapter.getItemCount() - 1)) {

                                finalLinearLayoutManager.smoothScrollToPosition(recyclerViewCard, new RecyclerView.State(), 0);
                            }
                        }
                    }, 0, time);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    int orientation = getApplicationContext().getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);

                    } else {

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 4, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }
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