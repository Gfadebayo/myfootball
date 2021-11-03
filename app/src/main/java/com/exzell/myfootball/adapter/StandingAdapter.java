package com.exzell.myfootball.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.exzell.myfootball.R;
import com.exzell.myfootball.databinding.ItemStandingBinding;
import com.exzell.myfootball.model.Standing;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class StandingAdapter extends RecyclerView.Adapter<StandingAdapter.ViewHolder> {

    private Context mContext;
    private List<Standing> mStandings;

    public StandingAdapter(Context context, List<Standing> standings){

        mContext = context;
        mStandings = standings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_standing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StandingAdapter.ViewHolder holder, int position) {

        if(position > 0) {
            Standing stand = mStandings.get(position-1);

            holder.mBinding.textPosition.setText(Integer.toString(stand.getPosition()));
            holder.mBinding.textTeam.setText(stand.getName());
            holder.mBinding.textPlayed.setText(Integer.toString(stand.getPlayed()));
            holder.mBinding.textWin.setText(Integer.toString(stand.getWin()));
            holder.mBinding.textDraw.setText(Integer.toString(stand.getDraw()));
            holder.mBinding.textLoss.setText(Integer.toString(stand.getLoss()));
            holder.mBinding.textGoalAg.setText(Integer.toString(stand.getGa()));
            holder.mBinding.textGoalDiff.setText(Integer.toString(stand.getGd()));
            holder.mBinding.textPoint.setText(Integer.toString(stand.getPoint()));

            Request request = Glide.with(mContext)
                    .asBitmap()
                    .load(stand.getLogoDir())
                    .circleCrop()
                    .into(new CustomViewTarget<MaterialTextView, Bitmap>(holder.mBinding.textTeam) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap res, @Nullable Transition<? super Bitmap> transition) {
                            holder.mBinding.textTeam.setCompoundDrawablesRelative(new BitmapDrawable(holder.itemView.getResources(), res), null, null, null);
                        }

                        @Override
                        protected void onResourceCleared(@Nullable Drawable placeholder) {
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        }
                    }).getRequest();

            if(!request.isRunning()) request.begin();
        }
    }

    public void setStanding(List<Standing> standings){

        mStandings.addAll(standings);

        notifyItemRangeInserted(1, mStandings.size());
    }

    @Override
    public int getItemCount() {
        return mStandings.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemStandingBinding mBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ItemStandingBinding.bind(itemView);


        }
    }
}
