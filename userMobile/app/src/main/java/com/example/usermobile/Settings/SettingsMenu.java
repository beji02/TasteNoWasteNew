package com.example.usermobile.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.example.usermobile.Authentication.Activities.AdditionalDataActivity;
import com.example.usermobile.Authentication.Activities.LoginActivity;
import com.example.usermobile.Notification.CustomNotificationManager;
import com.example.usermobile.ProductAddition.AddProductManually;
import com.example.usermobile.ProductAddition.ProductAdditionMenu;
import com.example.usermobile.R;
import com.example.usermobile.Storage.StorageListView;
import com.example.usermobile.barcodeScanner.barcodeScanner;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SettingsMenu extends AppCompatActivity {

    Switch sw;
    TextView tvNameSettings;
    TextView tvEmailSettings;
    TextView editName;
    TextView editPhone;
    Button btnLogout;
    Button btnModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue(String.class);
                    String name  = snapshot.child("name").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    tvNameSettings.setText(name);
                    tvEmailSettings.setText(email);
                    editName.setText(name);
                    editPhone.setText(phoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(valueEventListener);

        setContentView(R.layout.settings_activity);


        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        boolean notify_saved = sharedPreferences.getBoolean("notifyME", true);

        
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.settingsMenu);

        tvNameSettings = findViewById(R.id.tvNameSettings);
        tvEmailSettings = findViewById(R.id.tvEmailSettings);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);

        btnLogout = findViewById(R.id.btnLogout);
        btnModify = findViewById(R.id.btnModify);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                //Toast.makeText(getApplicationContext(), Boolean.toString(CustomNotificationManager.NOTIFY_ON), Toast.LENGTH_SHORT).show();
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameField = editName.getText().toString();
                String phoneField = editPhone.getText().toString();
                String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(userID).child("name").setValue(nameField).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsMenu.this,
                                "Name succesfully updated.", Toast.LENGTH_LONG).show();
                        tvNameSettings.setText(nameField);
                    } else {
                        Toast.makeText(SettingsMenu.this, "Failed to modify name.",
                                Toast.LENGTH_LONG).show();
                    }
                });
                try {
                    TimeUnit.MILLISECONDS.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(userID).child("phoneNumber").setValue(phoneField).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsMenu.this,
                                "Phone number succesfully updated.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SettingsMenu.this, "Failed to modify phone number.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

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