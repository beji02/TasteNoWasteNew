package com.example.usermobile.barcodeScanner;

import androidx.appcompat.app.AppCompatActivity;

public class WebRequest extends AppCompatActivity {

    public void sentWebRequest(String barcode) {

        String liveJSON = "https://world.openfoodfacts.org/api/v0/product/[barcode].json";
        String requestURL = liveJSON.replace("[barcode]", barcode);
    }
}
