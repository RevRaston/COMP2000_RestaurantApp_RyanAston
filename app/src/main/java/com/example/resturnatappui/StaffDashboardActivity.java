package com.example.resturnatappui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StaffDashboardActivity extends AppCompatActivity {

    private final String[] tabTitles = {"Reservations", "Menu Management"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_staff);
        NavigationView navView = findViewById(R.id.navigation_view_staff);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // 🔹 Menu icon opens drawer
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // 🔹 Handle drawer item selection
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();

            if (id == R.id.nav_staff_home) {
                Toast.makeText(this, "You're already on Dashboard", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_staff_reservations) {
                Toast.makeText(this, "Reservations Tab", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_staff_menu) {
                Toast.makeText(this, "Menu Management Tab", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_staff_logout) {
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });

        // ✅ Add notification menu icon programmatically
        toolbar.getMenu().add(0, 1, 0, "Notifications")
                .setIcon(android.R.drawable.stat_notify_more)
                .setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                if (NotificationData.staffNewReservationCount > 0) {
                    Toast.makeText(this,
                            NotificationData.staffNewReservationCount + " new reservations received!",
                            Toast.LENGTH_SHORT).show();
                    NotificationData.resetStaffNotifications();
                    updateNotificationIcon(toolbar);
                } else {
                    Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        // 🔹 Setup Tabs
        TabLayout tabLayout = findViewById(R.id.tabLayoutStaff);
        ViewPager2 viewPager = findViewById(R.id.viewPagerStaff);
        StaffPagerAdapter adapter = new StaffPagerAdapter(this);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        // 🔹 Update notification bell
        toolbar.post(() -> updateNotificationIcon(toolbar));
    }

    // 🔔 Update toolbar bell color
    private void updateNotificationIcon(MaterialToolbar toolbar) {
        android.view.MenuItem item = toolbar.getMenu().findItem(1);
        if (item == null) return;

        if (NotificationData.staffHasNotification) {
            item.setIcon(android.R.drawable.stat_sys_warning); // yellow
        } else {
            item.setIcon(android.R.drawable.stat_notify_more); // grey
        }
    }
}
