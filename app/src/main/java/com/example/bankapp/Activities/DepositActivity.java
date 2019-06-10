package com.example.bankapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.R;
import com.example.bankapp.Service.GetNumberService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DepositActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();
    AccountModel account;
    CustomerModel user;
    Button depositButton;
    EditText depositField;
    TextView accountDeposit;
    String number;
    GetNumberService numberService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        account = getIntent().getParcelableExtra(getString(R.string.intentAccount));
        user = getIntent().getParcelableExtra(getString(R.string.intentUser));

        init();

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!depositField.getText().toString().isEmpty() && Double.parseDouble(depositField.getText().toString()) > 0.0) {
                    depositMoney(Double.parseDouble(depositField.getText().toString()));
                } else {
                    Toast.makeText(DepositActivity.this, getString(R.string.depositPositiveAmount), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void depositMoney(double amount) {
        try {

            myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".", "") + getString(R.string.pathAccountSlash) + number + getString(R.string.pathBalance)).setValue(amount + account.getBalance());
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        depositButton = findViewById(R.id.depositButton);
        depositField = findViewById(R.id.depositField);
        numberService = new GetNumberService();
        accountDeposit = findViewById(R.id.accountBalanceAccounts);
        accountDeposit.setText(account.getType() + " " + getString(R.string.AccountInViewActivity));
        number = numberService.getNumber(this, account);
    }
}
