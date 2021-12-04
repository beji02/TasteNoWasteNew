package com.example.usermobile.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.example.usermobile.ProductAddition.ProductAdditionMenu;
import com.example.usermobile.R;
import com.example.usermobile.Storage.StorageListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.settingsMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.storageListView:
                        startActivity(new Intent(getApplicationContext(), StorageListView.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settingsMenu:
                        return true;
                    case R.id.productAdditionMenu:
                        startActivity(new Intent(getApplicationContext(), ProductAdditionMenu.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return true;
            }
        });
    }

   
}