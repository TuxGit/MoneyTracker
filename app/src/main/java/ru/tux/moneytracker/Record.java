package ru.tux.moneytracker;

/**
 * Created by tux on 15/03/18.
 */

public class Record {
    private final String title;
    private final int price;

    public Record(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

}
