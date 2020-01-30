package com.example.moviedb_populer.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_populer.R;

public class DataViewHolder  extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView date;
    public ImageView gambar;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.movieTitle);
        date = itemView.findViewById(R.id.date);
        gambar = itemView.findViewById(R.id.gambar);
    }
}
