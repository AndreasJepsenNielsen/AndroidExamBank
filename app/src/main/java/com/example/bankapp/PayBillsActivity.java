package com.example.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PayBillsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText paymentId, paymentName, paymentCreditor, paymentAmount;
    Spinner paySpinner;
    CheckBox paymentAutoCheckbox;
    Button paymentButton;
    TextView accountName,accountBalance;
    AccountModel account;
    CustomerModel user;
    String number;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);

        user = getIntent().getParcelableExtra(getString(R.string.intentUser));
        account = getIntent().getParcelableExtra(getString(R.string.intentAccount));

        init();

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatePayment()){

                    myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".","") + getString(R.string.pathAccountSlash) + number + getString(R.string.pathBalance)).setValue( account.getBalance() - Double.parseDouble(paymentAmount.getText().toString()));

                    finish();
                }
            }
        });


    }


    private boolean validatePayment() {
        if(paymentId.getText().toString().isEmpty() || paymentCreditor.getText().toString().isEmpty() || paymentName.getText().toString().isEmpty() || paymentAmount.toString().isEmpty()){
            Toast.makeText(this, getString(R.string.emptyFieldsPayBills),Toast.LENGTH_LONG).show();
            return false;
        }

        if(paymentCreditor.length() != 8){
            Toast.makeText(this, getString(R.string.creditorLength),Toast.LENGTH_LONG).show();
            return false;
        }

        if(paymentId.length() != 14){
            Toast.makeText(this, getString(R.string.paymentIdLength),Toast.LENGTH_LONG).show();
            return false;

        }

        if(Double.parseDouble(paymentAmount.getText().toString()) > account.getBalance()) {
            Toast.makeText(this, getString(R.string.notEnoughMoney),Toast.LENGTH_LONG).show();

            return false;
        }

        return true;

    }


    private void init() {
        paymentId = findViewById(R.id.payId);
        paymentName = findViewById(R.id.payName);
        paymentCreditor = findViewById(R.id.payCreditor);
        paySpinner = findViewById(R.id.spinnerPay);
        paymentAutoCheckbox = findViewById(R.id.checkBoxPayAuto);
        paymentButton = findViewById(R.id.buttonPay);
        paymentAmount = findViewById(R.id.paymentAmount);


        accountBalance = findViewById(R.id.accountBalanceAccountsPay);
        accountName = findViewById(R.id.accountNameAccountsPay);

        accountName.setText(account.getType() + " " + getString(R.string.AccountInViewActivity) );
        accountBalance.setText(getString(R.string.accountBalance) + account.getBalance());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.paySpinner, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        paySpinner.setAdapter(adapter);

        paySpinner.setOnItemSelectedListener(this);

        getNumber();


    }

    private void getNumber() {
        number = getString(R.string.zero);
        if(account.getType().equals(getString(R.string.BUDGET))){
            number = getString(R.string.one);
        }

        if(account.getType().equals(getString(R.string.BUSINESS))){
            number = getString(R.string.two);

        }

        if(account.getType().equals(getString(R.string.SAVINGS))){
            number = getString(R.string.three);

        }
        if(account.getType().equals(getString(R.string.PENSION))){
            number = getString(R.string.four);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}