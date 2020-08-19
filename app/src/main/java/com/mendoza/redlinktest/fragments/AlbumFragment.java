package com.mendoza.redlinktest.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mendoza.redlinktest.R;
import com.mendoza.redlinktest.adapters.AlbumItemAdapter;
import com.mendoza.redlinktest.models.Album;
import com.mendoza.redlinktest.viewmodel.AlbumViewModel;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {
    private static final String fragmentTAG = "photosFragment";
    private AlbumItemAdapter mAdapter;
    private AlbumViewModel albumViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SearchView searchView;
    private MenuItem searchItem;
    private PhotosFragment photosFragment = null;

//    public static AlbumFragment newInstance() {
//        return new AlbumFragment();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("AlbumFragment", "onCreateView");
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void initViews(View view){
        //Init ListView

        mAdapter = new AlbumItemAdapter(new ArrayList<Album>(), (view1, position) -> attachPhotosFragment(mAdapter.getItem(position).getId()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(mAdapter);
//        listView.setOnItemClickListener((parent, view1, position, id) -> {
////            Bundle args = new Bundle();
////            args.putInt("albumID", mAdapter.getItem(position).getId());
////            photosFragment = new PhotosFragment(1);
////            photosFragment.setArguments(args);
////            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, photosFragment).commitNow();
//            attachPhotosFragment(mAdapter.getItem(position).getId());
//        });

        //Init SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                albumViewModel.setIsRefreshing();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("AlbumFragment", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!=null)
        {
            photosFragment = (PhotosFragment) getActivity().getSupportFragmentManager().getFragment(savedInstanceState, fragmentTAG);
//            attachPhotosFragment(-1);
        }
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
        albumViewModel.init();
        albumViewModel.getAlbums().observe(this.getViewLifecycleOwner(), albums -> {
            if(albums==null)
                mAdapter.setItems(new ArrayList<Album>());
            else
                mAdapter.setItems(albums);
        });

        albumViewModel.getIsRefreshing().observe(this.getViewLifecycleOwner(), isRefreshing -> swipeRefreshLayout.setRefreshing(isRefreshing));
    }

    private void attachPhotosFragment(int albumId)
    {
//        if(photosFragment==null) {
        Bundle args = new Bundle();
        args.putInt("albumID", albumId);
        photosFragment = new PhotosFragment();
        photosFragment.setArguments(args);
//        }
        if(searchView!=null ){
            searchView.setIconified(true);
            searchView.setIconified(true);
//            searchItem.setVisible(false);
        }

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, photosFragment).commitNow();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("AlbumFragment", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        if(photosFragment!=null && photosFragment.isAdded())
            getActivity().getSupportFragmentManager().putFragment(outState, fragmentTAG, photosFragment);
    }

    public boolean isOnTop(){
        if(photosFragment!=null && photosFragment.isAdded())
            return false;
        else
            return super.isAdded();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d("AlbumFragment", "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //Reference to hide search button
        searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if(isOnTop()) {
                    mAdapter.getFilter().filter(query);
                }else
                    photosFragment.getAdapter().getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if(isOnTop()) {
                    mAdapter.getFilter().filter(query);
                }else
                    photosFragment.getAdapter().getFilter().filter(query);
                return true;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOnTop())
                    searchView.setIconified(true);
            }
        });
    }
}