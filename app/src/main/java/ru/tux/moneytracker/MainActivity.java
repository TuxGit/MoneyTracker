package ru.tux.moneytracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ItemListFragment.OnRecyclerScrollListener {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private FloatingActionButton fab;

    private ActionMode actionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        // MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), this);
        // viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = null;
                int currentPage = viewPager.getCurrentItem();

                if (currentPage == MainPageAdapter.PAGE_INCOMES) {
                    type = Record.TYPE_INCOMES;
                } else if (currentPage == MainPageAdapter.PAGE_EXPENSES) {
                    type = Record.TYPE_EXPENSES;
                }

                // явный intent
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra(AddItemActivity.TYPE_KEY, type);
                startActivityForResult(intent, ItemListFragment.ADD_ITEM_REQUEST_CODE);
            }
        });

        // Log.d(TAG, "onCreate");
    }

    // @Override
    // protected void onStart() {
    //     super.onStart();
    //
    //     // Log.d(TAG, "onStart");
    // }
    //
    // @Override
    // protected void onResume() {
    //     super.onResume();
    //
    //     // Log.d(TAG, "onResume");
    // }
    //
    // @Override
    // protected void onPause() {
    //     super.onPause();
    //
    //     // Log.d(TAG, "onPause");
    // }
    //
    // @Override
    // protected void onStop() {
    //     super.onStop();
    //
    //     // Log.d(TAG, "onStop");
    // }
    //
    // @Override
    // protected void onDestroy() {
    //     super.onDestroy();
    //
    //     // Log.d(TAG, "onDestroy");
    // }

    @Override
    protected void onResume() {
        // Log.i(TAG, BuildConfig.BUILD_TIME);
        // Log.i(TAG, getString(R.string.build_time));

        super.onResume();

        if (((App) getApplication()).isAuthorized()) {
            initTabs();
        } else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        }
    }

    private void initTabs() {
        if (viewPager.getAdapter() == null) {
            MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case MainPageAdapter.PAGE_INCOMES:
            case MainPageAdapter.PAGE_EXPENSES:
                fab.show();
                break;
            case MainPageAdapter.PAGE_BALANCE:
                fab.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                fab.setEnabled(true);
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                if (actionMode != null) {
                    actionMode.finish();
                }
                fab.setEnabled(false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);

        fab.hide();
        actionMode = mode;
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);

        fab.show();
        actionMode = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // return super.onOptionsItemSelected(item);
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.logout: {
                signOut();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void signOut() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("methodName","signOut");
        startActivity(intent);
        // final MainActivity self = this;
        // App app = (App) getApplication();
        // app.getGoogleSignInClient().signOut()
        //     .addOnCompleteListener(this, new OnCompleteListener<Void>() {
        //         @Override
        //         public void onComplete(@NonNull Task<Void> task) {
        //             // ...
        //             Intent intent = new Intent(self, AuthActivity.class);
        //             startActivity(intent);
        //         }
        //     });
    }

    /*    IMPLEMENT FRAGMENT INTERFACE     */

    @Override
    public void onRecyclerScroll(int dy) {
        if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
            fab.hide();
        } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
            fab.show();
        }
    }
}
