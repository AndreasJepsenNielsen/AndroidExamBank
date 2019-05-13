package com.example.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.bankapp.Model.CustomerModel;

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
        // TEST
        andreas.getDefaultAccount().setBalance(390.00);
        // TEST
        defaultAccountBtn.setText(getString(R.string.defaultAccount) + getString(R.string.balance) + andreas.getDefaultAccount().getBalance());
    }
}
