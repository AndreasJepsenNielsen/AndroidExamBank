package com.example.bankapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

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
        readFromDatabaseTest(userRef1);*/
        startActivity(new Intent(this,RegisterActivity.class));

    }

    private void writeNewUser(CustomerModel customerToCreate){
        CustomerModel testCustomer1 = new CustomerModel(customerToCreate.getSSN(), customerToCreate.getEmail(), customerToCreate.getPassword(), customerToCreate.getAddress(), customerToCreate.getFirstName(), customerToCreate.getLastName(), customerToCreate.getPhoneNumber());

        database.getReference("users").child(customerToCreate.getSSN()).setValue(testCustomer1);

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
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



    private void readFromDatabaseTest(DatabaseReference myRef){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                CustomerModel value = dataSnapshot.getValue(CustomerModel.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
