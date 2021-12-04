package com.example.usermobile.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.example.usermobile.Notification.CustomNotificationManager;
import com.example.usermobile.ProductAddition.AddProductManually;
import com.example.usermobile.ProductAddition.ProductAdditionMenu;
import com.example.usermobile.R;
import com.example.usermobile.Storage.StorageListView;
import com.example.usermobile.barcodeScanner.barcodeScanner;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsMenu extends AppCompatActivity {

    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        boolean notify_saved = sharedPreferences.getBoolean("notifyME", true);

        
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.settingsMenu);

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
                    case R.id.barcodeScanner_nav:
                        startActivity(new Intent(getApplicationContext(), barcodeScanner.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return true;
            }
        });

        sw = findViewById(R.id.swNotify);
        sw.setChecked(notify_saved);
        CustomNotificationManager.NOTIFY_ON = notify_saved;

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomNotificationManager.NOTIFY_ON = sw.isChecked();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("notifyME", sw.isChecked());
                editor.commit();

                //Toast.makeText(getApplicationContext(), Boolean.toString(CustomNotificationManager.NOTIFY_ON), Toast.LENGTH_SHORT).show();
            }
        });

    }

   
}