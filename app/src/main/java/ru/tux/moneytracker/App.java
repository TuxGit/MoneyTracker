package ru.tux.moneytracker;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tux.moneytracker.api.Api;

/**
 * Created by tux on 18/03/18.
 */

public class App extends Application {
    private static final String TAG = "App";

    private static final String PREFS_NAME = "shared_prefs";
    private static final String KEY_TOKEN = "auth_token";

    private Api api;

    // private GoogleSignInClient googleSignInClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(
                BuildConfig.DEBUG
                        ? HttpLoggingInterceptor.Level.BODY
                        : HttpLoggingInterceptor.Level.NONE
        );


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new AuthInterceptor())
                .build();


        Gson gson = new GsonBuilder()
                .setDateFormat("dd.MM.yyyy HH:mm:ss")
                // .registerTypeAdapter(Record.class, new RecordAdapter()) - customize the conversion
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                // .baseUrl("http://money-tracker.getsandbox.com/")
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

    public void saveAuthToken(String token) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public void clearAuthToken() {
        saveAuthToken(null);
    }

    public String getAuthToken() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    public boolean isAuthorized() {
        return !TextUtils.isEmpty(getAuthToken());
    }

    // public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) {
    //     this.googleSignInClient = googleSignInClient;
    // }
    //
    // public GoogleSignInClient getGoogleSignInClient() {
    //     return googleSignInClient;
    // }

    private class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl url = request.url();

            HttpUrl.Builder urlBuilder = url.newBuilder();
            HttpUrl newUrl = urlBuilder.addQueryParameter("auth-token", getAuthToken()).build();

            Request.Builder requestBuilder = request.newBuilder();
            Request newRequest = requestBuilder.url(newUrl).build();

            return chain.proceed(newRequest);
        }
    }
}
