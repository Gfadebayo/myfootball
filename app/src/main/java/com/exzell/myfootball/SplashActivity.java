package com.exzell.myfootball;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.exzell.myfootball.databinding.ActivitySplashBinding;
import com.exzell.myfootball.fragment.LoginDialog;
import com.exzell.myfootball.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding mBinding;

    private SplashViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(SplashViewModel.class);

        ((FootballApplication) getApplication()).getAppComponent().injectRepo(mViewModel);

        if(mViewModel.canSignIn()) {
            //immediately try to sign them in so things will be quicker
            mViewModel.signin();
            start();

        }else setupLogin();
    }

    private void setupLogin(){
        mBinding.getRoot().setVisibility(View.VISIBLE);

        View.OnClickListener signListener = v -> {
            String action = v.getId() == R.id.button_sign_in ? LoginDialog.ACTION_SIGN_IN :
                    LoginDialog.ACTION_SIGN_UP;

            LoginDialog loginDialog = new LoginDialog(this, action, mViewModel);
            loginDialog.setOnLoginDone(() -> {
                start();
                return null;
            });

            loginDialog.show();
        };

        mBinding.buttonSignUp.setOnClickListener(signListener);

        mBinding.buttonSignIn.setOnClickListener(signListener);
    }

    private void start(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}

/*TODO: Animations to add
 *  1) Animate the appearance of the sign in button. a sliding transition should be good*/
