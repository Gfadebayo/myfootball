package com.exzell.myfootball;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.exzell.myfootball.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);

        NavController controller = Navigation.findNavController(this, R.id.fragment_container);
        controller.setGraph(R.navigation.nav_paths);
        
        NavigationUI.setupWithNavController(mBinding.toolbar, controller);
        NavigationUI.setupWithNavController(mBinding.bottomNavView, controller);
    }
}