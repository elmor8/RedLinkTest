package com.mendoza.redlinktest.repositories;


import com.mendoza.redlinktest.models.Album;
import com.mendoza.redlinktest.models.AlbumPhoto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AlbumsService {
    @GET("users/{user}/albums")
    Call<List<Album>> listAlbums(@Path("user") Integer user);

//    @GET("users")
//    Call<List<User>> listUsers();

    @GET("albums/{album}/photos")
    Call<List<AlbumPhoto>> listPhotos(@Path("album") Integer albumId);
}
