package ru.tux.moneytracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemListFragment extends Fragment {

    private static final String TYPE_KEY = "type";
    private static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_INCOMES = 1;
    public static final int TYPE_EXPENSES = 2;

    private int type;
    private RecyclerView recycler;
    private ItemListAdapter adapter;

    public static ItemListFragment createItemListFragment(int type) {
        ItemListFragment fragment = new ItemListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ItemListFragment.TYPE_KEY, ItemListFragment.TYPE_INCOMES);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemListAdapter();

        Bundle bundle = getArguments();
        type = bundle.getInt(TYPE_KEY, TYPE_UNKNOWN);

        if (type == TYPE_UNKNOWN) {
            throw new IllegalArgumentException("Unknown type");
        }
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
    }
}
