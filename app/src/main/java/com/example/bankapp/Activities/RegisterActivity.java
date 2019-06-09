package com.example.bankapp.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.Interface.MyCallBack;
import com.example.bankapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.validator.routines.EmailValidator;

public class RegisterActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final String TAG = "REGISTERACTIVITY";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String affiliate;
    private Location userLocation;
    EditText SSN;
    EditText Email;
    EditText Password;
    EditText Address;
    EditText Firstname;
    EditText Lastname;
    EditText Phonenumber;
    Button RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        getDeviceLocation(new MyCallBack() {
            @Override
            public void onCallBack(CustomerModel value) {
            }

            @Override
            public void onCallBackLocation(Location value) {
                userLocation = value;
                double longitude = userLocation.getLongitude();
                double latitude = userLocation.getLatitude();

                double copenhagen = distance(latitude, longitude, 55.6760968, 12.5683371);
                double odense = distance(latitude, longitude, 55.403756, 10.402370);

                if (copenhagen < odense) {
                    affiliate = getString(R.string.copenhagen);

                } else {
                    affiliate = getString(R.string.odense);
                }
            }

            @Override
            public void onCallBackBalance(Double value) {

            }
        });


        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkValidity();
                try {
                    if (checkValidity()) {
                        CustomerModel tempCustomer = new CustomerModel(SSN.getText().toString(), Email.getText().toString(),
                                Password.getText().toString(), Address.getText().toString(), Firstname.getText().toString(),
                                Lastname.getText().toString(), Phonenumber.getText().toString(), affiliate);
                        writeNewUser(tempCustomer);

                        Intent login = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(login);
                    }
                } catch (NullPointerException Npe) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.allowLocation), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private boolean checkValidity() {
        if (SSN.getText().toString().isEmpty() || SSN.getText().length() < 10 || SSN.getText().length() > 10) {
            Toast.makeText(this, getString(R.string.ssn10Characters), Toast.LENGTH_LONG).show();

            return false;
        }

        if (Email.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.emailNotEmpty), Toast.LENGTH_LONG).show();

            return false;
        }

        if (!isValidEmailAddress(Email.getText().toString())) {
            Toast.makeText(this, getString(R.string.emailNotValid), Toast.LENGTH_LONG).show();

            return false;
        }

        if (Password.getText().toString().isEmpty() || Password.getText().toString().length() < 8) {
            Toast.makeText(this, getString(R.string.mustBe8Characters), Toast.LENGTH_LONG).show();
            return false;

        }

        if (checkPassword(Password.getText().toString())) {
            Toast.makeText(this, getString(R.string.mustContainUppercaseLetterAndNumber), Toast.LENGTH_LONG).show();
            return false;

        }

        if (Address.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.addressNotEmpty), Toast.LENGTH_LONG).show();
            return false;

        }

        if (Firstname.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.firstnameNotEmpty), Toast.LENGTH_LONG).show();

            return false;
        }

        if (Lastname.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.lastnameNotEmpty), Toast.LENGTH_LONG).show();

            return false;
        }

        if (Phonenumber.getText().toString().isEmpty() || Phonenumber.getText().toString().length() < 8 ||
                Phonenumber.getText().toString().length() > 8) {
            Toast.makeText(this, getString(R.string.phonenumberAuth), Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        Log.d("HER", "isValidEmailAddress: " + validator.isValid(email));
        return validator.isValid(email);
    }

    private boolean checkPassword(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if (numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    private void writeNewUser(CustomerModel customerToCreate) {
        CustomerModel testCustomer1 = new CustomerModel(customerToCreate.getSSN(), customerToCreate.getEmail(), customerToCreate.getPassword(), customerToCreate.getAddress(), customerToCreate.getFirstName(), customerToCreate.getLastName(), customerToCreate.getPhoneNumber(), affiliate);
        String[] fn = testCustomer1.getEmail().split("\\.");

        String emailNotDot = fn[0] + fn[1].replace(".", "");
        database.getReference(affiliate + getString(R.string.pathUser)).child(emailNotDot).setValue(testCustomer1);

        createAuthUser(customerToCreate.getEmail(), customerToCreate.getPassword());
    }

    private void createAuthUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, getString(R.string.authenticationFailed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getDeviceLocation(final MyCallBack myCallBack) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d(TAG, "getDeviceLocation: getting current location");
        try {
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();

                        myCallBack.onCallBackLocation(currentLocation);

                        Log.d(TAG, "onComplete: LATITUDE AND LONGITUDE: " + currentLocation.getLatitude() + currentLocation.getLongitude());

                    } else {
                        Log.d(TAG, "onComplete: Current location is null");

                    }
                }
            });

        } catch (SecurityException SE) {
            Log.d(TAG, "getDeviceLocation: SecurityException" + SE.getMessage());
        }
    }

    private void init() {
        RegisterButton = findViewById(R.id.button);
        SSN = findViewById(R.id.editText3);
        Email = findViewById(R.id.editText4);
        Password = findViewById(R.id.editText5);
        Address = findViewById(R.id.editText6);
        Firstname = findViewById(R.id.editText7);
        Lastname = findViewById(R.id.editText2);
        Phonenumber = findViewById(R.id.editText);
    }
}
