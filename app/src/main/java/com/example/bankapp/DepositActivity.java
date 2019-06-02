package com.example.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DepositActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();
    AccountModel account;
    CustomerModel user;
    Button depositButton;
    EditText depositField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        account = getIntent().getParcelableExtra("account");
        user = getIntent().getParcelableExtra("user");
        depositButton = findViewById(R.id.depositButton);
        depositField  = findViewById(R.id.depositField);
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !depositField.getText().toString().isEmpty() && Double.parseDouble(depositField.getText().toString()) > 0.0){
                    depositMoney(Double.parseDouble(depositField.getText().toString()));
                }else{
                    Toast.makeText(DepositActivity.this, "Make Sure you deposit a positive amount",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void depositMoney(double amount){
        String number = "0";
        if(account.getType().equals( "BUDGET")){
            number = "1";
        }

        if(account.getType().equals("BUSINESS")){
            number = "2";

        }

        if(account.getType().equals("PENSION")){
            number = "3";

        }
        if(account.getType().equals("SAVINGS")){
            number = "4";

        }
        try {

            myref.child("/" + user.getAffiliate() + "/users/" + user.getEmail().replace(".","") + "/accounts/" + number + "/balance").setValue(amount + account.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
