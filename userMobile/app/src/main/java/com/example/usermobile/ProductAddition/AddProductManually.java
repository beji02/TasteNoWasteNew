package com.example.usermobile.ProductAddition;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.usermobile.DatabaseManager.DatabaseStorageManager;
import com.example.usermobile.Notification.CustomNotification;
import com.example.usermobile.Notification.CustomNotificationManager;
import com.example.usermobile.R;
import com.example.usermobile.Storage.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddProductManually extends AppCompatActivity {
    /*
        TASKS:
            Activitate care citeste: Nume, Quantity, expirationDate, ambalaje cu dropdown, categorii cu dropdown
            O clasa Product cu atributele (Id, Nume, Quantity, expirationDate, ambalaje cu dropdown, categorii cu dropdown)
            StorageDatabase - clasa care se ocupa cu store User's Storage in firebase database:
                functions: storeProduct(idUser, Product), deleteProduct(idUser, idProduct)
         */

    String[] itemsPackage = {"Paper", "Glass", "Plastic", "Cardboard", "Metal", "Unknown"};
    String[] itemsCategory = {
            "Snacks",
            "Beverages",
            "Dairies",
            "Cereals",
            "Meats",
            "Fruits",
            "Vegetables",
            "Cheeses",
            "Desserts",
            "Seafood",
            "Condiments",
            "Fishes",
            "Wines",
            "Pastas",
            "Unknown"};
    String productName;
    String productPackage;
    String productCategory;

    DatabaseStorageManager databaseStorageManager;

    private CustomNotificationManager customNotificationManager;


    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    AutoCompleteTextView autoCompleteTextView2;
    ArrayAdapter<String> arrayAdapter2;

    private EditText etNume, etQuantity;
    private DatePicker dpExpirationDate;
    private ListView lvPackage, lvCategory;
    private Button bAdd, bCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_manually);



        databaseStorageManager = new DatabaseStorageManager(this);
        customNotificationManager = new CustomNotificationManager(this);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item_b, itemsPackage);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                productPackage = item;
            }
        });

        autoCompleteTextView2 = findViewById(R.id.auto_complete_txt2);
        arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.list_item_b, itemsCategory);
        autoCompleteTextView2.setAdapter(arrayAdapter2);
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                productCategory = item;
            }
        });

        etNume = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        dpExpirationDate = findViewById(R.id.datePicker2);

        bAdd = findViewById(R.id.addBtn2);
        bCancel = findViewById(R.id.cancelBtn2);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToDatabase();
                //Toast.makeText(getApplicationContext(), "ProductAdded", Toast.LENGTH_SHORT).show();
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void sendToDatabase() {
        productName = etNume.getText().toString();
        String[] productQuantity;
        String quantity = "0", unitOfMeasure = "";

        productQuantity = etQuantity.getText().toString().split("\\s* \\s*");
        quantity = productQuantity[0];
        if (productQuantity.length > 1) {
            unitOfMeasure = productQuantity[1];
        }

        String productExpirationDate = dpExpirationDate.getYear() + "-";
        if ((dpExpirationDate.getMonth() + 1) < 10) {
            productExpirationDate += "0" + (dpExpirationDate.getMonth() + 1);
        } else {
            productExpirationDate += (dpExpirationDate.getMonth() + 1);
        }
        if ((dpExpirationDate.getDayOfMonth() < 10)) {
            productExpirationDate += "-0" + dpExpirationDate.getDayOfMonth();
        } else {
            productExpirationDate += "-" + dpExpirationDate.getDayOfMonth();
        }

        String defaultImgURL = "https://www.graphicsprings.com/filestorage/stencils/af1870a7edc55de23aed98b0d18526a9.png?width=500&height=500";
        Product product = new Product(productName, Integer.parseInt(quantity), unitOfMeasure, productExpirationDate, productCategory, productPackage, defaultImgURL);
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        databaseStorageManager.addProduct(userID, product);
        // add product to database

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(product.getExpirationDate());
            Calendar rightNow = Calendar.getInstance();

            date.setHours(rightNow.get(Calendar.HOUR_OF_DAY));
            date.setMinutes(rightNow.get(Calendar.MINUTE));
            date.setSeconds(rightNow.get(Calendar.SECOND));

            long time = date.getTime() + 10*1000;
            //long time = System.currentTimeMillis() + 10 * 1000;

            //Toast.makeText(this, jsonProduct.getName(), Toast.LENGTH_SHORT).show();
            CustomNotification customNotification =
                    new CustomNotification("Produs expirat", product.getName() + " expira azi.", time);
            //Toast.makeText(this, Integer.toString(customNotification.id), Toast.LENGTH_SHORT).show();
            customNotificationManager.sendNotification(customNotification);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}