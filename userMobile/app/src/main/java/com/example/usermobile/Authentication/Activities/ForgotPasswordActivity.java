package com.example.usermobile.Authentication.Activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.usermobile.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etForgotPasswordEmail;

    private ProgressBar progressBar;

    private FirebaseAuth resetPasswordAuthentication;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        etForgotPasswordEmail= findViewById(R.id.etForgotPasswordEmail);

        Button btnForgotPasswordPasswordReset = findViewById(R.id.btnForgotPasswordPasswordReset);

        progressBar= findViewById(R.id.progressBar);

        resetPasswordAuthentication = FirebaseAuth.getInstance();

        btnForgotPasswordPasswordReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnForgotPasswordPasswordReset) {
            resetPassword();
        }
    }

    private void resetPassword() {

        String email = etForgotPasswordEmail.getText().toString().trim();

        if(email.isEmpty()) {
            etForgotPasswordEmail.setError("Email is required!");
            etForgotPasswordEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etForgotPasswordEmail.setError("Please provide valid email!");
            etForgotPasswordEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        resetPasswordAuthentication.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

            if(task.isSuccessful()) {
                Toast.makeText(ForgotPasswordActivity.this,"Check your email to reset password!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(ForgotPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.GONE);
    }
}