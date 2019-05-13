package com.example.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankapp.Model.CustomerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final String TAG = "REGISTERACTIVITY";
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RegisterButton = (Button) findViewById(R.id.button);
        SSN = findViewById(R.id.editText3);
        Email = findViewById(R.id.editText4);
        Password = findViewById(R.id.editText5);
        Address = findViewById(R.id.editText6);
        Firstname = findViewById(R.id.editText7);
        Lastname = findViewById(R.id.editText2);
        Phonenumber = findViewById(R.id.editText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckValidity();

                if(CheckValidity()){
                    CustomerModel tempCustomer = new CustomerModel(SSN.getText().toString(),Email.getText().toString(),
                            Password.getText().toString(), Address.getText().toString(), Firstname.getText().toString(),
                            Lastname.getText().toString(), Phonenumber.getText().toString());
                    writeNewUser(tempCustomer);

                    Intent login = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(login);
                }
            }
        });


    }

    private boolean CheckValidity(){
        if(SSN.getText().toString().isEmpty() || SSN.getText().length() < 10 || SSN.getText().length() > 10){
            Toast.makeText(this, "SSN must be 10 characters long", Toast.LENGTH_LONG).show();

            return false;
        }

        if(Email.getText().toString().isEmpty()){
            Toast.makeText(this, "Email must not be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if(!isValidEmailAddress(Email.getText().toString())){
            Toast.makeText(this, "Email is not valid",Toast.LENGTH_LONG).show();

            return false;
        }

        if(Password.getText().toString().isEmpty() || Password.getText().toString().length() < 8){
            Toast.makeText(this, "Must be atleast 8 characters", Toast.LENGTH_LONG).show();
            return false;

        }

        if(checkPassword(Password.getText().toString())){
            Toast.makeText(this, "Must contain atleast one uppercase letter and a number", Toast.LENGTH_LONG).show();
            return false;

        }

        if(Address.getText().toString().isEmpty()){
            Toast.makeText(this,"Address must not be empty", Toast.LENGTH_LONG).show();
            return false;

        }

        if(Firstname.getText().toString().isEmpty()){
            Toast.makeText(this,"Firstname must not be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if(Lastname.getText().toString().isEmpty()){
            Toast.makeText(this,"Lastname must not be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if(Phonenumber.getText().toString().isEmpty() || Phonenumber.getText().toString().length()< 8 ||
                Phonenumber.getText().toString().length() > 8){
            Toast.makeText(this, "Phonenumber must not be empty or greater or less than 8 digits", Toast.LENGTH_LONG).show();

            return false;
        }

        return true;

    }

    public static boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        Log.d("HER", "isValidEmailAddress: " + validator.isValid(email));
        return validator.isValid(email);

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



    private void writeNewUser(CustomerModel customerToCreate){
        CustomerModel testCustomer1 = new CustomerModel(customerToCreate.getSSN(), customerToCreate.getEmail(), customerToCreate.getPassword(), customerToCreate.getAddress(), customerToCreate.getFirstName(), customerToCreate.getLastName(), customerToCreate.getPhoneNumber());
        String[] fn = testCustomer1.getEmail().split("\\.");

        String emailNotDot = fn[0] + fn[1].replace(".","");
        System.out.println("KIG HER ANDREAS" + emailNotDot);
        database.getReference("users").child(emailNotDot).setValue(testCustomer1);

        createAuthUser(customerToCreate.getEmail(), customerToCreate.getPassword());

    }

    private void createAuthUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                        }else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}
