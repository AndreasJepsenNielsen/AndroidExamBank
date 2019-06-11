package com.example.bankapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bankapp.R;
import com.example.bankapp.Service.ValidEmailAddressService;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText emailfield;
    Button resetPassword;
    private FirebaseAuth mAuth;
    ValidEmailAddressService emailAddressService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();

        mAuth = FirebaseAuth.getInstance();

        /**
         * checks if the email is valid.
         */
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailfield.getText().length() <= 0) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.emailNotEmpty), Toast.LENGTH_LONG).show();
                } else if (!emailAddressService.isValidEmailAddress(emailfield.getText().toString())) {
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

    /**
     * send a email to the email from the input field with the rest password
     */
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
        emailAddressService = new ValidEmailAddressService();
    }
}
