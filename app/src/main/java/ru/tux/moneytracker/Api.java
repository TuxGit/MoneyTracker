package ru.tux.moneytracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.tux.moneytracker.Record;
import ru.tux.moneytracker.api.AuthResult;

/**
 * Created by tux on 18/03/18.
 */

public interface Api {

    @GET("auth")
    Call<AuthResult> auth(@Query("social_user_id") String userId);

    @GET("items")
    Call<List<Record>> getItems(@Query("type") String type);

    @POST("items/add")
    Call<Void> createItem(@Body Record record);

    @DELETE("items/remove")
    Call<Void> removeItem(@Query("id") int id);

}
