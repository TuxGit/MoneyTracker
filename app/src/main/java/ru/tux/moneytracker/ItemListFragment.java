package ru.tux.moneytracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListFragment extends Fragment {
    private static final String TAG = "ItemListFragment";

    private static final String TYPE_KEY = "type";

    private static final int ADD_ITEM_REQUEST_CODE = 101;

    private String type;
    private RecyclerView recycler;
    private ItemListAdapter adapter;

    private FloatingActionButton fab;
    private SwipeRefreshLayout refresh;

    private Api api;

    public static ItemListFragment createItemListFragment(String type) {
        ItemListFragment fragment = new ItemListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ItemListFragment.TYPE_KEY, type);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemListAdapter();

        Bundle bundle = getArguments();
        type = bundle.getString(TYPE_KEY, Record.TYPE_UNKNOWN);

        if (type.equals(Record.TYPE_UNKNOWN)) {
            throw new IllegalArgumentException("Unknown type");
        }

        api = ((App) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.list);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // неявный intent
               // Intent intent = new Intent();
               // intent.setAction(Intent.ACTION_VIEW);
               // intent.setData(Uri.parse("https://pikabu.ru"));
               // startActivity(intent);

                // явный intent
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.TYPE_KEY, type);
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
            }
        });

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.GREEN);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        loadItems();
    }

    private void loadItems() {
        Call<List<Record>> call = api.getItems(type);

        call.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                adapter.setData(response.body());
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final Record record = data.getParcelableExtra("record");
            // adapter.addItem(record);

            // test - POST request
            Call<Void> callCreate = api.createItem(record);
            callCreate.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d(TAG, response.headers().toString());
                    if (response.isSuccessful()) {
                        // int pos = 0;
                        adapter.addItem(record, true);
                        // show added item by scrolling top
                        // recycler.smoothScrollToPosition(0); // error -> RecyclerView: Passed over target position while smooth scrolling.
                        recycler.scrollToPosition(0);

                        Log.d(TAG, "onResponse: success, code=" + String.valueOf(response.code()));
                    } else {
                        Log.d(TAG, "onResponse: error, code=" + String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d(TAG, "onFailure: createItem");
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
