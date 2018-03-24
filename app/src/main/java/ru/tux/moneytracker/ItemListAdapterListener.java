package ru.tux.moneytracker;

/**
 * Created by tux on 24/03/18.
 */

public interface ItemListAdapterListener {
    void onItemClick(Record record, int position);
    void onItemLongClick(Record record, int position);
}
