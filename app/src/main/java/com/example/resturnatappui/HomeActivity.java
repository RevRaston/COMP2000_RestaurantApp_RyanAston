package com.example.resturnatappui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout reservationsContainer;
    private ViewPager2 viewPagerSpecials;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Drawer + Toolbar setup
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();

            if (id == R.id.nav_home) return true;
            else if (id == R.id.nav_menu) startActivity(new Intent(this, MenuActivity.class));
            else if (id == R.id.nav_account) startActivity(new Intent(this, AccountActivity.class));
            else if (id == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });

        // Buttons
        MaterialButton btnSeeMenu = findViewById(R.id.btnSeeMenu);
        MaterialButton btnBookNow = findViewById(R.id.btnBookNow);
        reservationsContainer = findViewById(R.id.reservationsContainer);

        btnSeeMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ReservationActivity.class);
            startActivity(intent);
        });

        // Specials / Deals carousel
        viewPagerSpecials = findViewById(R.id.viewPagerSpecials);
        List<String> specialsList = new ArrayList<>();
        specialsList.add("🍝 Pasta Deal - 2 for £10");
        specialsList.add("🥩 Steak Night - Fridays Only!");
        specialsList.add("🍰 Dessert Special - Free with any main");
        specialsList.add("🍕 Family Pizza Offer - £20 Meal Deal");

        SpecialAdapter adapter = new SpecialAdapter(specialsList);
        viewPagerSpecials.setAdapter(adapter);

        // Auto-scroll setup
        autoScrollHandler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPagerSpecials.getCurrentItem() + 1) % specialsList.size();
                viewPagerSpecials.setCurrentItem(nextItem, true);
                autoScrollHandler.postDelayed(this, 4000); // every 4 seconds
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 4000);

        loadReservations();
    }

    private void loadReservations() {
        reservationsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < ReservationData.reservations.size(); i++) {
            ReservationData.Reservation res = ReservationData.reservations.get(i);
            View item = inflater.inflate(R.layout.item_reservation, reservationsContainer, false);

            TextView info = item.findViewById(R.id.tvReservationInfo);
            MaterialButton btnEdit = item.findViewById(R.id.btnEdit);
            MaterialButton btnDelete = item.findViewById(R.id.btnDelete);

            String name = ReservationData.accountName.equals("NULL") ? "(No Name)" : ReservationData.accountName;
            info.setText(name + "\n" + res.date + "  " + res.time + "\nParty Size: " + res.guests);

            int index = i;
            btnEdit.setOnClickListener(v -> {
                Intent editIntent = new Intent(HomeActivity.this, ReservationActivity.class);
                editIntent.putExtra("edit_index", index);
                startActivity(editIntent);
            });

            btnDelete.setOnClickListener(v -> {
                ReservationData.reservations.remove(index);
                loadReservations();
            });

            reservationsContainer.addView(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }
}
