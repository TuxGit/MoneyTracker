package ru.tux.moneytracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tux on 16.03.2018.
 */
class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.RecordViewHolder> {
    private List<Record> data = new ArrayList<>();
    private ItemListAdapterListener listener = null;

    public void setListener(ItemListAdapterListener listener) {
        this.listener = listener;
    }

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
        holder.applyData(record, position, listener, selections.get(position, null) != null);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // private SparseBooleanArray selections = new SparseBooleanArray(); // оптимальнее, чем HashMap
    // private HashMap<Integer, Boolean> selections = new HashMap<>();
    private SparseArray<Record> selections = new SparseArray<>();

    // public void toggleSelection(int position) {
    //     if (selections.get(position, false)) {
    //         selections.delete(position);
    //     } else {
    //         selections.put(position, true);
    //     }
    //     notifyItemChanged(position);
    // }
    public void toggleSelection(int position) {
        if (selections.get(position, null) != null) {
            selections.delete(position);
        } else {
            selections.put(position, getRecordByPosition(position));
        }
        notifyItemChanged(position);
    }

    void clearSelections() {
        selections.clear();
        notifyDataSetChanged();
    }

    int getSelectedItemCount() {
        return selections.size();
    }

    // getSelectedRecordByPosition

    Record getRecordByPosition(int position) {
        Record record = data.get(position);
        return record;
    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selections.size());
        for (int i = 0; i < selections.size(); i++) {
            items.add(selections.keyAt(i));
        }
        return items;
    }

    Record remove(int position) {
        final Record record = data.remove(position);
        notifyItemRemoved(position);
        return record;
    }

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

        // bind
        public void applyData(final Record record, final int position, final ItemListAdapterListener listener, boolean isSelected) {

            title.setText(record.title);
            price.setText(context.getString(R.string.item_price_format, String.valueOf(record.price)));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(record, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onItemLongClick(record, position);
                    }
                    // return false;
                    return true;
                }
            });

            itemView.setActivated(isSelected);
        }
    }
}
