package com.example.usermobile.barcodeScanner;

import android.util.JsonReader;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.usermobile.Storage.Product;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebRequest extends AppCompatActivity {

    public Product response;

    // set web connection and get json input
    public Product sentWebRequest(String barcode) {

        String liveJSON = "https://world.openfoodfacts.org/api/v0/product/[barcode].json";
        String requestURL = liveJSON.replace("[barcode]", barcode);

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.connect();
            InputStream inStream = conn.getInputStream();

            response = readJsonStream(inStream);
        } catch (IOException ex) {
            Toast toast = Toast.makeText(getApplicationContext(), "Database access denied. Please try again later.", Toast.LENGTH_LONG);
            toast.show();
            ex.printStackTrace();
        }

        return response;
    }

    // read json information
    public Product readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            return readMessage(reader);
        } finally {
            reader.close();
        }
    }

    // identify json element based on id
    public Product readMessage(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("product")) {
                return readProduct(reader);
            } else {
                reader.skipValue();
            }
        }

        return new Product("", 0, "", "", "", "", "");
    }

    // take data from json array element, based on id
    public Product readProduct(JsonReader reader) throws IOException {
        String[] categories = new String[0];
        String[] packages = new String[0];
        String productImageUrl = "", productName = "";
        String[] productQuantity = new String[0];
        String quantity = "0", unitOfMeasure = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("categories")) {
                categories = reader.nextString().split("\\s*,\\s*");
            } else if (name.equals("image_front_url")) {
                productImageUrl = reader.nextString();
            } else if (name.equals("packaging")) {
                packages = reader.nextString().split("\\s*,\\s*");
            } else if (name.equals("product_name")) {
                productName = reader.nextString();
            } else if (name.equals("quantity")) {
                productQuantity = reader.nextString().split("\\s* \\s*");
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if (!productQuantity[0].isEmpty()) {
            quantity = productQuantity[0];
        }
        if (productQuantity.length > 1) {
            unitOfMeasure = productQuantity[1];
        }

        Product product = new Product(productName,
                Integer.parseInt(quantity),
                unitOfMeasure,
                "",
                StringUtils.capitalize(categories[0].replace('-', ' ')),
                packages[0],
                productImageUrl);
        return product;
    }
}
