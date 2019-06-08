package com.example.bankapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.Service.GetNumberService;
import com.google.firebase.database.*;
import org.apache.commons.validator.routines.EmailValidator;

public class TransferMoneyOthersActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();
    private DatabaseReference currentUserRef;
    CustomerModel receiveUser;
    GetNumberService numberService;

    String number;

    CustomerModel user;
    AccountModel account, receiverAccount;

    Button transferMoneyBtn;
    TextView accountName, accountBalance;
    EditText amountToTransfer, emailOtherAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money_others);

        user = getIntent().getParcelableExtra(getString(R.string.intentUser));
        account = getIntent().getParcelableExtra(getString(R.string.intentAccount));

        init();

        transferMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(emailOtherAccount, amountToTransfer, account.getBalance());
            }
        });
    }

    private void validate(EditText emailOtherAccount, TextView amountToPay, double accountBalance) {
        if (emailOtherAccount.getText().toString().isEmpty() && amountToPay.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.emptyFieldsTransfer), Toast.LENGTH_LONG).show();
        } else if (!isValidEmailAddress(emailOtherAccount.getText().toString())) {
            Toast.makeText(this, getString(R.string.emailNotValid), Toast.LENGTH_LONG).show();
        } else if (Double.parseDouble(amountToPay.getText().toString()) > accountBalance) {
            Toast.makeText(this, getString(R.string.notEnoughMoney), Toast.LENGTH_LONG).show();
        } else {
            transferMoneyToOthers(Double.parseDouble(amountToPay.getText().toString()));
        }
    }

    private void transferMoneyToOthers(double amount) {
        try {
            depositMoneyReceiver(database.getReference(getString(R.string.pathOdenseSlashUser) + emailOtherAccount.getText().toString().replace(".", "")));
            depositMoneyReceiver(database.getReference(getString(R.string.pathCPHSlashUser) + emailOtherAccount.getText().toString().replace(".", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void depositMoneyReceiver(DatabaseReference ref) {
        currentUserRef = ref;

        readFromDatabaseTest(new MyCallBack() {

            @Override
            public void onCallBack(CustomerModel value) {

                try {
                    receiveUser = value;
                    receiverAccount = value.getAccounts().get(0);
                    myref.child(getString(R.string.pathSlash) + receiveUser.getAffiliate() + getString(R.string.pathUserSlash) + receiveUser.getEmail().replace(".", "") + getString(R.string.pathAccountSlash) + getString(R.string.zero) + getString(R.string.pathBalance)).setValue(receiverAccount.getBalance() + Double.parseDouble(amountToTransfer.getText().toString()));
                    myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".", "") + getString(R.string.pathAccountSlash) + number + getString(R.string.pathBalance)).setValue(account.getBalance() - Double.parseDouble(amountToTransfer.getText().toString()));

                    finish();
                } catch (NullPointerException npe) {
                    Toast.makeText(TransferMoneyOthersActivity.this, getString(R.string.invalidUser), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCallBackLocation(Location value) {
            }

            @Override
            public void onCallBackBalance(Double value) {

            }
        }, currentUserRef);
    }

    private void readFromDatabaseTest(final MyCallBack myCallBack, DatabaseReference myRef) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                CustomerModel user = dataSnapshot.getValue(CustomerModel.class);
                myCallBack.onCallBack(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                return;
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        Log.d("HER", "isValidEmailAddress: " + validator.isValid(email));
        return validator.isValid(email);
    }

    private void init() {
        transferMoneyBtn = findViewById(R.id.transferMoneyBtn);
        accountName = findViewById(R.id.accountName);
        emailOtherAccount = findViewById(R.id.emailOtherAccount);
        amountToTransfer = findViewById(R.id.amountToTransfer);
        accountBalance = findViewById(R.id.accountBalance);
        numberService = new GetNumberService();
        number = numberService.getNumber(this, account);

        accountName.setText(account.getType() + " " + getString(R.string.AccountInViewActivity));
        accountBalance.setText(getString(R.string.accountBalance) + account.getBalance());
    }
}
