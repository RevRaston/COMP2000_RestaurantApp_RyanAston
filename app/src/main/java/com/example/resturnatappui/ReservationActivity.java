package com.example.resturnatappui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {

    // Helper to resolve view IDs by name (avoids R.id.* editor errors)
    private <T> T findByName(String idName) {
        int id = getResources().getIdentifier(idName, "id", getPackageName());
        if (id == 0) {
            Toast.makeText(this, "Missing view id: " + idName, Toast.LENGTH_SHORT).show();
            return null;
        }
        //noinspection unchecked
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations); // keep your XML name

        // Drawer + Toolbar
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (id == R.id.nav_menu) {
                startActivity(new Intent(this, MenuActivity.class));
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(this, AccountActivity.class));
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });

        // --- UI elements resolved by name (runtime), not R.id.* (compile-time) ---
        TextInputEditText etDate   = findByName("etDate");
        TextInputEditText etTime   = findByName("etTime");
        TextInputEditText etGuests = findByName("etGuests");
        MaterialButton btnConfirm  = findByName("btnConfirmReservation");
        TextView tvBackToMenu      = findByName("tvBackToMenu");

        // Guard in case layout was changed and IDs are missing
        if (etDate == null || etTime == null || etGuests == null || btnConfirm == null || tvBackToMenu == null) {
            // Don’t crash; just stop wiring listeners if something is missing
            return;
        }

        // Date Picker
        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, y, m, d) -> etDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", d, (m + 1), y)),
                    year, month, day
            );
            datePicker.show();
        });

        // Time Picker
        etTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(
                    this,
                    (view, h, m) -> etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m)),
                    hour, minute, true
            );
            timePicker.show();
        });

        // Confirm Reservation
        btnConfirm.setOnClickListener(v -> {
            String date = etDate.getText() == null ? "" : etDate.getText().toString().trim();
            String time = etTime.getText() == null ? "" : etTime.getText().toString().trim();
            String guestsStr = etGuests.getText() == null ? "" : etGuests.getText().toString().trim();

            if (date.isEmpty() || time.isEmpty() || guestsStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int numGuests;
            try {
                numGuests = Integer.parseInt(guestsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid number of guests", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add reservation
            ReservationData.reservations.add(new ReservationData.Reservation(date, time, numGuests));

            // Trigger staff-side notification counters
            NotificationData.staffHasNotification = true;
            NotificationData.staffNewReservationCount++;

            Toast.makeText(this, "Reservation confirmed!", Toast.LENGTH_SHORT).show();

            // Back to Home
            startActivity(new Intent(ReservationActivity.this, HomeActivity.class));
            finish();
        });

        // Back to Menu
        tvBackToMenu.setOnClickListener(v -> {
            startActivity(new Intent(ReservationActivity.this, MenuActivity.class));
            finish();
        });
    }
}
