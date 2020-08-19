package com.mendoza.redlinktest.repositories;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRepository {
    private static RetrofitRepository instance;
    private AlbumsService webService;

    public static RetrofitRepository getInstance(){
        if(instance == null){
            instance = new RetrofitRepository();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        instance.webService = retrofit.create(AlbumsService.class);

        return instance;
    }

    public AlbumsService getWebService(){return webService;}

}
