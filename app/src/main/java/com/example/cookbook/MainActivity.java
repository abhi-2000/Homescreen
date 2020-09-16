package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView) findViewById(R.id.category_recycler);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<modleClass> modleClassList =new ArrayList<>();
        modleClassList.add(new modleClass(R.drawable.breakfast,"Brkfst"));
        modleClassList.add(new modleClass(R.drawable.breakfast,"Brkfst1"));
        modleClassList.add(new modleClass(R.drawable.breakfast,"Brkfst2"));
        modleClassList.add(new modleClass(R.drawable.breakfast,"Brkfst3"));
        modleClassList.add(new modleClass(R.drawable.breakfast,"Brkfst4"));
        modleClassList.add(new modleClass(R.drawable.breakfast,"Brkfst5"));
        modleClassList.add(new modleClass(R.drawable.breakfast,"Brkfst6"));

        Adapter adapter=new Adapter(modleClassList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}