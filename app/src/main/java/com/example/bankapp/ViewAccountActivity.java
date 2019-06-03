package com.example.bankapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;

public class ViewAccountActivity extends AppCompatActivity {

    CustomerModel userDetails;
    AccountModel account;

    Button accountView;
    Button transferMoneyBetweenAccounts;
    Button transferMoneyToOtherAccounts;
    Button depositButton, payBillsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        userDetails = getIntent().getParcelableExtra(getString(R.string.intentUser));
        account = getIntent().getParcelableExtra(getString(R.string.intentAccounts));

        accountView = findViewById(R.id.AccountBtn2);
        transferMoneyBetweenAccounts = findViewById(R.id.transferMoneyBetweenAccountsBtn);
        transferMoneyToOtherAccounts = findViewById(R.id.transferMoneyToOtherAccountsBtn);
        depositButton = findViewById(R.id.DepositBtn);
        //payBillsBtn = findViewById(R.id.payBillsBtn);

        accountView.setText(account.getType() + " " + getString(R.string.AccountInViewActivity) + " " + getString(R.string.balance) + account.getBalance());

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAccountActivity.this, DepositActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                intent.putExtra(getString(R.string.intentAccounts), account);
                startActivity(intent);
            }
        });

        transferMoneyToOtherAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transferToOthers = new Intent(ViewAccountActivity.this, NemIdActivity.class);
                transferToOthers.putExtra(getString(R.string.intentUser), userDetails);
                transferToOthers.putExtra(getString(R.string.intentAccounts), account);
                transferToOthers.putExtra(getString(R.string.clicked), getString(R.string.transfer));
                startActivity(transferToOthers);
            }
        });

    }
}
