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

import java.util.ArrayList;

public class DepositActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();
    AccountModel account = getIntent().getParcelableExtra("account");
    CustomerModel user = getIntent().getParcelableExtra("user");
    Button depositButton;
    EditText depositField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        depositButton = findViewById(R.id.depositButton);
        depositField  = findViewById(R.id.depositField);
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( Double.parseDouble(depositField.getText().toString()) > 0.0 && !depositField.getText().toString().isEmpty()){
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
            myref.child(user.getAffiliate()).child(user.getEmail().replace(".","")).child("accounts").child(number).child("balance").setValue(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
