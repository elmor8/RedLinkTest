package com.mendoza.redlinktest.repositories;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.mendoza.redlinktest.models.AlbumPhoto;

import java.io.IOException;
import java.util.List;

public class PhotosRepository {
    private RetrofitRepository retrofitInstance;
    MutableLiveData<List<AlbumPhoto>> photosList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();

    public PhotosRepository()
    {
        retrofitInstance = RetrofitRepository.getInstance();
    }
//    public static AlbumsRepository getInstance(){
//        if(instance == null){
//            instance = new AlbumsRepository();
//            instance.retrofitInstance = RetrofitRepository.getInstance();
//        }
////        Retrofit retrofit = new Retrofit.Builder()
////                .baseUrl("https://jsonplaceholder.typicode.com/")
////                .build();
////        instance.webService = retrofit.create(AlbumsService.class);
////
//        return instance;
//    }

    public MutableLiveData<List<AlbumPhoto>> getPhotos(){
        if(photosList==null)
            photosList = new MutableLiveData<>();
        return photosList;
    }

    public MutableLiveData<Boolean> getIsRefreshing(){
        if(isRefreshing==null)
            isRefreshing = new MutableLiveData<>();
        return isRefreshing;
    }

    public void downloadPhotosFromAlbum(int albumId)
    {
        new DownloadPhotos().execute(albumId);
    }

    private class DownloadPhotos extends AsyncTask<Integer, Void, List<AlbumPhoto>> {

        @Override
        protected List<AlbumPhoto> doInBackground(Integer... albumId) {
            try {
                return retrofitInstance.getWebService().listPhotos(albumId[0]).execute().body();
            }catch (IOException error)
            {
                Log.e("DownloadPhotos", error.getMessage()+"");
            }catch (Exception error)
            {
                Log.e("DownloadPhotos", error.getMessage()+"");
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AlbumPhoto> photos) {
            super.onPostExecute(photos);
            isRefreshing.setValue(false);
            photosList.setValue(photos);
        }
    }
}
