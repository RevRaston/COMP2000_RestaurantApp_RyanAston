package com.example.resturnatappui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private ScrollView scrollMenu;
    private LinearLayout menuContainer;

    // 🧭 Store anchors for tab scrolling
    private final Map<Integer, View> sectionAnchors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        scrollMenu = findViewById(R.id.scrollMenu);
        MaterialButton btnBookNow = findViewById(R.id.btnBookNow);
        menuContainer = findViewById(R.id.menuContainer);

        // ✅ Tabs for category navigation
        String[] sections = {"Starters", "Deals", "Specials", "Mains", "Desserts", "Drinks"};
        for (String section : sections)
            tabLayout.addTab(tabLayout.newTab().setText(section));

        // ☰ Drawer setup
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();

            if (id == R.id.nav_home)
                startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.nav_account)
                startActivity(new Intent(this, AccountActivity.class));
            else if (id == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });

        // 📅 Reservation button
        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ReservationActivity.class);
            startActivity(intent);
        });

        // 🔁 Load menu items dynamically
        displayMenuItems();

        // 🧭 Smooth scroll between dynamic anchors
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View anchor = sectionAnchors.get(tab.getPosition());
                if (anchor != null) {
                    scrollMenu.post(() -> scrollMenu.smoothScrollTo(0, anchor.getTop()));
                } else {
                    Toast.makeText(MenuActivity.this, "Section not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // ✅ Display all items grouped by category with dynamic anchors
    private void displayMenuItems() {
        menuContainer.removeAllViews();
        sectionAnchors.clear(); // reset section map

        if (MenuData.menuItems.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No menu items available right now.");
            empty.setTextSize(16);
            empty.setPadding(0, 80, 0, 0);
            empty.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
            menuContainer.addView(empty);
            return;
        }

        int tabIndex = 0;
        for (MenuData.Category category : MenuData.Category.values()) {
            List<MenuData.MenuItem> group = new ArrayList<>();
            for (MenuData.MenuItem item : MenuData.menuItems) {
                if (item.category == category) group.add(item);
            }

            if (group.isEmpty()) {
                tabIndex++;
                continue;
            }

            // 🧭 Category header (acts as scroll anchor)
            TextView header = new TextView(this);
            header.setText(category.name());
            header.setTextSize(18);
            header.setTextColor(getResources().getColor(android.R.color.black));
            header.setPadding(0, 40, 0, 10);
            header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            // Generate unique ID for dynamic anchors
            int viewId = View.generateViewId();
            header.setId(viewId);
            sectionAnchors.put(tabIndex, header);

            menuContainer.addView(header);

            // Menu items below header
            for (MenuData.MenuItem item : group) {
                View itemView = getLayoutInflater().inflate(R.layout.item_menu_card, menuContainer, false);

                TextView nameView = itemView.findViewById(R.id.tvItemName);
                TextView priceView = itemView.findViewById(R.id.tvItemPrice);
                TextView categoryView = itemView.findViewById(R.id.tvItemCategory);

                nameView.setText(item.name);
                priceView.setText(String.format(Locale.UK, "£%.2f", item.price));
                categoryView.setText(category.name());

                menuContainer.addView(itemView);
            }

            tabIndex++;
        }

        // ✅ Ensure scroll view fully updates (so Desserts & Drinks show)
        menuContainer.post(() -> {
            menuContainer.requestLayout();
            scrollMenu.fullScroll(View.FOCUS_DOWN);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh menu after staff changes
        displayMenuItems();
    }
}
