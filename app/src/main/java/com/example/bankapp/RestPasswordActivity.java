package com.example.bankapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class RestPasswordActivity extends AppCompatActivity {

    EditText emailfield;
    Button resetPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);



        emailfield = findViewById(R.id.editText8);
        resetPassword = findViewById(R.id.button3);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                }
            }
        };

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassResetViaEmail();
                Intent intent = new Intent(RestPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void PassResetViaEmail(){

        if(mAuth != null) {
            Log.w(" if Email authenticated", "Recovery Email has been  sent to " +  emailfield.getText().toString());
            mAuth.sendPasswordResetEmail( emailfield.getText().toString());
        } else {
            Log.w(" error ", " bad entry ");
        }
    }
}
