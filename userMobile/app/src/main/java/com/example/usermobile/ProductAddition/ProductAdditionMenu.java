package com.example.usermobile.ProductAddition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.usermobile.R;
import com.example.usermobile.Settings.SettingsMenu;
import com.example.usermobile.Storage.StorageListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductAdditionMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_addition_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.productAdditionMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.storageListView:
                        startActivity(new Intent(getApplicationContext(), StorageListView.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settingsMenu:
                        startActivity(new Intent(getApplicationContext(), SettingsMenu.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.productAdditionMenu:
                        return true;
                }
                return true;
            }
        });
    }
}