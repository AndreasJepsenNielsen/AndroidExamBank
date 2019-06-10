package com.example.bankapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.Interface.MyCallBack;
import com.example.bankapp.R;
import com.example.bankapp.Service.CheckPasswordService;
import com.example.bankapp.Service.ValidEmailAddressService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MAINACTIVITY";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button loginButton;
    Button registerButton;
    EditText emailLogin;
    EditText passwordLogin;
    TextView forgotPassword;
    CustomerModel currentUser;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private DatabaseReference currentUserRef;
    CheckPasswordService passwordService;
    ValidEmailAddressService emailAddressService;
    ProgressDialog loginProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkLoginValidity()) {
                    login(emailLogin.getText().toString(), passwordLogin.getText().toString(), database.getReference(getString(R.string.pathCPHSlashUser) + emailLogin.getText().toString().replace(".", "")));

                    login(emailLogin.getText().toString(), passwordLogin.getText().toString(), database.getReference(getString(R.string.pathOdenseSlashUser) + emailLogin.getText().toString().replace(".", "")));
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                        COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } else {
                    getLocationPermission();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkLoginValidity() {
        if (emailLogin.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.emailNotEmpty), Toast.LENGTH_LONG).show();

            return false;
        }

        if (!emailAddressService.isValidEmailAddress(emailLogin.getText().toString())) {
            Toast.makeText(this, getString(R.string.emailNotValid), Toast.LENGTH_LONG).show();

            return false;
        }

        if (passwordLogin.getText().toString().isEmpty() || passwordLogin.getText().toString().length() < 7) {
            Toast.makeText(this, getString(R.string.mustBe8Characters), Toast.LENGTH_LONG).show();
            return false;
        }

        if (passwordService.checkPassword(passwordLogin.getText().toString())) {
            Toast.makeText(this, getString(R.string.mustContainUppercaseLetterAndNumber), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void readFromDatabaseTest(final MyCallBack myCallBack, DatabaseReference myRef) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                CustomerModel user = dataSnapshot.getValue(CustomerModel.class);
                Log.d(TAG, "Value is: " + currentUser);
                myCallBack.onCallBack(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void login(final String email, final String password, final DatabaseReference userRef) {
        loginProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            currentUserRef = userRef;

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            readFromDatabaseTest(new MyCallBack() {
                                @Override
                                public void onCallBack(CustomerModel value) {

                                    try {
                                        System.out.println(value.getAccounts());
                                        currentUser = value;
                                        currentUser.setAccounts(value.getAccounts());

                                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);

                                        intent.putExtra(getString(R.string.intentUser), currentUser);
                                        intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), currentUser.getAccounts());
                                        startActivity(intent);

                                        loginProgressDialog.dismiss();

                                    } catch (NullPointerException npe) {
                                        return;
                                    }
                                }

                                @Override
                                public void onCallBackLocation(Location value) {
                                }

                                @Override
                                public void onCallBackBalance(Double value) {

                                }
                            }, currentUserRef);

                        } else {
                            // If sign in fails, display a message to the user.
                            loginProgressDialog.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, getString(R.string.authenticationFailed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void init() {
        loginButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.button2);
        emailLogin = findViewById(R.id.editText);
        passwordLogin = findViewById(R.id.editText2);
        forgotPassword = findViewById(R.id.textView3);
        passwordService = new CheckPasswordService();
        emailAddressService = new ValidEmailAddressService();

        loginProgressDialog = new ProgressDialog(MainActivity.this);
        loginProgressDialog.setIndeterminate(true);
        loginProgressDialog.setMessage("Authenticating...");
    }
}
