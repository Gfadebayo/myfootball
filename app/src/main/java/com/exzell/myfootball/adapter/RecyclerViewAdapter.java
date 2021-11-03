package com.exzell.myfootball.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static final int LINEAR_MANAGER = 0;
    public static final int GRID_MANAGER = 1;


    private Context mContext;
    private RecyclerView.LayoutManager mManager;
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mAdapter;

    public RecyclerViewAdapter(Context context, RecyclerView.LayoutManager manager, RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter){
        mContext = context;
        mManager = manager;
        mAdapter = adapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView rv = new RecyclerView(mContext);
        return new ViewHolder(rv);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull RecyclerView itemView) {
            super(itemView);


            itemView.setLayoutManager(mManager);
            itemView.setAdapter(mAdapter);
        }
    }
}
