package ru.tux.moneytracker.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tux on 26/03/18.
 */

public class BalanceResult {
    public String status;
    @SerializedName("total_expenses")
    public int expense;
    @SerializedName("total_income")
    public int income;
}
