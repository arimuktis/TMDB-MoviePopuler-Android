package com.example.moviedb_populer.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_populer.R;

public class DataViewHolder  extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView hobby;
    public ImageView gambar;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        hobby = itemView.findViewById(R.id.hobby);
        gambar = itemView.findViewById(R.id.gambar);
    }
}
