package com.example.usermobile.Authentication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usermobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView goToRegister;
    private TextView goToForgotPassword;

    private EditText etEmail;
    private EditText etPassword;

    private Button btnSignIn;

    private ProgressBar progressBar;

    private FirebaseAuth loginAuthentication;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        goToRegister       = (TextView) findViewById(R.id.tvLoginRegister);
        goToForgotPassword = (TextView) findViewById(R.id.tvLoginForgotPassword);
        btnSignIn          = (Button) findViewById(R.id.btnLoginSignIn);
        etEmail            = (EditText) findViewById(R.id.etLoginEmail);
        etPassword         = (EditText) findViewById(R.id.etLoginPassword);
        progressBar        = (ProgressBar) findViewById(R.id.progressBar);

        /**
         * Checking if the user is already signed into the app
         */
        loginAuthentication = FirebaseAuth.getInstance();

        if(loginAuthentication != null) {
            FirebaseUser currentUser = loginAuthentication.getCurrentUser();
            if(currentUser != null) {
                //startActivity(new Intent(this, MainActivity.class));
            }
        }

        /**
         * Redirecting the user depending on the button they press
         */
        goToRegister      .setOnClickListener(this);
        goToForgotPassword.setOnClickListener(this);
        btnSignIn         .setOnClickListener(this);
    }

    /**
     * Choosing which part to start
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.tvLoginRegister:
                // open new window
                break;
            case R.id.tvLoginForgotPassword:
                // open new window
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

        /**
         * Get user input data and check for null fields
         */
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        /**
         * Check if the input is in the correct format
         */
        if(assertInputCorrectness(email, password) == false) {
            return;
        }

        /**
         * Check the user credentials and redirect to the main page
         */
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

        loginAuthentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        //redirect to user profile
                        //startActivity(new Intent(loginScreen.this, MainActivity.class));
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
            }
        });
    }
}