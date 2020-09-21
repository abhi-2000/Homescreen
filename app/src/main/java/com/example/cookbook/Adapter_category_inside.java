package com.example.cookbook;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_category_inside extends RecyclerView.Adapter<Adapter_category_inside.viewholder_category> {

    private List<modelclassCategory_inside> modelclassCategory_insideList ;

    public Adapter_category_inside(List<modelclassCategory_inside> modelclassCategory_insideList) {
        this.modelclassCategory_insideList = modelclassCategory_insideList;
    }

    @NonNull
    @Override
    public viewholder_category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
        return new viewholder_category(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder_category holder, int position) {
        String url=modelclassCategory_insideList.get(position).getUrl();
        String txt=modelclassCategory_insideList.get(position).getTxt();
        holder.setData(url,txt);

    }

    @Override
    public int getItemCount() {
        return modelclassCategory_insideList.size();
    }



    class viewholder_category extends RecyclerView.ViewHolder{
        public String url1;
        private ImageView image= itemView.findViewById(R.id.img_cat_items);
        private TextView text;

        public viewholder_category(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.txt_cat_items);
        }
        public void setData(String url, String txt)
        {
            url1 = url;
            Picasso.get().load(url1).into(image);
            text.setText(txt);

        }

    }


}