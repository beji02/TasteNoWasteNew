package com.example.usermobile.barcodeScanner;

import android.util.JsonReader;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("deprecation")
public class WebRequest extends AppCompatActivity {

    public String response = "";

    private static String streamToString(InputStream inputStream) {
        return new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
    }

    public String sentWebRequest(String barcode) {

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

            response = readJsonStream(inStream).toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return response;
    }

    public List<String> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<String> readMessagesArray(JsonReader reader) throws IOException {
        List<String> messages = new ArrayList<String>();

        reader.beginObject();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endObject();
        return messages;
    }

    public String readMessage(JsonReader reader) throws IOException {
        String code = "";
        String productInfo = "";

//        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("code")) {
                code = reader.nextString();
            } else if (name.equals("product")) {
                productInfo = readProduct(reader);
            } else {
                reader.skipValue();
            }
        }
//        reader.endObject();
        String product;
        product = code + ";" + productInfo;

        return product;
    }

    public String readProduct(JsonReader reader) throws IOException {
        String product = "";
        String category = "", packages = "";
        String productImageUrl = "", productName = "", quantity = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("compared_to_category")) {
                category = reader.nextString();
            } else if (name.equals("image_front_url")) {
                productImageUrl = reader.nextString();
            } else if (name.equals("packaging")) {
                packages = reader.nextString();
            } else if (name.equals("product_name")) {
                productName = reader.nextString();
            } else if (name.equals("quantity")) {
                quantity = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        product = category + ";" + packages + ";" + productImageUrl + ";" + productName + ";" + quantity + ';';
        return product;
    }
}
