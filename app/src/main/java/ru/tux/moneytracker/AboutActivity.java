package ru.tux.moneytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    private ArrayList<Item> data = new ArrayList<Item>() {{
        add(new Item("Название", "Учёт бюджета"));
        add(new Item("Автор", "Никитин Александр"));
        add(new Item("Версия", BuildConfig.VERSION_NAME)); // + "-beta"
        add(new Item("Сборка", BuildConfig.BUILD_TIME));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // initData();

        // addContentView();

        ViewGroup listView = findViewById(R.id.about_list);
        for (Item item : data) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_about, listView, false);
            TextView name = view.findViewById(R.id.name);
            TextView value = view.findViewById(R.id.value);

            name.setText(String.format(getString(R.string.about_name_format), item.name));
            value.setText(item.value);
            listView.addView(view);
            // listView.addView(view); // java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(true);
        // getSupportActionBar().setTitle(R.string.add_item_screen_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                // finish(); - альтернатива
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // private void initData() {
    //     data.add(new Item("Название", "Учёт бюджета"));
    //     data.add(new Item("Автор", "Никитин Александр"));
    //     data.add(new Item("Версия", "1.0.0-beta"));
    //     data.add(new Item("Сборка", "4312312-11-22"));
    // }

    class Item {
        String name;
        String value;

        Item(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
