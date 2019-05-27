package com.example.bankapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;

import java.util.ArrayList;

public class ViewAccountActivity extends AppCompatActivity {

    CustomerModel userDetails;
    AccountModel account;

    Button accountView;
    Button transferMoneyBetweenAccounts;
    Button transferMoneyToOtherAccounts;
    Button depositButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        userDetails = getIntent().getParcelableExtra("user");
        account = getIntent().getParcelableExtra("account");

        accountView = findViewById(R.id.AccountBtn2);
        transferMoneyBetweenAccounts = findViewById(R.id.transferMoneyBetweenAccountsBtn);
        transferMoneyToOtherAccounts = findViewById(R.id.transferMoneyToOtherAccountsBtn);
        depositButton = findViewById(R.id.DepositBtn);

        accountView.setText(account.getType() + " " + getString(R.string.AccountInViewActivity) + " " + getString(R.string.balance) + account.getBalance());

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAccountActivity.this, DepositActivity.class);
                System.out.println(userDetails.getAffiliate());
                intent.putExtra("user", userDetails);
                intent.putExtra("account", account);
                startActivity(intent);
            }
        });

    }
}
