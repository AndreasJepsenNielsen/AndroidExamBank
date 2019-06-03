package com.example.bankapp;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.validator.routines.EmailValidator;

public class TransferMoneyOthersActivity extends AppCompatActivity  {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();
    private DatabaseReference currentUserRef;
    CustomerModel receiveUser;

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
        account = getIntent().getParcelableExtra(getString(R.string.intentAccounts));

        init();

        transferMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate(emailOtherAccount, amountToTransfer, account.getBalance());
            }
        });

    }

    private void validate(EditText emailOtherAccount, TextView amountToPay, double accountBalance) {
        if(emailOtherAccount.getText().toString().isEmpty() && amountToPay.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.emptyFieldsTransfer),Toast.LENGTH_LONG).show();
        }
        else if (!isValidEmailAddress(emailOtherAccount.getText().toString())) {
            Toast.makeText(this, getString(R.string.emailNotValid), Toast.LENGTH_LONG).show();
        }
        else if(Double.parseDouble(amountToPay.getText().toString()) > accountBalance) {
            Toast.makeText(this, getString(R.string.notEnoughMoney),Toast.LENGTH_LONG).show();
        }
        else {
            transferMoneyToOthers(Double.parseDouble(amountToPay.getText().toString()));
        }
    }


    private void transferMoneyToOthers(double amount){
        String number = getString(R.string.zero);
        if(account.getType().equals(getString(R.string.BUDGET))){
            number = getString(R.string.one);
        }

        if(account.getType().equals(getString(R.string.BUSINESS))){
            number = getString(R.string.two);

        }

        if(account.getType().equals(getString(R.string.PENSION))){
            number = getString(R.string.three);

        }
        if(account.getType().equals(getString(R.string.SAVINGS))){
            number = getString(R.string.four);

        }
        try {
            System.out.println("KIG HER ELLER" + receiveUser);
            myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".","") + getString(R.string.pathAccountSlash) + number + getString(R.string.pathBalance)).setValue( account.getBalance() - amount);
            depositMoneyReceiver(database.getReference(getString(R.string.pathOdenseSlashUser) + emailOtherAccount.getText().toString().replace(".","")));
            depositMoneyReceiver(database.getReference(getString(R.string.pathCPHSlashUser) + emailOtherAccount.getText().toString().replace(".","")));

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
                    myref.child(getString(R.string.pathSlash) + receiveUser.getAffiliate() + getString(R.string.pathUserSlash) + receiveUser.getEmail().replace(".","") + getString(R.string.pathAccountSlash) + getString(R.string.zero) + getString(R.string.pathBalance)).setValue(receiverAccount.getBalance() + Double.parseDouble(amountToTransfer.getText().toString()));

                }catch (NullPointerException npe){

                    return;
                }

            }




            @Override
            public void onCallBackLocation(Location value) {
            }
        },currentUserRef);
    }

    private void readFromDatabaseTest(final MyCallBack myCallBack, DatabaseReference myRef){
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
                return ;
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

        accountName.setText(account.getType() + " " + getString(R.string.AccountInViewActivity) );
        accountBalance.setText(getString(R.string.accountBalance) + account.getBalance());
    }

}
