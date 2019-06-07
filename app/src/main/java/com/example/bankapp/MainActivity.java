package com.example.bankapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.validator.routines.EmailValidator;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button registerButton;
    EditText emailLogin;
    EditText passwordLogin;
    TextView forgotPassword;
    CustomerModel currentUser;
    final String TAG = "MAINACTIVITY";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private DatabaseReference currentUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        loginButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.button2);
        emailLogin = findViewById(R.id.editText);
        passwordLogin = findViewById(R.id.editText2);
        forgotPassword = findViewById(R.id.textView3);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLoginValidity()){

                        Login(emailLogin.getText().toString(), passwordLogin.getText().toString(), database.getReference(getString(R.string.pathCPHSlashUser) + emailLogin.getText().toString().replace(".","")));

                        Login(emailLogin.getText().toString(), passwordLogin.getText().toString(), database.getReference(getString(R.string.pathOdenseSlashUser) + emailLogin.getText().toString().replace(".","")));
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                        COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }else{
                    getLocationPermission();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RestPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public static boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        Log.d("HER", "isValidEmailAddress: " + validator.isValid(email));
        return validator.isValid(email);

    }

    private void getLocationPermission(){
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }



    }

    private boolean checkPassword(String str){
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    private boolean checkLoginValidity(){


        if(emailLogin.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.emailNotEmpty), Toast.LENGTH_LONG).show();

            return false;
        }

        if(!isValidEmailAddress(emailLogin.getText().toString())){
            Toast.makeText(this, getString(R.string.emailNotValid),Toast.LENGTH_LONG).show();

            return false;
        }

        if(passwordLogin.getText().toString().isEmpty() || passwordLogin.getText().toString().length() < 7){
            Toast.makeText(this, getString(R.string.mustBe8Characters), Toast.LENGTH_LONG).show();
            return false;
        }

        if(checkPassword(passwordLogin.getText().toString())){
            Toast.makeText(this, getString(R.string.mustContainUppercaseLetterAndNumber), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }



    private void readFromDatabaseTest(final MyCallBack myCallBack, DatabaseReference myRef){
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

    private void Login(final String email, final String password, final DatabaseReference userRef) {

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
                                        System.out.println("HAEJKWJEOKAW" + currentUser.getAccounts());

                                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);

                                        intent.putExtra(getString(R.string.intentUser), currentUser);
                                        intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), currentUser.getAccounts());
                                        startActivity(intent);
                                    }catch (NullPointerException npe){
                                        return ;
                                    }
                                }

                                @Override
                                public void onCallBackLocation(Location value) {
                                }

                                @Override
                                public void onCallBackBalance(Double value) {

                                }
                            },currentUserRef);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, getString(R.string.authenticationFailed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
