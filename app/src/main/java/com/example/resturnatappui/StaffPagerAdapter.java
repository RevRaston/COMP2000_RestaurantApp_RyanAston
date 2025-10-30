package com.example.resturnatappui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StaffPagerAdapter extends FragmentStateAdapter {

    public StaffPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new StaffReservationsFragment();
        else return new StaffMenuFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
