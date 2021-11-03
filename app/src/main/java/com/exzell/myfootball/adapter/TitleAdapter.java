package com.exzell.myfootball.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exzell.myfootball.R;
import com.google.android.material.textview.MaterialTextView;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    private Context mContext;
    private String mTitle;

    private View.OnClickListener onClick;

    public TitleAdapter(Context context, String title){
        mContext = context;
        mTitle = title;
    }

    public void setClickListener(View.OnClickListener listener){
        onClick = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TitleAdapter.ViewHolder holder, int position) {
        ((MaterialTextView) holder.itemView).setText(mTitle);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public String getTitle(){ return mTitle; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(onClick);
        }
    }
}
