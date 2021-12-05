package com.example.usermobile.Authentication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.usermobile.Authentication.Util.Address;
import com.example.usermobile.Authentication.Util.Location;
import com.example.usermobile.R;
import com.example.usermobile.Storage.StorageListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AdditionalDataActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etAdditionalDataCountry;
    private EditText etAdditionalDataCounty;
    private EditText etAdditionalDataCity;
    private EditText etAdditionalDataAddress;
    private EditText etAdditionalDataZipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_additional_data);


        etAdditionalDataCountry = findViewById(R.id.etAdditionalDataCountry);
        etAdditionalDataCounty = findViewById(R.id.etAdditionalDataCounty);
        etAdditionalDataCity = findViewById(R.id.etAdditionalDataCity);
        etAdditionalDataAddress = findViewById(R.id.etAdditionalDataAddress);
        etAdditionalDataZipcode = findViewById(R.id.etAdditionalDataZipcode);

        Button btnAdditionalDataSkip = findViewById(R.id.btnAdditionalDataSkip);
        Button btnAdditionalDataSave = findViewById(R.id.btnAdditionalDataSave);


        btnAdditionalDataSkip.setOnClickListener(this);
        btnAdditionalDataSave.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdditionalDataSkip:
                startActivity(new Intent(AdditionalDataActivity.this, StorageListView.class));
                break;
            case R.id.btnAdditionalDataSave:
                updateData();
                break;
        }
    }

    private void updateData() {
        // no need for null checks
        String country = etAdditionalDataCountry.getText().toString().trim();
        String county = etAdditionalDataCounty.getText().toString().trim();
        String city = etAdditionalDataCity.getText().toString().trim();
        String address = etAdditionalDataAddress.getText().toString().trim();
        String zipcode = etAdditionalDataZipcode.getText().toString().trim();

        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Address fullAddress = new Address(address, city, county, country, zipcode);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).child("Address").setValue(fullAddress).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdditionalDataActivity.this,
                        "Address has been updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdditionalDataActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AdditionalDataActivity.this, "Failed to add your address",
                        Toast.LENGTH_LONG).show();
            }
        });

        Location location = getCurrentLocation();

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).child("Location").setValue(location).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdditionalDataActivity.this,
                        "All data is complete. Thank you for using our app!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdditionalDataActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AdditionalDataActivity.this, "Failed to add your location. Please try again!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private Location getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {
                return getLocation();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Location(45, 45);
    }

    public Location getLocation(){
        GPSTracker gpsTracker = new GPSTracker(AdditionalDataActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            return new Location(latitude, longitude);
        }else{
            gpsTracker.showSettingsAlert();
        }
        gpsTracker.stopUsingGPS();
        return new Location(45, 45);
    }


}