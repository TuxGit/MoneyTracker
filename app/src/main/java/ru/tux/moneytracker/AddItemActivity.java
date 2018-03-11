package ru.tux.moneytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";

    EditText name;
    EditText price;
    Button addBtn;

    TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Log.d(TAG, editable.toString());
            String itemName = name.getText().toString();
            String itemPrice = price.getText().toString();

            if (!TextUtils.isEmpty(itemName) && !TextUtils.isEmpty(itemPrice)) {
                addBtn.setEnabled(true);
            } else {
                addBtn.setEnabled(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        // setTitle(R.string.add_item_title);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        addBtn = findViewById(R.id.add_btn);

        name.addTextChangedListener(watcher);
        price.addTextChangedListener(watcher);

//        name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                addBtn.setEnabled(!TextUtils.isEmpty(s));
//            }
//        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = name.getText().toString();
                String itemPrice = price.getText().toString();

                Log.d(TAG, "onClick: values=" + itemName + ", " + itemPrice);
            }
        });
    }
}
