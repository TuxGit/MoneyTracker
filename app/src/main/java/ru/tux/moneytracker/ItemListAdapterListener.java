package ru.tux.moneytracker;

import android.support.v7.widget.RecyclerView;

/**
 * Created by tux on 24/03/18.
 */

public interface ItemListAdapterListener {
    void onItemClick(Record record, int position, RecyclerView.ViewHolder viewHolder);
    void onItemLongClick(Record record, int position, RecyclerView.ViewHolder viewHolder);
}
