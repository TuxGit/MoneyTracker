package ru.tux.moneytracker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tux on 15/03/18.
 */

public class Record {

    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_INCOMES = "incomes";
    public static final String TYPE_EXPENSES = "expenses";

    public int id;
    @SerializedName("name")
    public String title;
    public int price;
    public String type;

    public Record(String title, int price, String type) {
        this.title = title;
        this.price = price;
        this.type = type;
    }
}
