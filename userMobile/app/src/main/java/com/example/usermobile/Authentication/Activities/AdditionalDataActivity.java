package com.example.usermobile.Authentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usermobile.Authentication.Util.Address;
import com.example.usermobile.Authentication.Util.Location;
import com.example.usermobile.Authentication.Util.User;
import com.example.usermobile.MainActivity;
import com.example.usermobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class AdditionalDataActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText etAdditionalDataCountry;
    private EditText etAdditionalDataCounty;
    private EditText etAdditionalDataCity;
    private EditText etAdditionalDataAddress;
    private EditText etAdditionalDataZipcode;

    private Button btnAdditionalDataSkip;
    private Button btnAdditionalDataSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_data);

        etAdditionalDataCountry = (EditText) findViewById(R.id.etAdditionalDataCountry);
        etAdditionalDataCounty = (EditText) findViewById(R.id.etAdditionalDataCounty);
        etAdditionalDataCity = (EditText) findViewById(R.id.etAdditionalDataCity);
        etAdditionalDataAddress = (EditText) findViewById(R.id.etAdditionalDataAddress);
        etAdditionalDataZipcode = (EditText) findViewById(R.id.etAdditionalDataZipcode);

        btnAdditionalDataSkip = (Button) findViewById(R.id.btnAdditionalDataSkip);
        btnAdditionalDataSave = (Button) findViewById(R.id.btnAdditionalDataSave);


        btnAdditionalDataSkip.setOnClickListener(this);
        btnAdditionalDataSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnAdditionalDataSkip:
                startActivity(new Intent(AdditionalDataActivity.this, MainActivity.class));
                break;
            case R.id.btnAdditionalDataSave:
                updateData();
                break;
        }
    }

    private void updateData() {
        // no need for null checks
        String country = etAdditionalDataCountry.getText().toString().trim();
        String county  = etAdditionalDataCounty.getText().toString().trim();
        String city    = etAdditionalDataCity.getText().toString().trim();
        String address = etAdditionalDataAddress.getText().toString().trim();
        String zipcode = etAdditionalDataZipcode.getText().toString().trim();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Address fullAddress = new Address(address, city, county, country, zipcode);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).child("Address").setValue(fullAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(AdditionalDataActivity.this,
                            "Address has been updated",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdditionalDataActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AdditionalDataActivity.this,"Failed to add data",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //here there is needed to implement getting user location

        Location location = new Location(45.0, 45.0);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).child("Location").setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(AdditionalDataActivity.this,
                            "All data is complete. Thank you for using our app!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdditionalDataActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AdditionalDataActivity.this,"Failed to add data. Try again!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}