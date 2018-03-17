package ru.tux.moneytracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tux on 16.03.2018.
 */
class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.RecordViewHolder> {
    private List<Record> data = new ArrayList<>(); //Collections.emptyList();

    public ItemListAdapter() {
        // v3 - context, pass context in constructor
        createData();
    }

    @Override
    public ItemListAdapter.RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemListAdapter.RecordViewHolder holder, int position) {
        Record record = data.get(position);
        holder.applyData(record);
        // ((RecordViewHolder) holder).applyData(record); <- extends RecyclerView.Adapter {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void createData() {
        data.add(new Record("Молоко", 35));
        data.add(new Record("Обед на всех", 1015));
        data.add(new Record("Сноуборд и крепления", 14200));
        data.add(new Record("День рождения", 2000));
        data.add(new Record("Мебель", 35));
        data.add(new Record("Ракета Маска для полёта на Марс", 1000000));
        data.add(new Record("Шоколадка", 75));
        data.add(new Record("Хлеб", 18));
        data.add(new Record("", 0));
        data.add(new Record("Умная книжка", 650));
        data.add(new Record("Молоко деревенское", 52));
        data.add(new Record("Творог", 30));
        data.add(new Record("Сырок", 32));
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView price;
        private Context context;

        public RecordViewHolder(View itemView) {
            super(itemView);
            // v1 - context, get context from view
            context = itemView.getContext();

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }

        public void applyData(Record record) {

            title.setText(record.getTitle());
            price.setText(context.getString(R.string.item_price_format, String.valueOf(record.getPrice())));

            // v2 - context, get context from child view
            /*
            title.getContext().getResources();
            title.getRootView().getContext().getResources();

            title.getContext().getResources().getString(R.string.currency_rubl)
            title.getRootView().getContext().getResources().getString(R.string.currency_rubl)
            */

            // test string format
            /*
            price.setText(record.getPrice()); -> crash app
            String s1 = String.format("dsad %d", 22);
            String s2 = String.format("dsad %s", "dsa");
            price.setText(String.format(Locale.getDefault(),"%1$d %2$s", record.getPrice(), resources.getString(R.string.currency_rubl)));

            not work -> price.setText(String.format("%1$d %2$s", 11, R.string.currency_rubl));
            price.setText(String.valueOf(record.getPrice()));
            title.setText(R.string.some_string);
            */
        }
    }
}
