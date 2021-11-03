package com.exzell.myfootball.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exzell.myfootball.R;
import com.exzell.myfootball.databinding.ItemHomeStandingBinding;
import com.exzell.myfootball.model.Standing;
import com.exzell.myfootball.utils.ImageExtKt;

import java.util.ArrayList;
import java.util.List;

public class HomeStandingAdapter extends RecyclerView.Adapter<HomeStandingAdapter.ViewHolder> {

    private Context mContext;
    private List<Standing> mStanding;

    public HomeStandingAdapter(Context context, List<Standing> teams){
        mContext = context;
        mStanding = teams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_home_standing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeStandingAdapter.ViewHolder holder, int position) {
        Standing standing = mStanding.get(position);

        holder.mBinding.textPosition.setText(String.valueOf(standing.getPosition()));
        holder.mBinding.textStats.setText(standing.getForm());
        holder.mBinding.textTeamName.setText(standing.getName());

        ImageExtKt.loadImage(holder.mBinding.imageLogo, standing.getLogoDir());
    }

    @Override
    public int getItemCount() {
        return mStanding.size();
    }

    public void setStanding(List<Standing> standing) {
        List<Standing> oldStanding = new ArrayList<>(mStanding);
        mStanding.clear();
        mStanding.addAll(standing);

        if(oldStanding.size() > mStanding.size()){
            notifyItemRangeRemoved(mStanding.size(), oldStanding.size()-mStanding.size());

        }else if(oldStanding.size() < mStanding.size()){
            notifyItemRangeInserted(oldStanding.size(), mStanding.size()-oldStanding.size());

        }else{
            oldStanding.forEach(stand -> {
                if(mStanding.contains(stand)) notifyItemChanged(mStanding.indexOf(stand));
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ItemHomeStandingBinding mBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ItemHomeStandingBinding.bind(itemView);
        }
    }
}
