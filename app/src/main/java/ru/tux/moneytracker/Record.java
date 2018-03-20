package ru.tux.moneytracker;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tux on 15/03/18.
 */

public class Record implements Parcelable {

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

    protected Record(Parcel in) {
        id = in.readInt();
        title = in.readString();
        price = in.readInt();
        type = in.readString();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(price);
        parcel.writeString(type);
    }
}
