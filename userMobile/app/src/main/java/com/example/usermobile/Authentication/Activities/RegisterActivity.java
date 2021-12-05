package com.example.usermobile.Authentication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.usermobile.Authentication.Util.User;
import com.example.usermobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUserEmail;
    private EditText etUserFullName;
    private EditText etUserPhoneNumber;
    private EditText etUserPassword;
    private EditText etUserConfirmPassword;

    private ProgressBar progressBar;

    private FirebaseAuth registerAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_register);



        etUserEmail            = findViewById(R.id.etRegisterEmail);
        etUserPhoneNumber      = findViewById(R.id.etRegisterPhoneNumber);
        etUserFullName         = findViewById(R.id.etRegisterFullName);
        etUserPassword         = findViewById(R.id.etRegisterPassword);
        etUserConfirmPassword  = findViewById(R.id.etRegisterConfirmPassword);

        Button btnContinue = findViewById(R.id.btnRegisterContinue);

        progressBar            = findViewById(R.id.progressBar);

        registerAuthentication = FirebaseAuth.getInstance();

        btnContinue.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegisterContinue) {
            register();
        }
    }

    private void register() {
        String email           = etUserEmail.getText().toString().trim();
        String fullName        = etUserFullName.getText().toString().trim();
        String phoneNumber     = etUserPhoneNumber.getText().toString().trim();
        String password        = etUserPassword.getText().toString().trim();
        String confirmPassword = etUserConfirmPassword.getText().toString().trim();

        User user = new User(email, fullName, phoneNumber);

        if(!assertInputCorrectness(user, password, confirmPassword)) {
            return;
        }

        createUser(user, password);
    }

    /**
     * Check whether the given input exists and is in the desired format
     * @param user            is the user data that will be stored unencrypted
     * @param password        is the password provided by the user ( it is encrypted )
     * @param confirmPassword is the confirmation of the password provided by the user ( it is encrypted )
     * @return whether the input is correct
     */
    private boolean assertInputCorrectness(User user, String password, String confirmPassword) {
        if(user.getEmail().isEmpty()) {
            etUserEmail.setError("Email is required!");
            etUserEmail.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
            etUserEmail.setError("Please provide valid email!");
            etUserEmail.requestFocus();
            return false;
        }

        if (user.getName().isEmpty()) {
            etUserFullName.setError("Full name is required!");
            etUserFullName.requestFocus();
            return false;
        }

        if(!Patterns.PHONE.matcher(user.getPhoneNumber()).matches()) {
            etUserPhoneNumber.setError("Please input a valid phone number");
            etUserPhoneNumber.requestFocus();
            return false;
        }

        if(password.isEmpty()) {
            etUserPassword.setError("Password is required!");
            etUserPassword.requestFocus();
            return false;
        }

        if(password.length() < 6) {
            etUserPassword.setError("Minimum password length should be 6 characters!");
            etUserPassword.requestFocus();
            return false;
        }

        if(confirmPassword.isEmpty()) {
            etUserConfirmPassword.setError("Confirming the password is required");
            etUserConfirmPassword.requestFocus();
            return false;
        }

        if(!confirmPassword.equals(password)) {
            etUserConfirmPassword.setError("Passwords do not match");
            etUserConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Creates a new user in the both the realtime database and authentication one
     * @param user     the data provided by the user that will be stored unencrypted in the realtime database
     * @param password the password of the user ( it will be encrypted )
     */
    private void createUser(User user, String password) {
        registerAuthentication.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {

                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()) //getUid
                                .setValue(user).addOnCompleteListener(task1 -> {

                                    if(task1.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,
                                                "User has been registered successfully! Please confirm email",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(RegisterActivity.this, AdditionalDataActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this,"Failed to register! Try again!",
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"Failed to register", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}