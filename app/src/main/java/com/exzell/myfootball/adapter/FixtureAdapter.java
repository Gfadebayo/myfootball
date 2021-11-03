package com.exzell.myfootball.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exzell.myfootball.R;
import com.exzell.myfootball.databinding.ItemFixtureBinding;
import com.exzell.myfootball.model.Fixture;
import com.exzell.myfootball.utils.ImageExtKt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.ViewHolder> {

    private static final String PAYLOAD_LOGO = "logo change";

    private Context mContext;
    private List<Fixture> mFixtures;
    private Consumer<Integer> mListener;

    public FixtureAdapter(Context context, List<Fixture> fixtures){
        mContext = context;
        mFixtures = fixtures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_fixture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fixture fixture = mFixtures.get(position);

        String homeTeamScore = fixture.getHomeName() + "\n" + fixture.getHomeScore();
        holder.mBinding.textHome.setText(homeTeamScore);

        String awayTeamScore = fixture.getAwayName() + "\n" + fixture.getAwayScore();
        holder.mBinding.textAway.setText(awayTeamScore);

        if(fixture.getStatus().isEmpty()) holder.mBinding.textMatchStatus.setVisibility(View.GONE);
        else holder.mBinding.textMatchStatus.setText(fixture.getStatus());

        ImageExtKt.loadImage(holder.mBinding.imageHome, fixture.getHomeLogoDir());
        ImageExtKt.loadImage(holder.mBinding.imageAway, fixture.getAwayLogoDir());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FixtureAdapter.ViewHolder holder, int position, @NonNull @NotNull List<Object> payloads) {
        if(payloads.contains(PAYLOAD_LOGO)) {
            Fixture fix = mFixtures.get(position);

            ImageExtKt.loadImage(holder.mBinding.imageHome, fix.getHomeLogoDir());
            ImageExtKt.loadImage(holder.mBinding.imageAway, fix.getAwayLogoDir());
        }
        else super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mFixtures.size();
    }

    public void setFixtures(List<Fixture> fixtures) {
        List<Fixture> oldFix = new ArrayList<>(mFixtures);
        mFixtures.clear();
        mFixtures.addAll(fixtures);

        notifyItemRangeChanged(0, oldFix.size());

        if(oldFix.size() > fixtures.size()){
            notifyItemRangeRemoved(fixtures.size(), oldFix.size() - fixtures.size());

        }else{
            notifyItemRangeInserted(oldFix.size(), fixtures.size() - oldFix.size());
        }
    }

    public void callLogoChange() {
        notifyItemRangeChanged(0, mFixtures.size(), PAYLOAD_LOGO);
    }

    public void setOnFixtureClickListener(Consumer<Integer> listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ItemFixtureBinding mBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ItemFixtureBinding.bind(itemView);

            mBinding.getRoot().setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();

                mListener.accept(mFixtures.get(pos).getId());
            });
        }
    }
}
