package com.example.usermobile.barcodeScanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.usermobile.R;
import com.example.usermobile.Storage.Product;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class barcodeScanner extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView barcodeText, expiryDate, selectedDate;
    private String barcodeData;
    private static final int DURATION = Toast.LENGTH_LONG;
    private WebRequest webRequest;
    private Product jsonProduct;
    private DatePicker datePicker;
    private Button cancelBtn, okBtn, manualBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_scanner);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        barcodeText.setVisibility(View.GONE);

        datePicker = (DatePicker)findViewById(R.id.datePicker);
        expiryDate = findViewById(R.id.expiry_date);
        selectedDate = findViewById(R.id.selected_Date);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        manualBtn = (Button)findViewById(R.id.manualBtn);
        okBtn = (Button)findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate.setText("Selected date: " + datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear());
                surfaceView.setVisibility(View.VISIBLE);
                expiryDate.setVisibility(View.GONE);
                selectedDate.setVisibility(View.GONE);
                datePicker.setVisibility(View.GONE);
            }
        });

        expiryDate.setVisibility(View.GONE);
        selectedDate.setVisibility(View.GONE);
        datePicker.setVisibility(View.GONE);
//        barcodeText.setVisibility(View.GONE);
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
                                barcodeText.setText(barcodeData);

                                expiryDate.setVisibility(View.VISIBLE);
                                datePicker.setVisibility(View.VISIBLE);

                                String expiryDate = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                                jsonProduct.setExpirationDate(LocalDate.parse(expiryDate));
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
                                barcodeText.setText(barcodeData);

                                expiryDate.setVisibility(View.VISIBLE);
                                datePicker.setVisibility(View.VISIBLE);

                                String expiryDate = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                                jsonProduct.setExpirationDate(LocalDate.parse(expiryDate));
                            } else {
                                barcodeText.setText("Product does not exist in our database.");
                            }
                        }
                    });
                }
            }
        });
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