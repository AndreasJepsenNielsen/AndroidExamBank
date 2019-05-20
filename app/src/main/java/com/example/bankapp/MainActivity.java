package com.example.bankapp;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.validator.routines.EmailValidator;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button registerButton;
    EditText emailLogin;
    EditText passwordLogin;
    CustomerModel currentUser;
    final String TAG = "MAINACTIVITY";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*DatabaseReference userRef = database.getReference("/users/131188-2485");
        DatabaseReference userRef1 = database.getReference("/users/121195-3235");

        CustomerModel kasper = new CustomerModel("131188-2485","lovinstone@gmail.com","lovin123","Silkeborggade 41", "Kasper", "Lovin", "30563053");
        CustomerModel andreas = new CustomerModel("121195-3235", "andreas@gmail.com", "andreas123", "Michael berings vang 10", "Andreas", "Nielsen", "31663421");
        writeNewUser(kasper);
        readFromDatabaseTest(userRef);
        writeNewUser(andreas);
        readFromDatabaseTest(userRef1);
*/
        loginButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.button2);
        emailLogin = findViewById(R.id.editText);
        passwordLogin = findViewById(R.id.editText2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLoginValidity()){

                        Login(emailLogin.getText().toString(), passwordLogin.getText().toString());



                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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

    private boolean checkLoginValidity(){


        if(emailLogin.getText().toString().isEmpty()){
            Toast.makeText(this, "Email must not be empty", Toast.LENGTH_LONG).show();

            return false;
        }

        if(!isValidEmailAddress(emailLogin.getText().toString())){
            Toast.makeText(this, "Email is not valid",Toast.LENGTH_LONG).show();

            return false;
        }

        if(passwordLogin.getText().toString().isEmpty() || passwordLogin.getText().toString().length() < 7){
            Toast.makeText(this, "Must be atleast 8 characters", Toast.LENGTH_LONG).show();
            return false;

        }

        if(checkPassword(passwordLogin.getText().toString())){
            Toast.makeText(this, "Must contain atleast one uppercase letter and a number", Toast.LENGTH_LONG).show();
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

    private void Login(final String email, final String password ) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference currentUserRef = database.getReference("/users/" + email.replace(".",""));

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            readFromDatabaseTest(new MyCallBack() {
                                @Override
                                public void onCallBack(CustomerModel value) {
                                    System.out.println(value.getAccounts());
                                    currentUser = value;
                                    currentUser.setAccounts(value.getAccounts());
                                    System.out.println("HAEJKWJEOKAW" + currentUser.getAccounts());

                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);


                                    intent.putExtra("user", currentUser);
                                    intent.putParcelableArrayListExtra("accounts", currentUser.getAccounts());
                                    startActivity(intent);
                                }

                                @Override
                                public void onCallBackLocation(Location value) {

                                }
                            },currentUserRef);

                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("EMAIL:" + email + " PASS:" + password);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

}
