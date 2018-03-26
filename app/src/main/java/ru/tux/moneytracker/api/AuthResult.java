package ru.tux.moneytracker.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tux on 24/03/18.
 */

public class AuthResult {
    public String status;
    public int id;
    @SerializedName("auth_token")
    public String token;
}
