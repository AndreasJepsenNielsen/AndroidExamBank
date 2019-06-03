package com.example.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TransferMoneyAccounts extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();

    CustomerModel user;
    ArrayList<AccountModel> accounts;

    Button transferMoneyAccountsBtn;
    TextView accountNameAccounts, accountBalanceAccounts;
    Spinner spinnerTransferAccounts;
    EditText amountToTransferAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money_accounts);

        user = getIntent().getParcelableExtra(getString(R.string.intentUser));
        accounts = getIntent().getParcelableArrayListExtra(getString(R.string.intentAccounts));

        init();
    }

    private void init() {
        transferMoneyAccountsBtn = findViewById(R.id.transferMoneyAccountsBtn);
        accountNameAccounts = findViewById(R.id.accountNameAccounts);
        accountBalanceAccounts = findViewById(R.id.accountBalanceAccounts);
        spinnerTransferAccounts = findViewById(R.id.spinnerTransferAccounts);
        amountToTransferAccounts = findViewById(R.id.amountTransferAccounts);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
