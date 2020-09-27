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

public class Adapter_card extends RecyclerView.Adapter<Adapter_card.viewholder_card> {



    private List<modleCard> modleCardList ;
    private Adapter_card.OnItemClickListenercard mListener;

    public interface OnItemClickListenercard{
        void onItemClick1(int position);
    }

    public void setOnItemClickListener(Adapter_card.OnItemClickListenercard listener) {
        mListener = listener;
    }

    public Adapter_card(List<modleCard> modleCardList) {
        this.modleCardList = modleCardList;
    }

    @NonNull
    @Override
    public Adapter_card.viewholder_card onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_card_layout,viewGroup,false);
        return new Adapter_card.viewholder_card(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_card.viewholder_card holder, int position) {
        String url=modleCardList.get(position).getUrl();
        holder.setData(url);
    }

    @Override
    public int getItemCount() {
        return modleCardList.size();
    }

    class viewholder_card extends RecyclerView.ViewHolder{

        private ImageView image;
        String url1;
        public viewholder_card(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.top_img_card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick1(position);
                        }
                    }
                }
            });
        }
        public void setData(String url)
        {
            url1 = url;
            Picasso.get().load(url1).into(image);

        }
    }
}
