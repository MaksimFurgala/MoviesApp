package com.example.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.Domains.Cast;
import com.example.moviesapp.R;

import java.util.ArrayList;

public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.ViewHolder> {
    ArrayList<Cast> actors;
    Context context;

    public ActorListAdapter(ArrayList<Cast> actors) {
        this.actors = actors;
    }

    @NonNull
    @Override
    public ActorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.actors_viewholder, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorListAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(actors.get(position).getPicUrl())
                .into(holder.imageView);
        holder.nameTxt.setText(actors.get(position).getActor());
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            nameTxt = itemView.findViewById(R.id.nameTxt);
        }
    }
}
