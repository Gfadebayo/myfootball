package com.exzell.myfootball.di;

import android.content.Context;

import com.exzell.myfootball.AppExecutors;
import com.exzell.myfootball.io.internet.InternetManager;
import com.exzell.myfootball.viewmodel.FixtureViewModel;
import com.exzell.myfootball.viewmodel.HomeViewModel;
import com.exzell.myfootball.viewmodel.ProfileViewModel;
import com.exzell.myfootball.viewmodel.SplashViewModel;
import com.exzell.myfootball.viewmodel.StandingViewModel;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {InternetManager.class})
public interface AppComponent {

    void injectRepo(SplashViewModel splashViewModel);

    void injectRepo(HomeViewModel homeViewModel);

    void injectRepo(ProfileViewModel profileViewModel);

    void injectAuth(ProfileViewModel profileViewModel);

    void injectRepo(StandingViewModel standingViewModel);

    void injectRepo(FixtureViewModel fixtureViewModel);

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder bindContext(Context context);

        @BindsInstance
        Builder bindFirebaseAuth(FirebaseAuth auth);

        @BindsInstance
        Builder bindExecutors(AppExecutors executors);

        AppComponent build();
    }
}
