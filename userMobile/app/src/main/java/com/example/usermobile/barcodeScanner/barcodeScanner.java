package com.example.usermobile.barcodeScanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.usermobile.DatabaseManager.DatabaseStorageManager;
import com.example.usermobile.Notification.CustomNotification;
import com.example.usermobile.Notification.CustomNotificationManager;
import com.example.usermobile.ProductAddition.AddProductManually;
import com.example.usermobile.R;
import com.example.usermobile.Settings.SettingsMenu;
import com.example.usermobile.Storage.Product;
import com.example.usermobile.Storage.StorageListView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class barcodeScanner extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final int DURATION = Toast.LENGTH_LONG;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView barcodeText, expiryDate, selectedDate;
    private String barcodeData;
    private WebRequest webRequest;
    private Product jsonProduct;
    private DatePicker datePicker;
    private Button okBtn, manualBtn;
    private DatabaseStorageManager databaseStorageManager;
    private CustomNotificationManager customNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_scanner);

        databaseStorageManager = new DatabaseStorageManager(this);
        customNotificationManager = new CustomNotificationManager(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.barcodeScanner_nav);

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
                    case R.id.barcodeScanner_nav:
                        return true;
                }

                return true;
            }
        });

        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        barcodeText.setVisibility(View.VISIBLE);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        expiryDate = findViewById(R.id.expiry_date);
        selectedDate = findViewById(R.id.selected_Date);
        manualBtn = (Button) findViewById(R.id.manualBtn);
        okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pickExpiryDate = datePicker.getYear() + "-";
                if ((datePicker.getMonth() + 1) < 10) {
                    pickExpiryDate += "0" + (datePicker.getMonth() + 1);
                } else {
                    pickExpiryDate += (datePicker.getMonth() + 1);
                }
                if ((datePicker.getDayOfMonth() < 10)) {
                    pickExpiryDate += "-0" + datePicker.getDayOfMonth();
                } else {
                    pickExpiryDate += "-" + datePicker.getDayOfMonth();
                }
                jsonProduct.setExpirationDate(pickExpiryDate);
                sendToDatabase();

//                selectedDate.setText("Selected date: " + datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear());
                barcodeText.setText("Scan something...");
                datePicker.setVisibility(View.GONE);
                surfaceView.setVisibility(View.VISIBLE);
                barcodeText.setVisibility(View.VISIBLE);
                expiryDate.setVisibility(View.GONE);
                selectedDate.setVisibility(View.GONE);
            }
        });

        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddProductManually.class));
            }
        });

        expiryDate.setVisibility(View.GONE);
        selectedDate.setVisibility(View.GONE);
        datePicker.setVisibility(View.GONE);
        barcodeText.setVisibility(View.VISIBLE);
        initialiseDetectorsAndSources();
    }

    private void initialiseDetectorsAndSources() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(barcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(barcodeScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeText.post(() -> {
                        if (barcodes.valueAt(0).email != null) {
                            barcodeText.removeCallbacks(null);
                            barcodeData = barcodes.valueAt(0).email.address;
                            webRequest = new WebRequest();
                            jsonProduct = webRequest.sentWebRequest(barcodeData);
//                            Toast toast = Toast.makeText(getApplicationContext(), jsonProducts.getName(), DURATION);
//                            toast.show();
                            if (jsonProduct.getName() != "") {
                                surfaceView.setVisibility(View.GONE);
                                barcodeText.setVisibility(View.GONE);

                                expiryDate.setVisibility(View.VISIBLE);
                                datePicker.setVisibility(View.VISIBLE);
                            } else {
                                barcodeText.setText("Product does not exist in our database.");
                            }
                        } else {
                            barcodeData = barcodes.valueAt(0).displayValue;
                            webRequest = new WebRequest();
                            jsonProduct = webRequest.sentWebRequest(barcodeData);
//                            Toast toast = Toast.makeText(getApplicationContext(), jsonProducts.getName(), DURATION);
//                            toast.show();
                            if (jsonProduct.getName() != "") {
                                surfaceView.setVisibility(View.GONE);
                                barcodeText.setVisibility(View.GONE);

                                expiryDate.setVisibility(View.VISIBLE);
                                datePicker.setVisibility(View.VISIBLE);
                            } else {
                                barcodeText.setText("Product does not exist in our database.");
                            }
                        }
                    });
                }
            }
        });
    }

    void sendToDatabase() {
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseStorageManager.addProduct(userID, jsonProduct);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(jsonProduct.getExpirationDate());
            Calendar rightNow = Calendar.getInstance();

            date.setHours(rightNow.get(Calendar.HOUR_OF_DAY));
            date.setMinutes(rightNow.get(Calendar.MINUTE));
            date.setSeconds(rightNow.get(Calendar.SECOND));

            long time = date.getTime() + 10*1000;

            CustomNotification customNotification =
                    new CustomNotification("Produs expirat", jsonProduct.getName() + " expira azi.", time);

            customNotificationManager.sendNotification(customNotification);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Objects.requireNonNull(getSupportActionBar()).hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Objects.requireNonNull(getSupportActionBar()).hide();
        initialiseDetectorsAndSources();
    }
}