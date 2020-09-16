package com.example.cookbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.viewholder> {

    private List<modleClass> modleClassList;

    public Adapter(List<modleClass> modleClassList) {
        this.modleClassList = modleClassList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
     int resource = modleClassList.get(position).getImageresource();
    String txt=modleClassList.get(position).getTxt();
   holder.setData(resource,txt);
    }

    @Override
    public int getItemCount() {
        return modleClassList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView text;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageview);
            text = itemView.findViewById(R.id.txtview);
        }
        public void setData(int resource, String txt)
        {
            image.setImageResource(resource);
            text.setText(txt);
        }
    }
}
