package com.mendoza.redlinktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mendoza.redlinktest.fragments.AlbumFragment;

public class MainActivity extends AppCompatActivity {
    private static final String fragmentTAG = "albumFragment";
    private AlbumFragment albumFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            albumFragment = new AlbumFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumFragment).commit();
        }else {
            albumFragment = (AlbumFragment) getSupportFragmentManager().getFragment(savedInstanceState, fragmentTAG);
        }
    }

    @Override
    public void onBackPressed() {
        if(albumFragment!=null && !albumFragment.isOnTop())
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumFragment).commit();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(albumFragment!=null && albumFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, fragmentTAG, albumFragment);
    }
}