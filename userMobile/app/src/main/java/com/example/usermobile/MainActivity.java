package com.example.usermobile;

import android.content.Intent;
import android.os.Bundle;

import com.example.usermobile.Authentication.Activities.LoginActivity;
import com.example.usermobile.Notification.CustomNotification;
import com.example.usermobile.Notification.CustomNotificationManager;
import com.example.usermobile.Storage.StorageListAdapter;
import com.example.usermobile.Storage.StorageListView;
import com.example.usermobile.barcodeScanner.barcodeScanner;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.usermobile.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, StorageListView.class));

//        //cod de test Bejan
//        //afiseaza o notificare dupa 10 secunde de la rulare
//        CustomNotificationManager myNotificationManager = new CustomNotificationManager(this);
//        long time = System.currentTimeMillis();
//        CustomNotification myNotification = new CustomNotification("heiRadu", "cmf raducule", time + 1000*10*6);
//        myNotificationManager.sendNotification(myNotification);
//        Toast.makeText(this,"Notification Set!", Toast.LENGTH_SHORT).show();
    }
}