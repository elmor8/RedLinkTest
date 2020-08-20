package com.mendoza.redlinktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mendoza.redlinktest.fragments.AlbumFragment;
import com.mendoza.redlinktest.fragments.PhotosFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String albumFragmentTAG = "albumFragment";
    private static final String photoFragmentTAG = "photosFragment";
    private AlbumFragment albumFragment;
    private PhotosFragment photosFragment;

    private SearchView searchView;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        if(savedInstanceState == null) {
            albumFragment = new AlbumFragment();
            albumFragment.setOnItemClickCallback(((view, position) -> attachPhotosFragment(albumFragment.getAdapter().getItem(position).getId())));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumFragment).commit();
        }else {
            albumFragment = (AlbumFragment) getSupportFragmentManager().getFragment(savedInstanceState, albumFragmentTAG);
            albumFragment.setOnItemClickCallback(((view, position) -> attachPhotosFragment(albumFragment.getAdapter().getItem(position).getId())));
            photosFragment = (PhotosFragment) getSupportFragmentManager().getFragment(savedInstanceState, photoFragmentTAG);
        }
    }

    @Override
    public void onBackPressed() {
        if(!isAlbumFragmentOnTop())
        {
            searchItem.setVisible(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumFragment).commit();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * Verifica si el fragment que se está mostrando es el de Álbumes o Fotos
     * @return True si se está mostrando el de Álbumes
     */
    public boolean isAlbumFragmentOnTop()
    {
        return photosFragment==null || !photosFragment.isAdded();
    }

    /**
     * Muestra el Fragment de Fotos para el ID de album {@Link albumId}
     * @param albumId
     */
    private void attachPhotosFragment(int albumId)
    {
        Bundle args = new Bundle();
        args.putInt("albumID", albumId);
        photosFragment = new PhotosFragment();
        photosFragment.setArguments(args);
        if(searchView!=null ){
            searchView.setIconified(true);
            searchView.setIconified(true);
            searchItem.setVisible(false);
        }

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, photosFragment).commitNow();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(albumFragment!=null && albumFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, albumFragmentTAG, albumFragment);

        if(photosFragment!=null && photosFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, photoFragmentTAG, photosFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Reference to hide search button
        searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if(isAlbumFragmentOnTop()) {
                    albumFragment.getAdapter().getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if(isAlbumFragmentOnTop()) {
                    albumFragment.getAdapter().getFilter().filter(query);
                }
                return true;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAlbumFragmentOnTop())
                    searchView.setIconified(true);
            }
        });
        //Si no se está mostrando el Fragment de Álbumes, oculta la búsqueda
        if(!isAlbumFragmentOnTop())
            searchItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
}