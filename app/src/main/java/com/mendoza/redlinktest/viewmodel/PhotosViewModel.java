package com.mendoza.redlinktest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.mendoza.redlinktest.models.AlbumPhoto;
import com.mendoza.redlinktest.repositories.PhotosRepository;

import java.util.List;

public class PhotosViewModel extends ViewModel {
    private Integer selectedAlbum;
    private MutableLiveData<List<AlbumPhoto>> photosData;
    private MutableLiveData<Boolean> isRefreshing;
    private PhotosRepository mRepo;

    public void init(Integer album){
        if(photosData != null){
            return;
        }
        selectedAlbum = album;
        mRepo = new PhotosRepository();
        photosData = mRepo.getPhotos();
        isRefreshing = mRepo.getIsRefreshing();
        isRefreshing.setValue(true);
        setIsRefreshing();
    }

    public void setIsRefreshing()
    {
        mRepo.downloadPhotosFromAlbum(selectedAlbum);
    }

    public LiveData<List<AlbumPhoto>> getPhotos(){
        return photosData;
    }

    public LiveData<Boolean> getIsRefreshing(){
        return isRefreshing;
    }
}