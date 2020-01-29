package com.example.moviedb_populer.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_populer.R;
import com.example.moviedb_populer.Resource.OurDataSet;
import com.example.moviedb_populer.view.DataViewHolder;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {

    private List<OurDataSet> list;
    private Context context;

    public DataAdapter(List<OurDataSet> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row,viewGroup,false);

        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int i) {
        OurDataSet currentData = list.get(i);

        dataViewHolder.name.setText(currentData.getName());
        dataViewHolder.hobby.setText(currentData.getHobby());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
