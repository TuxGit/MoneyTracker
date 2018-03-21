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
    private List<Record> data = new ArrayList<>();

    public void setData(List<Record> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(Record record, boolean atBegin) {
        // data.add(0, record);
        // notifyItemInserted(0);

        // notifyDataSetChanged();

        if (atBegin) {
            data.add(0, record);
            notifyItemInserted(0);
        } else {
            data.add(record);
            notifyItemInserted(data.size());
        }
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // private void createData() {
    //     data.add(new Record("Молоко", 35));
    //     data.add(new Record("Обед на всех", 1015));
    //     data.add(new Record("Сноуборд и крепления", 14200));
    //     data.add(new Record("День рождения", 2000));
    //     data.add(new Record("Мебель", 35));
    //     data.add(new Record("Ракета Маска для полёта на Марс", 1000000));
    //     data.add(new Record("Шоколадка", 75));
    //     data.add(new Record("Хлеб", 18));
    //     data.add(new Record("", 0));
    //     data.add(new Record("Умная книжка", 650));
    //     data.add(new Record("Молоко деревенское", 52));
    //     data.add(new Record("Творог", 30));
    //     data.add(new Record("Сырок", 32));
    // }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView price;
        private Context context;

        public RecordViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }

        public void applyData(Record record) {

            title.setText(record.title);
            price.setText(context.getString(R.string.item_price_format, String.valueOf(record.price)));

        }
    }
}
