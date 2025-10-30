package com.example.resturnatappui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private boolean isStaffMode = false; // default to Guest login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI references
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvForgot = findViewById(R.id.tvForgot);
        TextView tvCreate = findViewById(R.id.tvCreate);
        TabLayout tabLoginType = findViewById(R.id.tabLoginType);

        // Setup Guest / Staff tabs
        tabLoginType.addTab(tabLoginType.newTab().setText("Guest"));
        tabLoginType.addTab(tabLoginType.newTab().setText("Staff"));

        tabLoginType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isStaffMode = tab.getPosition() == 1; // 0 = Guest, 1 = Staff
                if (isStaffMode) {
                    Toast.makeText(LoginActivity.this, "Staff login selected", Toast.LENGTH_SHORT).show();
                    tvCreate.setEnabled(false);
                    tvCreate.setAlpha(0.4f); // fade it
                } else {
                    Toast.makeText(LoginActivity.this, "Guest login selected", Toast.LENGTH_SHORT).show();
                    tvCreate.setEnabled(true);
                    tvCreate.setAlpha(1f);
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Login button logic
        btnLogin.setOnClickListener(v -> {
            if (isStaffMode) {
                // STAFF login → Staff Dashboard
                Intent intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                // GUEST login → Home
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Register navigation
        tvCreate.setOnClickListener(v -> {
            if (isStaffMode) {
                Toast.makeText(this, "Staff cannot register via app", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Forgot password navigation
        tvForgot.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}
