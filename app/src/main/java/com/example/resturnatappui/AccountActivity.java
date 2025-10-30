package com.example.resturnatappui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Drawer + Toolbar
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (id == R.id.nav_menu) {
                startActivity(new Intent(this, MenuActivity.class));
            } else if (id == R.id.nav_account) {
                // already here
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });

        // Account page controls
        SwitchMaterial switchNotifications = findViewById(R.id.switchNotifications);
        MaterialButton btnLight = findViewById(R.id.btnLight);
        MaterialButton btnDark = findViewById(R.id.btnDark);
        MaterialButton btnSignOut = findViewById(R.id.btnSignOut);
        TextInputEditText etName = findViewById(R.id.etChangeName);
        TextInputEditText etPassword = findViewById(R.id.etChangePassword);

        // Notification toggle
        switchNotifications.setOnCheckedChangeListener((b, checked) ->
                Toast.makeText(this, checked ? "Notifications ON" : "Notifications OFF", Toast.LENGTH_SHORT).show());

        // Theme buttons
        btnLight.setOnClickListener(v ->
                Toast.makeText(this, "Light mode coming soon!", Toast.LENGTH_SHORT).show());
        btnDark.setOnClickListener(v ->
                Toast.makeText(this, "Dark mode coming soon!", Toast.LENGTH_SHORT).show());

        // Save name dynamically when user stops editing
        etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newName = etName.getText().toString().trim();
                if (!newName.isEmpty()) {
                    ReservationData.accountName = newName;
                    Toast.makeText(this, "Name updated to " + newName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sign out
        btnSignOut.setOnClickListener(v -> {
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            finish();
        });
    }
}
