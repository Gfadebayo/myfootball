package com.exzell.myfootball.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.exzell.myfootball.FootballApplication;
import com.exzell.myfootball.databinding.FragmentFixtureBinding;
import com.exzell.myfootball.model.Fixture;
import com.exzell.myfootball.utils.ImageExtKt;
import com.exzell.myfootball.viewmodel.FixtureViewModel;

public class FixtureFragment extends Fragment {

    public static final String KEY_FIXTURE_ID = "com.exzell.myfootball.FIXTURE_ID";
    private FixtureViewModel mViewModel;

    private FragmentFixtureBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FootballApplication app = (FootballApplication) requireActivity().getApplication();

        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(FixtureViewModel.class);

        app.getAppComponent().injectRepo(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFixtureBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.layoutDetail.getRoot().setVisibility(View.VISIBLE);

        int fixtureId = getArguments().getInt(KEY_FIXTURE_ID, -1);
        mViewModel.getFixtureDetails(fixtureId, fix -> {

            mBinding.textScore.setText(fix.getHomeScore() + "-" + fix.getAwayScore());
            ImageExtKt.loadImage(mBinding.imageHome, fix.getHomeLogoDir());
            ImageExtKt.loadImage(mBinding.imageAway, fix.getAwayLogoDir());

            mBinding.textTeamHome.setText(fix.getHomeName());
            mBinding.textTeamAway.setText(fix.getAwayName());

            addMatchStats(fix);
            return null;
        });
    }

    private void addMatchStats(Fixture fix) {
        String homeStat = fix.getHomeShots() + "\n" + fix.getHomeYellowCards() + "\n" + fix.getHomeRedCards();

        String awayStat = fix.getAwayShots() + "\n" + fix.getAwayYellowCards() + "\n" + fix.getAwayRedCards();

        String stat = "Shots" + "\n" + "Yellow Cards" + "\n" + "Red Cards";

        mBinding.layoutDetail.textDetailHome.setText(homeStat);
        mBinding.layoutDetail.textDetailAway.setText(awayStat);
        mBinding.layoutDetail.textDetailTitle.setText(stat);
    }
}
