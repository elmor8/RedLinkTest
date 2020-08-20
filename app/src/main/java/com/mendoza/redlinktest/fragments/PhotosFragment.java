package com.mendoza.redlinktest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.snackbar.Snackbar;

import com.mendoza.redlinktest.R;
import com.mendoza.redlinktest.adapters.PhotoItemAdapter;
import com.mendoza.redlinktest.models.AlbumPhoto;
import com.mendoza.redlinktest.viewmodel.PhotosViewModel;

import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    private Integer albumId;
    private PhotoItemAdapter mAdapter;
    private PhotosViewModel photosViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        mAdapter = new PhotoItemAdapter(new ArrayList<AlbumPhoto>(), ((view1, position) -> Snackbar.make(view1,"Photo "+mAdapter.getItem(position),Snackbar.LENGTH_LONG).show()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(mAdapter);

        //Init SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                photosViewModel.setIsRefreshing();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        photosViewModel = new ViewModelProvider(this).get(PhotosViewModel.class);
        albumId = getArguments().getInt("albumID");

        photosViewModel.init(albumId);
        photosViewModel.getPhotos().observe(getViewLifecycleOwner(), photos -> {
            if(photos!=null)
                mAdapter.setItems(photos);
            else
                mAdapter.setItems(new ArrayList<>());
        });

        photosViewModel.getIsRefreshing().observe(getViewLifecycleOwner(), result -> swipeRefreshLayout.setRefreshing(result));
    }

    public PhotoItemAdapter getAdapter()
    {
        return mAdapter;
    }
}