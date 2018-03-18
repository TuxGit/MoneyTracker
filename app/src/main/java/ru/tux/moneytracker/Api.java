package ru.tux.moneytracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by tux on 18/03/18.
 */

public interface Api {

    @GET("/items")
    Call<List<Record>> getItems(@Query("type") String type);

    @POST("/items/add")
    Call<Void> createItem(@Body Record record);

}
