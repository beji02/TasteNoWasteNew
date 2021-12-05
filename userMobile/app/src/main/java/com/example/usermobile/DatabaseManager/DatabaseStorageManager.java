package com.example.usermobile.DatabaseManager;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.usermobile.R;
import com.example.usermobile.Storage.Product;
import com.example.usermobile.Storage.StorageListAdapter;
import com.example.usermobile.Storage.StorageListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DatabaseStorageManager {
    Context context;


    public DatabaseStorageManager(Context context) {
        this.context = context;
    }

    public void addProduct(String userId, Product newProduct) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean found = false;
                    for (DataSnapshot product : snapshot.getChildren()) {
                        Product currProduct = product.getValue(Product.class);
                        currProduct.setIdCode(product.getKey());
                        if(currProduct.equals(newProduct)) {
                            found = true;
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Storage").child(product.getKey()).child("quantity").setValue(currProduct.getQuantity() + newProduct.getQuantity());
                            break;
                        }
                    }

                    if(found == false) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Storage").child(userId + newProduct.getIdCode()).setValue(newProduct).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context,
                                        "Product added successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Failed to add Product!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Storage").addListenerForSingleValueEvent(valueEventListener);
    }

    public void modifyProductQuantity(String userId, Product product) {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId).child("Storage").child(product.getIdCode()).child("quantity").setValue(product.getQuantity()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                Toast.makeText(context,
//                        "All data is complete. Thank you for using our app!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Failed to update your product quantity. Please try again!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

//    public void deleteProduct(String userId, Product newProduct){
//        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Storage").child(userId + newProduct.getIdCode()).setValue(newProduct).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(context,
//                        "Product modifies successfully!", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(context, "Failed to modify Product!",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    Product product2 = new Product("Pasta", 10, "2021-08-12", "Food", "Plastic", "https://world.openfoodfacts.org/images/products/594/904/020/2447/front_fr.3.400.jpg%22");
}
