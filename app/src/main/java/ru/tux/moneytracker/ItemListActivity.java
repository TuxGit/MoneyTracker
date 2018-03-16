package ru.tux.moneytracker;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Record> data = new ArrayList<>();
    private ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_list);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // todo - recyclerView.addItemDecoration();
        adapter = new ItemListAdapter();
        createData();
        recyclerView.setAdapter(adapter); // recyclerView.setAdapter(new ItemListAdapter());
        data.add(new Record("Новый элемент", 99));
        adapter.notifyDataSetChanged();
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


    private class ItemListAdapter extends RecyclerView.Adapter<RecordViewHolder> {
        @Override
        public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_record, parent, false);
            return new RecordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecordViewHolder holder, int position) {
            Record record = data.get(position);
            holder.applyData(record);
            // ((RecordViewHolder) holder).applyData(record); <- extends RecyclerView.Adapter {
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class RecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView price;

        public RecordViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }

        public void applyData(Record record) {
            Resources resources = getResources();

            title.setText(record.getTitle());
            price.setText(String.format(
                    resources.getString(R.string.item_price_format),
                    record.getPrice(),
                    resources.getString(R.string.currency_rubl))
            );
            // not work -> price.setText(String.format("%1$d %2$s", 11, R.string.currency_rubl));
            // price.setText(String.valueOf(record.getPrice()));
            // title.setText(R.string.some_string);
        }
    }
}
