package com.exzell.myfootball;

import android.app.Application;
import android.content.res.Resources;

import androidx.core.os.ConfigurationCompat;

import com.exzell.myfootball.di.AppComponent;
import com.exzell.myfootball.di.DaggerAppComponent;
import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;

public class FootballApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        String langCode = ConfigurationCompat.getLocales(Resources.getSystem()
                .getConfiguration()).get(0).getLanguage();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.setLanguageCode(langCode);

        mAppComponent = DaggerAppComponent.builder()
                .bindContext(getApplicationContext())
                .bindFirebaseAuth(auth)
                .bindExecutors(new AppExecutors())
                .build();
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}
