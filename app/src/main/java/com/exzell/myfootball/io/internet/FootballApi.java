package com.exzell.myfootball.io.internet;

import com.exzell.myfootball.io.FixtureCollection;
import com.exzell.myfootball.io.LeagueCollection;
import com.exzell.myfootball.io.SeasonCollection;
import com.exzell.myfootball.io.StandingCollection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FootballApi {

    @GET("api/v1/json/1/all_leagues.php")
    Call<LeagueCollection> getLeagues();

    /**
     * Get the standing (lookup table) for a particular league
     * @param leagueId the id of the league
     * @param season the season for the table separated with a hyphen
     */
    @GET("api/v1/json/1/lookuptable.php")
    Call<StandingCollection> getStanding(@Query("l") int leagueId, @Query("s") String season);

    @GET("api/v1/json/1/eventspastleague.php")
    Call<FixtureCollection> getLastResult(@Query("id") int leagueId);

    /**
     * Since the next fixture is only for patreons, you can use this
     * instead by changing the season to the current one and the gameweek
     * to the one after the fixture
     */
    @GET("api/v1/json/1/eventsround.php/")
    Call<FixtureCollection> getFixtures(@Query("id") int leagueId, @Query("r") int gameWeek, @Query("s") String season);

    @GET("api/v1/json/1/search_all_seasons.php/")
    Call<SeasonCollection> getSeasons(@Query("id") int leagueId);

    @GET("api/v1/json/1/lookupevent.php")
    Call<FixtureCollection> getFixture(@Query("id") int id);
}
