package com.exzell.myfootball.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.exzell.myfootball.FootballApplication;
import com.exzell.myfootball.R;
import com.exzell.myfootball.adapter.StandingAdapter;
import com.exzell.myfootball.viewmodel.StandingViewModel;

import java.util.ArrayList;

public class StandingFragment extends Fragment {

    public static final String KEY_LEAGUE = "com.exzell.myfootball.LEAGUE_ID";
    public static final String KEY_SEASON = "com.exzell.myfootball.SEASON";

    private StandingViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FootballApplication app = (FootballApplication) requireActivity().getApplication();

        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(StandingViewModel.class);

        app.getAppComponent().injectRepo(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_standing,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StandingAdapter adapter = new StandingAdapter(requireContext(), new ArrayList<>());

        if(getArguments() != null){
            int leagueId = getArguments().getInt(KEY_LEAGUE);
            String season = getArguments().getString(KEY_SEASON);

            mViewModel.getStanding(leagueId, season, standings -> {
                adapter.setStanding(standings);
                return null;
            });
        }

        ((RecyclerView) view).setAdapter(adapter);
    }
}
