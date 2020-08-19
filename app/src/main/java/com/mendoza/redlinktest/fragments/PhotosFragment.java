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

    public PhotosFragment()
    {
//        albumId = albumID;
    }

//    public static PhotosFragment newInstance(int albumID) {
//        return new PhotosFragment(albumID);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        initViews(view);
//        setHasOptionsMenu(true);
        return view;
    }

    private void initViews(View view){
        //Init ListView
        mAdapter = new PhotoItemAdapter(new ArrayList<AlbumPhoto>(), ((view1, position) -> Toast.makeText(getContext(), "Item "+mAdapter.getItem(position), Toast.LENGTH_SHORT).show()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(mAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "Item "+mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
//
//            }
//        });

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