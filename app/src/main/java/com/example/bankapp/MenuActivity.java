package com.example.bankapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    Button transferBtn, payBillsBtn, applyAccountBtn, defaultAccountBtn, budgetAccountBtn, businessAccountBtn, pensionAccountBtn;
    TextView defaultAccountBalance;
    // TEST
    CustomerModel andreas = new CustomerModel("121195-3235", "andreas@gmail.com", "andreas123", "Michael berings vang 10", "Andreas", "Nielsen", "31663421");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);





        init();
        displayAccounts();
    }

    private void displayAccounts(){
        CustomerModel userDetails = getIntent().getParcelableExtra("user");
        ArrayList<AccountModel> accounts = getIntent().getParcelableArrayListExtra("accounts");
        System.out.println(accounts);

        for (int i = 0; i < accounts.size() ; i++) {
            if(accounts.get(i).getType().equals("DEFAULT")){
                System.out.println("INDE");
                defaultAccountBtn.setText(getString(R.string.defaultAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
            }

            if(accounts.get(i).getType().equals("BUDGET")) {
                budgetAccountBtn.setText(getString(R.string.budgetAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
            }

            if(accounts.get(i).getType().equals("BUSINESS")){
                businessAccountBtn.setText(getString(R.string.businessAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
            }else{
                businessAccountBtn.setVisibility(View.GONE);
            }

            if(accounts.get(i).getType().equals("PENSION")){
                pensionAccountBtn.setText(getString(R.string.pensionAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
            }else{
                pensionAccountBtn.setVisibility(View.GONE);
            }

            if(accounts.get(i).getType().equals("SAVINGS")){
                //savingsAccountBtn.setText(getString(R.string.savingsAccount) + getString(R.string.balance) + userDetails.getAccounts().get(i).getBalance());
            }else{
               // savingsAccountBtn.setVisibility(View.GONE);
            }
        }


    }

    private void init() {
        transferBtn = findViewById(R.id.transferBtn);
        payBillsBtn = findViewById(R.id.payBillsBtn);
        applyAccountBtn = findViewById(R.id.applyAccountBtn);
        defaultAccountBalance = findViewById(R.id.defaultAccountBalance);
        defaultAccountBtn = findViewById(R.id.defaultAccountBtn);
        budgetAccountBtn = findViewById(R.id.budgetAccountBtn);
        businessAccountBtn = findViewById(R.id.businessAccountBtn);
        pensionAccountBtn = findViewById(R.id.pensionAccountBtn);
        businessAccountBtn.setVisibility(View.GONE);
        //savingsAccountBtn = findViewById()



        // TEST
        //andreas.getDefaultAccount().setBalance(390.00);
        // TEST
        //defaultAccountBtn.setText(getString(R.string.defaultAccount) + getString(R.string.balance) + andreas.getDefaultAccount().getBalance());
    }
}
