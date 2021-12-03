package com.example.usermobile.Authentication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.usermobile.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUserEmail;
    private EditText etUserFullName;
    private EditText etUserPassword;
    private EditText etUserConfirmPassword;

    private Button btnRegisterUser;

    private ProgressBar progressBar;

    private FirebaseAuth registerAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**
         * Initializing the connection between the code and design
         */
        etUserEmail            = (EditText) findViewById(R.id.etRegisterFullName);
        etUserFullName        = (EditText) findViewById(R.id.etRegisterFullName);
        etUserPassword         = (EditText) findViewById(R.id.etRegisterPassword);
        etUserConfirmPassword  = (EditText) findViewById(R.id.etRegisterConfirmPassword);

        btnRegisterUser        = (Button) findViewById(R.id.btnRegisterContinue);

        progressBar            = (ProgressBar) findViewById(R.id.progressBar);

        /**
         * Checking if the user is already signed into the app
         */
        registerAuthentication = FirebaseAuth.getInstance();

        /**
         * Redirecting the user depending on the button they press
         */
        btnRegisterUser.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnRegisterContinue:
                break;
        }
    }
}