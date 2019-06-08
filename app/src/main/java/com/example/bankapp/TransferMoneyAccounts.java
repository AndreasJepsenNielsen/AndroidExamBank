package com.example.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class TransferMoneyAccounts extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();

    CustomerModel user;
    AccountModel account;
    AccountModel selectedAccount;
    ArrayList<AccountModel> accounts;
    ArrayAdapter<AccountModel> adapter;

    Button transferMoneyAccountsBtn;
    TextView accountNameAccounts, accountBalanceAccounts;
    Spinner spinnerTransferAccounts;
    EditText amountToTransferAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money_accounts);

        user = getIntent().getParcelableExtra(getString(R.string.intentUser));
        account = getIntent().getParcelableExtra(getString(R.string.intentAccount));
        accounts = getIntent().getParcelableArrayListExtra(getString(R.string.intentAccounts));

        init();
        transferMoneyAccountsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(amountToTransferAccounts, account.getBalance());
            }
        });
    }

    private void transferMoneyBetweenAccounts(double amount){

        try {
            myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".","") + getString(R.string.pathAccountSlash) + getAccountDatabaseNumber(account) + getString(R.string.pathBalance)).setValue(account.getBalance() - amount);
            myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".","") + getString(R.string.pathAccountSlash) + getAccountDatabaseNumber(selectedAccount) + getString(R.string.pathBalance)).setValue(selectedAccount.getBalance() + amount);

            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validate(EditText amountToPay, double accountBalance) {
        if(amountToPay.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.emptyFieldsTransfer),Toast.LENGTH_LONG).show();
        }

        else if(Double.parseDouble(amountToPay.getText().toString()) > accountBalance) {
            Toast.makeText(this, getString(R.string.notEnoughMoney),Toast.LENGTH_LONG).show();
        }
        else {
            transferMoneyBetweenAccounts(Double.parseDouble(amountToTransferAccounts.getText().toString()));
            finish();
        }
    }

    private String getAccountDatabaseNumber(AccountModel acc){
        String number = getString(R.string.zero);
        if(acc.getType().equals(getString(R.string.BUDGET))){
            number = getString(R.string.one);
        }

        if(acc.getType().equals(getString(R.string.BUSINESS))){
            number = getString(R.string.two);

        }

        if(acc.getType().equals(getString(R.string.SAVINGS))){
            number = getString(R.string.three);

        }
        if(acc.getType().equals(getString(R.string.PENSION))){
            number = getString(R.string.four);

        }
        return number;
    }

    private void init() {
        transferMoneyAccountsBtn = findViewById(R.id.transferMoneyAccountsBtn);
        accountNameAccounts = findViewById(R.id.accountNameAccounts);
        accountBalanceAccounts = findViewById(R.id.accountBalanceAccounts);
        spinnerTransferAccounts = findViewById(R.id.spinnerTransferAccounts);
        amountToTransferAccounts = findViewById(R.id.amountTransferAccounts);

        accountNameAccounts.setText(account.getType() + " " + getString(R.string.AccountInViewActivity) );
        accountBalanceAccounts.setText(getString(R.string.accountBalance) + account.getBalance());
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);


        for (int i = 0; i < accounts.size() ; i++) {
            try {
                if (accounts.get(i).getType() != null && !accounts.get(i).getType().equals(account.getType())){
                    adapter.add(accounts.get(i));
                }

            }
            catch (NullPointerException npE){

            }

        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransferAccounts.setAdapter(adapter);
        spinnerTransferAccounts.setOnItemSelectedListener(this);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedAccount = (AccountModel) spinnerTransferAccounts.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
