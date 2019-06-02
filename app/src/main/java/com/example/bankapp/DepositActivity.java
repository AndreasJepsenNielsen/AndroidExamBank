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
        account = getIntent().getParcelableExtra(getString(R.string.intentAccounts));
        user = getIntent().getParcelableExtra(getString(R.string.intentUser));
        depositButton = findViewById(R.id.depositButton);
        depositField  = findViewById(R.id.depositField);
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !depositField.getText().toString().isEmpty() && Double.parseDouble(depositField.getText().toString()) > 0.0){
                    depositMoney(Double.parseDouble(depositField.getText().toString()));
                }else{
                    Toast.makeText(DepositActivity.this, getString(R.string.depositPositiveAmount),Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void depositMoney(double amount){
        String number = getString(R.string.zero);
        if(account.getType().equals(getString(R.string.typeBudget))){
            number = getString(R.string.one);
        }

        if(account.getType().equals(getString(R.string.typeBusiness))){
            number = getString(R.string.two);

        }

        if(account.getType().equals(getString(R.string.typePension))){
            number = getString(R.string.three);

        }
        if(account.getType().equals(getString(R.string.typeSavings))){
            number = getString(R.string.four);

        }
        try {

            myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".","") + getString(R.string.pathAccountSlash) + number + getString(R.string.pathBalance)).setValue(amount + account.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
