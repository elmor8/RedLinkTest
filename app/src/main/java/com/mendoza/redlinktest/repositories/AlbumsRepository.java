package com.mendoza.redlinktest.repositories;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mendoza.redlinktest.models.Album;

import java.io.IOException;
import java.util.List;

public class AlbumsRepository {
    private RetrofitRepository retrofitInstance;
    MutableLiveData<List<Album>> albumsList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();

    public AlbumsRepository()
    {
        retrofitInstance = RetrofitRepository.getInstance();
    }

    public MutableLiveData<List<Album>> getAlbums(){
        if(albumsList==null)
            albumsList = new MutableLiveData<>();
        return albumsList;
    }

    public MutableLiveData<Boolean> getIsRefreshing(){
        if(isRefreshing==null)
            isRefreshing = new MutableLiveData<>();
        return isRefreshing;
    }

    public void downloadAlbumsForUser(int userId)
    {
        new DownloadAlbums().execute(userId);
    }

    private class DownloadAlbums extends AsyncTask<Integer, Void, List<Album>>{

        @Override
        protected List<Album> doInBackground(Integer... user) {
            try {
                return retrofitInstance.getWebService().listAlbums(user[0]).execute().body();
            }catch (IOException error)
            {
                Log.e("DownloadAlbums", error.getMessage()+"");
            }catch (Exception error)
            {
                Log.e("DownloadAlbums", error.getMessage()+"");
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            super.onPostExecute(albums);
            isRefreshing.setValue(false);
            albumsList.setValue(albums);
        }
    }
}
