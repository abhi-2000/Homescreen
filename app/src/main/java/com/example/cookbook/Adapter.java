package com.example.cookbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.viewholder> {

    private List<modleClass> modleClassList;
//    public String url1;

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
//     int resource = modleClassList.get(position).getImageresource();

        String url=modleClassList.get(position).getUrl();
        String txt=modleClassList.get(position).getTxt();
        holder.setData(url,txt);
    }

    @Override
    public int getItemCount() {
        return modleClassList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        public String url1;
        private ImageView image= itemView.findViewById(R.id.imageview);
        private TextView text;


        public viewholder(@NonNull View itemView) {
            super(itemView);

//            image = itemView.findViewById(R.id.imageview);
            text = itemView.findViewById(R.id.txtview);
//            Picasso.get().load(url1).into(image);
        }
        public void setData(String url, String txt)
        {
//            image.setImageResource(resource);
            url1 = url;
            Picasso.get().load(url1).into(image);
            text.setText(txt);

        }
    }
}
