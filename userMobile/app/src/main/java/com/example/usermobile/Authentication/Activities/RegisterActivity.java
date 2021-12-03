package com.example.usermobile.Authentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.usermobile.Authentication.Util.User;
import com.example.usermobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUserEmail;
    private EditText etUserFullName;
    private EditText etUserPhoneNumber;
    private EditText etUserPassword;
    private EditText etUserConfirmPassword;

    private Button btnContinue;

    private ProgressBar progressBar;

    private FirebaseAuth registerAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**
         * Initializing the connection between the code and design
         */
        etUserEmail            = (EditText) findViewById(R.id.etRegisterEmail);
        etUserPhoneNumber      = (EditText) findViewById(R.id.etRegisterPhoneNumber);
        etUserFullName        = (EditText) findViewById(R.id.etRegisterFullName);
        etUserPassword         = (EditText) findViewById(R.id.etRegisterPassword);
        etUserConfirmPassword  = (EditText) findViewById(R.id.etRegisterConfirmPassword);

        btnContinue        = (Button) findViewById(R.id.btnRegisterContinue);

        progressBar            = (ProgressBar) findViewById(R.id.progressBar);

        /**
         * Checking if the user is already signed into the app
         */
        registerAuthentication = FirebaseAuth.getInstance();

        /**
         * Redirecting the user depending on the button they press
         */
        btnContinue.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnRegisterContinue:
                register();
                //startActivity(new Intent(RegisterActivity.this, AdditionalDataActivity.class));
                break;
        }
    }

    private void register() {
        /**
         * Get user input data and check for null fields
         */
        String email = etUserEmail.getText().toString().trim();
        String fullName = etUserFullName.getText().toString().trim();
        String phoneNumber = etUserPhoneNumber.getText().toString().trim();
        String password = etUserPassword.getText().toString().trim();
        String confirmPassword = etUserConfirmPassword.getText().toString().trim();

        User user = new User(email, fullName, phoneNumber);

        /**
         * Check if the input is in the correct format
         */
        if(assertInputCorrectness(user, password, confirmPassword) == false) {
            return;
        }

        /**
         * Create a new user with the data provided
         */
        createUser(user, password);
    }

    /**
     * Check whether the given input exists and is in the desired format
     * @param user            is the user data that will be stored unencrypted
     * @param password        is the password provided by the user ( it is encrypted )
     * @param confirmPassword is the confirmation of the password provided by the user ( it is encrypted )
     * @return
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
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //getUid
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,
                                                "User has been registered successfully! Please confirm email",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        //startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this,"Failed to register! Try again!",
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterActivity.this,"Failed to register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}