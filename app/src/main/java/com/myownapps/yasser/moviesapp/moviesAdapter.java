package com.myownapps.yasser.moviesapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Yasser on 8/13/2016.
 */

public class moviesAdapter extends RecyclerView.Adapter<moviesAdapter.moviesHolder> {

    List<movies>moviesList_detail;

    MainActivity context;

    public  moviesAdapter(MainActivity context,List<movies>moviesList)
    {
        this.context=context;
        this.moviesList_detail=moviesList;


    }


    @Override
    public moviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_first_page,parent,false);
        moviesHolder holder=new moviesHolder(row);

        return holder;
    }

int pos;
    @Override
    public void onBindViewHolder(final moviesHolder holder, final int position) {

        Picasso.with( context).load(moviesList_detail.get(position).poster_path).placeholder(R.mipmap.loading).error(R.mipmap.error).into(holder.poster);
        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onClick(holder.getAdapterPosition(), moviesList_detail);
                pos=position;
            }
        });

    }

    public void clearData() {
        int size = this.moviesList_detail.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.moviesList_detail.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList_detail.size();
    }

    class moviesHolder extends RecyclerView.ViewHolder{

        ImageView poster;



        public moviesHolder(View itemView) {
            super(itemView);
            poster=(ImageView)itemView.findViewById(R.id.movieposteIMG);

        }
    }}