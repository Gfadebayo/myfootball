package com.exzell.myfootball.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exzell.myfootball.FootballApplication;
import com.exzell.myfootball.R;
import com.exzell.myfootball.adapter.FixtureAdapter;
import com.exzell.myfootball.adapter.HomeStandingAdapter;
import com.exzell.myfootball.adapter.LeagueAdapter;
import com.exzell.myfootball.adapter.RecyclerViewAdapter;
import com.exzell.myfootball.adapter.TitleAdapter;
import com.exzell.myfootball.databinding.FragmentHomeBinding;
import com.exzell.myfootball.model.Fixture;
import com.exzell.myfootball.model.League;
import com.exzell.myfootball.viewmodel.HomeViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;

    private HomeViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FootballApplication application = (FootballApplication) getActivity().getApplication();

        mViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)).get(HomeViewModel.class);

        application.getAppComponent().injectRepo(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.getLeagues((status, leagues) -> {
            onLeagueFetchDone(status, leagues);
            return null;
        });

        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            mViewModel.clearLeagueList();
            mViewModel.clearSavedContent();

            mViewModel.getLeagues((status, leagues) -> {
                mBinding.swipeRefresh.setRefreshing(false);
                onLeagueFetchDone(status, leagues);
                return null;
            });
        });
    }

    private void onLeagueFetchDone(boolean isSuccessful, List<League> leagues){

        mBinding.indicatorLoading.setVisibility(View.GONE);

        if (isSuccessful) {
            mBinding.recView.setVisibility(View.VISIBLE);

            LeagueAdapter leagueAdapter = new LeagueAdapter(requireContext(), leagues);
            leagueAdapter.setOnItemClicked((position) -> onLeagueClicked(leagues.get(position)));

            LinearLayoutManager linearManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(requireContext(), linearManager, leagueAdapter);

            ConcatAdapter concatAdapter = new ConcatAdapter(rvAdapter);

            Arrays.stream(requireContext().getResources().getStringArray(R.array.home_titles)).forEach(header -> {
                TitleAdapter titleAdapter = new TitleAdapter(requireContext(), header);
                titleAdapter.setClickListener(v -> handleHeaderClick(header));

                concatAdapter.addAdapter(titleAdapter);

                if (!header.equals(requireContext().getString(R.string.current_standing))) {
                    FixtureAdapter fixAdapter = new FixtureAdapter(requireContext(), new ArrayList<>());
                    fixAdapter.setOnFixtureClickListener(id -> {
                        Bundle bund = new Bundle(1);
                        bund.putInt(FixtureFragment.KEY_FIXTURE_ID, id);

                        Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_frag_home_to_frag_fixture, bund);
                    });
                    concatAdapter.addAdapter(fixAdapter);

                } else {
                    HomeStandingAdapter standAdapter = new HomeStandingAdapter(requireContext(), new ArrayList<>());
                    concatAdapter.addAdapter(standAdapter);
                }
            });

            mBinding.recView.setAdapter(concatAdapter);
            onLeagueClicked(leagues.get(0));

        } else {
            Snackbar.make(mBinding.getRoot(), R.string.error_message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again, v ->
                    mViewModel.getLeagues((status, leagues1) -> {
                        onLeagueFetchDone(status, leagues1);
                        return null;
                    })).show();
        }
    }

    private void handleHeaderClick(String header) {
        if(header.equals(requireContext().getString(R.string.current_standing))){
            Bundle bund = new Bundle(2);
            bund.putInt(StandingFragment.KEY_LEAGUE, mViewModel.getMSelectedLeague());
            bund.putString(StandingFragment.KEY_SEASON, mViewModel.getMCurrentSeason());

            Navigation.findNavController(mBinding.getRoot()).navigate(R.id.frag_standing, bund);
        }
    }

    private void onLeagueClicked(League league){
        mViewModel.clearSavedContent();
        mViewModel.setMSelectedLeague(league.getId());

        List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> adapters = ((ConcatAdapter) mBinding.recView.getAdapter()).getAdapters();

        //get all adapters that are not a title adapter
        adapters.stream()
                .filter(adap -> adap instanceof HomeStandingAdapter || adap instanceof FixtureAdapter)
                .forEach(adap -> {
                    String title = ((TitleAdapter) adapters.get(adapters.indexOf(adap)-1)).getTitle();

                    if(adap instanceof HomeStandingAdapter){
                        ((HomeStandingAdapter) adap).setStanding(Collections.emptyList());

                        mViewModel.getTopTeams(league.getId(), (success, standing) -> {
                            if(success) {
                                ((HomeStandingAdapter) adap).setStanding(standing);
                                mViewModel.setLogoFromStanding(null);
                                loadLogos(adapters);
                            }
                            return null;
                        });

                    }else if(title.equals(requireContext().getString(R.string.recent_result))){

                        mViewModel.getLastResults(league.getId(), (success, results) -> {
                            if(success) {
                                if(results.stream().allMatch(fix -> fix.getAwayLogoDir() == null && fix.getHomeLogoDir() == null)) mViewModel.setLogoFromStanding(results);

                                ((FixtureAdapter) adap).setFixtures(results);

                                //getting the fixtures relies on first getting the results
                                findAndFetchFixtures(adapters, league.getId(), results.get(0));
                            }
                            return null;
                        });
                    }else {
                        ((FixtureAdapter) adap).setFixtures(Collections.emptyList());
                    }
                });
    }

    private void findAndFetchFixtures(List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> adapters, int leagueId, Fixture f){

        FixtureAdapter adapter = adapters.stream()
                .filter(adapt -> adapt instanceof TitleAdapter)
                .filter(adapt -> ((TitleAdapter) adapters.get(adapters.indexOf(adapt))).getTitle()
                        .equals(requireContext().getString(R.string.next_fixtures)))
                .map(ad -> ((FixtureAdapter) adapters.get(adapters.indexOf(ad) + 1)))
                .findFirst()
                .get();


        mViewModel.getFixtures(leagueId, f.getWeek()+1, f.getSeason(), (isSuccessful, fixtures) -> {
            if(isSuccessful){

                if(fixtures.stream().allMatch(fix -> fix.getAwayLogoDir() == null && fix.getHomeLogoDir() == null)) mViewModel.setLogoFromStanding(fixtures);
                adapter.setFixtures(fixtures);
            }
            return null;
        });
    }

    private void loadLogos(List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> adapters){
        String fixture = requireContext().getString(R.string.next_fixtures);
        String result = requireContext().getString(R.string.recent_result);

        adapters.stream()
                .filter(adapt -> adapt instanceof TitleAdapter)
                .filter(adapt -> ((TitleAdapter) adapters.get(adapters.indexOf(adapt))).getTitle()
                        .equals(fixture) || ((TitleAdapter) adapters.get(adapters.indexOf(adapt))).getTitle()
                        .equals(result))
                .forEach(ad -> ((FixtureAdapter) adapters.get(adapters.indexOf(ad) + 1)).callLogoChange());
    }
}
