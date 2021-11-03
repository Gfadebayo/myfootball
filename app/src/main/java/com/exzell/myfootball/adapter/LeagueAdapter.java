package com.exzell.myfootball.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exzell.myfootball.R;
import com.exzell.myfootball.databinding.ItemLeagueBinding;
import com.exzell.myfootball.model.League;
import com.exzell.myfootball.utils.ImageExtKt;

import java.util.List;
import java.util.function.Consumer;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.ViewHolder> {

    private Context mContext;
    private List<League> mLeagues;
    private Consumer<Integer> mListener;

    public LeagueAdapter(Context context, List<League> leagues){
        mContext = context;
        mLeagues = leagues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_league, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueAdapter.ViewHolder holder, int position) {
        League l = mLeagues.get(position);

        holder.mBinding.textLeagueTitle.setText(l.getName());
        ImageExtKt.loadImage(holder.mBinding.imageLeagueThumbnail, l.getLogoDir());
    }

    @Override
    public int getItemCount() {
        return mLeagues.size();
    }

    public void setOnItemClicked(Consumer<Integer> listener){
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ItemLeagueBinding mBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ItemLeagueBinding.bind(itemView);

            itemView.setOnClickListener(v -> mListener.accept(getAbsoluteAdapterPosition()));
        }
    }
}
