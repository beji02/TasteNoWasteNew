package com.example.usermobile.Authentication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.usermobile.MainActivity;
import com.example.usermobile.R;
import com.example.usermobile.Storage.StorageListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail;
    private EditText etPassword;

    private ProgressBar progressBar;

    private FirebaseAuth loginAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView goToRegister       = findViewById(R.id.tvLoginRegister);
        TextView goToForgotPassword = findViewById(R.id.tvLoginForgotPassword);

        etEmail            = findViewById(R.id.etLoginEmail);
        etPassword         = findViewById(R.id.etLoginPassword);
        progressBar        = findViewById(R.id.progressBar);

        Button btnSignIn = findViewById(R.id.btnLoginSignIn);

        loginAuthentication = FirebaseAuth.getInstance();

        FirebaseUser currentUser = loginAuthentication.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()) {
            startActivity(new Intent(this, MainActivity.class));
        }

        goToRegister.setOnClickListener(this);
        goToForgotPassword.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    /**
     * Choosing which part to start
     * @param view the current phone screen
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.tvLoginRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tvLoginForgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.btnLoginSignIn:
                userLogin();
                break;
        }
    }

    /**
     * Handles the user authentication of the user
     */
    private void userLogin() {

        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(!assertInputCorrectness(email, password)) {
            return;
        }

        authenticateUser(email, password);
    }

    /**
     * Check whether the given input exists and is in the desired format
     * @param email     is the email typed by the user
     * @param password  is the password typed by the user
     * @return whether the given input is in the desired format
     */
    private boolean assertInputCorrectness(String email, String password) {

        if (email.isEmpty()) {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide valid email!");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Min password length should be 6 characters!");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Checks the credentials of the user and redirects if they are correct
     * @param email    is the email typed by the user
     * @param password is the password typed by the user
     */
    public void authenticateUser(String email, String password) {

        progressBar.setVisibility(View.VISIBLE);

        loginAuthentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                if(user.isEmailVerified()) {
                    startActivity(new Intent(LoginActivity.this, StorageListView.class));
                }
                else {
                    user.sendEmailVerification();
                    Toast.makeText(LoginActivity.this, "Check your email to verify your account",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials",
                        Toast.LENGTH_SHORT).show();
            }
        });

        progressBar.setVisibility(View.GONE);
    }
}