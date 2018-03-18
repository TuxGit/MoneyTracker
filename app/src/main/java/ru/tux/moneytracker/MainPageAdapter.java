package ru.tux.moneytracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tux on 16.03.2018.
 */

public class MainPageAdapter extends FragmentPagerAdapter {

    private static final int PAGE_INCOMES = 0;
    private static final int PAGE_EXPENSES = 1;
    private static final int PAGE_BALANCE = 2;

    private String[] titles;

    public MainPageAdapter(FragmentManager fm, Context context) {
        super(fm);

        titles = context.getResources().getStringArray(R.array.tab_title);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_INCOMES:
                return ItemListFragment.createItemListFragment(Record.TYPE_INCOMES);


            case PAGE_EXPENSES:
                return ItemListFragment.createItemListFragment(Record.TYPE_EXPENSES);

            case PAGE_BALANCE:
                return new BalanceFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}