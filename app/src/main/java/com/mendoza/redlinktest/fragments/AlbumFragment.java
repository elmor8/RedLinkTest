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
import com.mendoza.redlinktest.adapters.ItemClickListener;
import com.mendoza.redlinktest.models.Album;
import com.mendoza.redlinktest.viewmodel.AlbumViewModel;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {
    private static final String TAG = "AlbumFragment";
    private AlbumItemAdapter mAdapter;
    private AlbumViewModel albumViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ItemClickListener onItemClick;

    public void setOnItemClickCallback(ItemClickListener onItemClick)
    {
        Log.d(TAG, "setOnItemClickCallback");
        this.onItemClick = onItemClick;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        initViews(view);
        return view;
    }

    /**
     * Inicializo el RecyclerView y SwipeToRefresh
     * @param view
     */
    private void initViews(View view){
        //Init ListView
        mAdapter = new AlbumItemAdapter(new ArrayList<Album>(), this.onItemClick);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(mAdapter);

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
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

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

    public AlbumItemAdapter getAdapter()
    {
        return mAdapter;
    }
}