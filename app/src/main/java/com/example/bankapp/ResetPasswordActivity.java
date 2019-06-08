package com.example.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import org.apache.commons.validator.routines.EmailValidator;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText emailfield;
    Button resetPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();

        mAuth = FirebaseAuth.getInstance();


        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailfield.getText().length() <= 0) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.emailNotEmpty), Toast.LENGTH_LONG).show();
                } else if (!isValidEmailAddress(emailfield.getText().toString())) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.emailNotValid), Toast.LENGTH_LONG).show();
                } else {
                    passResetViaEmail();
                    Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    public static boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        Log.d("HER", "isValidEmailAddress: " + validator.isValid(email));
        return validator.isValid(email);
    }

    private void passResetViaEmail() {

        if (mAuth != null) {
            Log.w(" if Email authenticated", "Recovery Email has been  sent to " + emailfield.getText().toString());
            mAuth.sendPasswordResetEmail(emailfield.getText().toString());
        } else {
            Log.w(" error ", " bad entry ");
        }
    }

    private void init() {
        emailfield = findViewById(R.id.editText8);
        resetPassword = findViewById(R.id.button3);
    }
}
