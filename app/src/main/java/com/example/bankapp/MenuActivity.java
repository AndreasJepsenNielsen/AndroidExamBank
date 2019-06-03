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
    Button transferBtn, applyAccountBtn, defaultAccountBtn, budgetAccountBtn, businessAccountBtn, pensionAccountBtn, savingsAccountBtn;
    TextView defaultAccountBalance;

    CustomerModel userDetails;
    ArrayList<AccountModel> accounts;
    AccountModel clickedAccount;
    // TEST
    //CustomerModel andreas = new CustomerModel("121195-3235", "andreas@gmail.com", "andreas123", "Michael berings vang 10", "Andreas", "Nielsen", "31663421");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

         userDetails = getIntent().getParcelableExtra(getString(R.string.intentUser));
         accounts = getIntent().getParcelableArrayListExtra(getString(R.string.intentAccounts));

        init();
        displayAccounts();

        defaultAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                System.out.println(userDetails.getAffiliate());
                System.out.println(userDetails.getFirstName());
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.DEFAULT));

                intent.putExtra(getString(R.string.intentAccounts), clickedAccount);
                startActivity(intent);
            }
        });

        budgetAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.BUDGET));
                intent.putExtra(getString(R.string.intentAccounts), clickedAccount);
                startActivity(intent);
            }
        });

        businessAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.BUSINESS));
                intent.putExtra(getString(R.string.intentAccounts), clickedAccount);
                startActivity(intent);
            }
        });

        pensionAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.PENSION));
                intent.putExtra(getString(R.string.intentAccounts), clickedAccount);
                startActivity(intent);
            }
        });

        savingsAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.SAVINGS));

                intent.putExtra(getString(R.string.intentAccounts), clickedAccount);
                startActivity(intent);
            }
        });


        applyAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent applyForAccounts = new Intent(MenuActivity.this, ApplyForAccountsActivity.class);
                applyForAccounts.putExtra(getString(R.string.intentUser), userDetails);
                applyForAccounts.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                System.out.println("USERAPPLY: " + userDetails);
                System.out.println("ACCOUNTSAPPLY: " + accounts);

                startActivity(applyForAccounts);
            }
        });

    }

    private AccountModel getAccount(String type){
        AccountModel temp = null;
        for (int i = 0; i < accounts.size(); i++) {
            try {
                if (accounts.get(i).getType().equals(type)) {
                    temp = accounts.get(i);
                }
            }catch(NullPointerException npe){

            }
        }

        System.out.println("TEMP" + temp);
        return temp;
    }

    private void displayAccounts(){

        System.out.println(accounts);


        for (int i = 0; i < accounts.size() ; i++) {
           try {
               if(accounts.get(i).getType().equals(getString(R.string.DEFAULT))){
                   defaultAccountBtn.setText(getString(R.string.defaultAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
               }

               if(accounts.get(i).getType().equals(getString(R.string.BUDGET))) {
                   budgetAccountBtn.setText(getString(R.string.budgetAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
               }

               if(accounts.get(i).getType().equals(getString(R.string.BUSINESS))){
                   businessAccountBtn.setVisibility(View.VISIBLE);
                   businessAccountBtn.setText(getString(R.string.businessAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
               }

               if(accounts.get(i).getType().equals(getString(R.string.SAVINGS))){
                   savingsAccountBtn.setVisibility(View.VISIBLE);
                   savingsAccountBtn.setText(getString(R.string.savingsAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
               }

               if(accounts.get(i).getType().equals(getString(R.string.PENSION))){
                   pensionAccountBtn.setVisibility(View.VISIBLE);
                   pensionAccountBtn.setText(getString(R.string.pensionAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
               }
           }
           catch (NullPointerException npE) {
               System.out.println(npE);
           }


        }
    }


    private void init() {
        applyAccountBtn = findViewById(R.id.applyAccountBtn);
        defaultAccountBalance = findViewById(R.id.defaultAccountBalance);
        defaultAccountBtn = findViewById(R.id.defaultAccountBtn);
        budgetAccountBtn = findViewById(R.id.budgetAccountBtn);
        businessAccountBtn = findViewById(R.id.businessAccountBtn);
        pensionAccountBtn = findViewById(R.id.pensionAccountBtn);
        savingsAccountBtn = findViewById(R.id.savingsAccountBtn);

        businessAccountBtn.setVisibility(View.GONE);
        pensionAccountBtn.setVisibility(View.GONE);
        savingsAccountBtn.setVisibility(View.GONE);




        // TEST
        //andreas.getDefaultAccount().setBalance(390.00);
        // TEST
        //defaultAccountBtn.setText(getString(R.string.defaultAccount) + getString(R.string.balance) + andreas.getDefaultAccount().getBalance());
    }
}
