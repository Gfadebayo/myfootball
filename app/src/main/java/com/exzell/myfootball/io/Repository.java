package com.exzell.myfootball.io;

import android.content.Context;
import android.content.SharedPreferences;

import com.exzell.myfootball.AppExecutors;
import com.exzell.myfootball.io.internet.FootballApi;
import com.exzell.myfootball.model.Fixture;
import com.exzell.myfootball.model.League;
import com.exzell.myfootball.model.Standing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import timber.log.Timber;

@Singleton
public class Repository {

    private Context context;

    private static final String SECURE_PREF = "user details";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TOKEN_TIMESTAMP = "token time";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DISPLAY_NAME = "display name";
    private static final String KEY_PHONE_NO = "phone number";

    private AppExecutors executors;
    private SharedPreferences userPref;
    private FirebaseAuth auth;
    private FootballApi api;

    @Inject
    public Repository(Context context, FirebaseAuth auth, AppExecutors executors, Retrofit retrofit){

        this.context = context;

        this.auth = auth;

        this.executors = executors;

        userPref = context.getSharedPreferences(SECURE_PREF, 0);

        api = retrofit.create(FootballApi.class);

        setAuth();
        saveUserData();
    }

    private void saveUserData() {
        executors.getIoExecutor().submit(() -> {
            FirebaseUser user = auth.getCurrentUser();

            if(user == null) return;

            SharedPreferences.Editor editor = userPref.edit();

            if(!userPref.contains(KEY_DISPLAY_NAME)) editor.putString(KEY_DISPLAY_NAME, user.getDisplayName());

            if(!userPref.contains(KEY_EMAIL)) editor.putString(KEY_EMAIL, user.getEmail());

            if(!userPref.contains(KEY_PHONE_NO)) editor.putString(KEY_PHONE_NO, user.getPhoneNumber());

            editor.apply();
        });
    }

    private void setAuth(){
        auth.addIdTokenListener((FirebaseAuth.IdTokenListener) auth -> {

            FirebaseUser user = auth.getCurrentUser();
            if(user != null) {
                user.getIdToken(false).addOnCompleteListener(task -> {
                    GetTokenResult tokenResult = task.getResult();

                    userPref.edit()
                            .putString(KEY_TOKEN, tokenResult.getToken())
                            .putLong(KEY_TOKEN_TIMESTAMP, tokenResult.getExpirationTimestamp())
                            .apply();
                });
            }
        });
    }

    public boolean canSignIn() {
        return userPref.getLong(KEY_TOKEN_TIMESTAMP, 0) > System.currentTimeMillis() || userPref.contains(KEY_EMAIL);
    }

    public void signin(){
        if(!canSignIn()) return;

        Timber.d("User email from Firebase is %s", auth.getCurrentUser().getDisplayName());
        if(userPref.getLong(KEY_TOKEN_TIMESTAMP, 0) > System.currentTimeMillis()){
            auth.signInWithCustomToken(userPref.getString(KEY_TOKEN, ""));
        }else{
            auth.signInWithEmailAndPassword(userPref.getString(KEY_EMAIL, ""), userPref.getString(KEY_PASSWORD, ""));
        }
    }

    public void saveLoginDetails(@NotNull String email, @NotNull String password) {
        userPref.edit()
                .putString(KEY_EMAIL, email)
                .putString(KEY_PASSWORD, password)
                .apply();
    }

    public List<League> getLeagueList(){
        try {
            return executors.getIoExecutor().submit(() -> api.getLeagues().execute()
                    .body().getLeagues()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Standing> getStandingList(int leagueId, String season){
        try {
            return executors.getIoExecutor().submit(() -> api.getStanding(leagueId, season).execute()
                    .body().getStandings()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public String getCurrentSeason(int leagueId) {
        try {
            return executors.getIoExecutor().submit(() -> api.getSeasons(leagueId).execute()
                    .body().getCurrentSeason()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<Fixture> getLastResults(int leagueId) {
        try {
            return executors.getIoExecutor().submit(() -> api.getLastResult(leagueId).execute()
                    .body().getFixtures()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Fixture> getFixtures(int leagueId, int week, String season) {
        try {
            return executors.getIoExecutor().submit(() -> api.getFixtures(leagueId, week, season).execute()
                    .body().getFixtures()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Fixture getFixture(int id) {
        try {
            return executors.getIoExecutor().submit(() -> api.getFixture(id).execute()
                    .body().getFixtures().get(0)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
