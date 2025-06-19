package com.example.matchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private int userID;
    private String username;
    private String phone;
    private String country;
    private String password;

    private TextView textWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userId", -1);
        username = intent.getStringExtra("username");
        phone = intent.getStringExtra("phone");
        country = intent.getStringExtra("country");
        password = intent.getStringExtra("password");

        bottomNav = findViewById(R.id.bottom_nav);
        textWelcome = findViewById(R.id.toolbar_welcome);

        textWelcome.setText("Bienvenido " + username);

        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("userId", userID);
            bundle.putString("username", username);
            bundle.putString("phone", phone);
            bundle.putString("country", country);
            bundle.putString("password", password);
            homeFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
        }

        bottomNav.setOnItemSelectedListener((MenuItem item) -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();

            } else if (id == R.id.nav_orders) {
                selectedFragment = new OrdersFragment();

            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userID);
                bundle.putString("username", username);
                bundle.putString("phone", phone);
                bundle.putString("country", country);
                bundle.putString("password", password);
                selectedFragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}
