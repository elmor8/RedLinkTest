package com.mendoza.redlinktest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.mendoza.redlinktest.models.Album;
import com.mendoza.redlinktest.repositories.AlbumsRepository;

import java.util.List;

public class AlbumViewModel extends ViewModel {
    private MutableLiveData<List<Album>> albumData;
    private MutableLiveData<Boolean> isRefreshing;
    private AlbumsRepository mRepo;

    public void init(){
        if(albumData != null){
            return;
        }
        mRepo = new AlbumsRepository();
        albumData = mRepo.getAlbums();
        isRefreshing = mRepo.getIsRefreshing();
        isRefreshing.setValue(true);
        setIsRefreshing();
    }

    public void setIsRefreshing()
    {
        mRepo.downloadAlbumsForUser(1);
    }

    public LiveData<List<Album>> getAlbums(){
        return albumData;
    }

    public LiveData<Boolean> getIsRefreshing(){
        return isRefreshing;
    }
}