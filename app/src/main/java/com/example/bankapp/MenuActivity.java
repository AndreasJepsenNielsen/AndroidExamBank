package com.example.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    Button applyAccountBtn, defaultAccountBtn, budgetAccountBtn, businessAccountBtn, pensionAccountBtn, savingsAccountBtn, monthlyPayBtn;
    ImageButton logOutBtn;
    TextView defaultAccountBalance;

    CustomerModel userDetails;
    ArrayList<AccountModel> accounts;
    AccountModel clickedAccount;

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
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.DEFAULT));
                intent.putExtra(getString(R.string.intentAccount), clickedAccount);
                intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                startActivity(intent);
            }
        });

        budgetAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.BUDGET));
                intent.putExtra(getString(R.string.intentAccount), clickedAccount);
                intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                startActivity(intent);
            }
        });

        businessAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.BUSINESS));
                intent.putExtra(getString(R.string.intentAccount), clickedAccount);
                intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                startActivity(intent);
            }
        });

        pensionAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.PENSION));
                intent.putExtra(getString(R.string.intentAccount), clickedAccount);
                intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                startActivity(intent);
            }
        });

        savingsAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ViewAccountActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                clickedAccount = getAccount(getString(R.string.SAVINGS));
                intent.putExtra(getString(R.string.intentAccount), clickedAccount);
                intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                startActivity(intent);
            }
        });

        applyAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent applyForAccounts = new Intent(MenuActivity.this, ApplyForAccountsActivity.class);
                applyForAccounts.putExtra(getString(R.string.intentUser), userDetails);
                applyForAccounts.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                startActivity(applyForAccounts);
                finish();
            }
        });

        monthlyPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monthlyPay = new Intent(MenuActivity.this, MonthlyPaymentsActivity.class);
                monthlyPay.putExtra(getString(R.string.intentUser), userDetails);
                monthlyPay.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                startActivity(monthlyPay);
                finish();
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOut = new Intent(getApplicationContext(), MainActivity.class);
                logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logOut);
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    private AccountModel getAccount(String type) {
        AccountModel temp = null;
        for (int i = 0; i < accounts.size(); i++) {
            try {
                if (accounts.get(i).getType().equals(type)) {
                    temp = accounts.get(i);
                }
            } catch (NullPointerException npe) {

            }
        }
        return temp;
    }

    private void displayAccounts() {

        for (int i = 0; i < accounts.size(); i++) {
            try {
                if (accounts.get(i).getType().equals(getString(R.string.DEFAULT))) {
                    defaultAccountBtn.setText(getString(R.string.defaultAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
                }

                if (accounts.get(i).getType().equals(getString(R.string.BUDGET))) {
                    budgetAccountBtn.setText(getString(R.string.budgetAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
                }

                if (accounts.get(i).getType().equals(getString(R.string.BUSINESS))) {
                    businessAccountBtn.setVisibility(View.VISIBLE);
                    businessAccountBtn.setText(getString(R.string.businessAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
                }

                if (accounts.get(i).getType().equals(getString(R.string.SAVINGS))) {
                    savingsAccountBtn.setVisibility(View.VISIBLE);
                    savingsAccountBtn.setText(getString(R.string.savingsAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
                }

                if (accounts.get(i).getType().equals(getString(R.string.PENSION))) {
                    pensionAccountBtn.setVisibility(View.VISIBLE);
                    pensionAccountBtn.setText(getString(R.string.pensionAccount) + getString(R.string.balance) + accounts.get(i).getBalance());
                }
            } catch (NullPointerException npE) {
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
        logOutBtn = findViewById(R.id.logOutBtn);
        monthlyPayBtn = findViewById(R.id.montlyPayBtn);

        businessAccountBtn.setVisibility(View.GONE);
        pensionAccountBtn.setVisibility(View.GONE);
        savingsAccountBtn.setVisibility(View.GONE);
    }
}
