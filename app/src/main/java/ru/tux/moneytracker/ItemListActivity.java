package ru.tux.moneytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 *
 */
public class ItemListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    // private List<Record> data = new ArrayList<>();
    private ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_list);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // todo - recyclerView.addItemDecoration();
        //adapter = new ItemListAdapter(this);
        adapter = new ItemListAdapter();
        recyclerView.setAdapter(adapter);
    }

}
