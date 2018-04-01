package ru.tux.moneytracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tux.moneytracker.api.AddItemResult;
import ru.tux.moneytracker.api.Api;
import ru.tux.moneytracker.dialog.ConfirmationDialog;
import ru.tux.moneytracker.dialog.ConfirmationDialogListener;

public class ItemListFragment extends Fragment {
    private static final String TAG = "ItemListFragment";

    private static final String TYPE_KEY = "type";

    public static final int ADD_ITEM_REQUEST_CODE = 101;

    private String type;
    private RecyclerView recycler;
    private ItemListAdapter adapter;

    private SwipeRefreshLayout refresh;

    private FloatingActionButton fab;

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
        adapter.setListener(new AdapterListener());

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

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.GREEN);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearSelections();
                loadItems();
                // Log.d(TAG, "onRefresh: loadItems()");
            }
        });

        // test - work with FAB in child view
        ViewGroup row = (ViewGroup) view.getParent().getParent();
        fab = row.findViewById(R.id.fab);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.d(TAG, "onScrolled recycler: " + String.valueOf(dx) + " / " + String.valueOf(dy));
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        // end test

        loadItems();
        // Log.d(TAG, "onViewCreated: loadItems()");
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

    private void addItem(final Record record) {
        // POST request
        Call<AddItemResult> callCreate = api.createItem(record);
        callCreate.enqueue(new Callback<AddItemResult>() {
            @Override
            public void onResponse(Call<AddItemResult> call, Response<AddItemResult> response) {
                // Log.d(TAG, response.headers().toString());

                if (response.isSuccessful()) {
                    record.id = response.body().id;
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
            public void onFailure(Call<AddItemResult> call, Throwable t) {
                Log.d(TAG, "onFailure: createItem");
            }
        });
    }

    private void removeItems(final List<Integer> items) {
        // todo - вынести в func callbacks
        refresh.setRefreshing(true);

        for (int i = items.size() - 1; i >= 0; i--) {
            final int pos = items.get(i);
            Record record = adapter.getRecordByPosition(pos);

            final int finalI = i;
            Call<Void> callRemove = api.removeItem(record.id);

            callRemove.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // Log.d(TAG, response.headers().toString());
                    if (response.isSuccessful()) {
                        // --itemsCount;
                        items.remove(finalI);
                        if (items.size() == 0) {
                            removeSelectedItems();

                            refresh.setRefreshing(false);
                        }

                        Log.d(TAG, "onResponse callRemove: success, code=" + String.valueOf(response.code()));
                    } else {
                        refresh.setRefreshing(false);

                        Log.d(TAG, "onResponse callRemove: error, code=" + String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    refresh.setRefreshing(false);

                    Log.d(TAG, "onFailure callRemove");
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Record record = data.getParcelableExtra("record");

            // adapter.addItem(record);
            if (record.type.equals(type)) {
                addItem(record);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*    ACTION MODE     */

    ActionMode actionMode = null;

    private void removeSelectedItems() {
        // обходим элементы с конца для корректного удаления
        for (int i = adapter.getSelectedItems().size() - 1; i >= 0; i--) {
            adapter.remove(adapter.getSelectedItems().get(i));
        }

        actionMode.finish();
    }

    private class AdapterListener implements ItemListAdapterListener {

        @Override
        public void onItemClick(Record record, int position, RecyclerView.ViewHolder viewHolder) {
            int pos = viewHolder.getAdapterPosition();

            if (isInActionMode()) {
                toggleSelection(pos);
            }
        }

        @Override
        public void onItemLongClick(Record record, int position, RecyclerView.ViewHolder viewHolder) {
            int pos = viewHolder.getAdapterPosition();

            if (isInActionMode()) {
                return;
            }

            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
            toggleSelection(pos);
        }

        private boolean isInActionMode () {
            return actionMode != null;
        }

        private void toggleSelection(int position) {
            adapter.toggleSelection(position);
        }

    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // actionMode = mode;
            MenuInflater inflater = new MenuInflater(getContext());
            inflater.inflate(R.menu.item_list_menu, menu);
            return true;
            // return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.remove:
                    showDialog();
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
        }
    };


    /*     DIALOG     */

    private void showDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setListener(new ConfirmationDialogListener() {
            @Override
            public void onPositiveClicked(ConfirmationDialog dialog) {
                // refresh.setRefreshing(true);

                // массив с позициями элементов, которые выделены
                List<Integer> items = adapter.getSelectedItems();

                removeItems(items);
            }

            @Override
            public void onNegativeClicked(ConfirmationDialog dialog) {
                // dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), "ConfirmationDialog");
    }

}
