package com.example.bankapp.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bankapp.MainActivity;
import com.example.bankapp.MenuActivity;
import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.MyCallBack;
import com.example.bankapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class AutoPayReceiver extends BroadcastReceiver{

    String affiliate, accountNumber, userEmail;
    Double amountWithdraw, userBalance, currentBalance;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();

    @Override
    public void onReceive(Context context, Intent intent) {

        affiliate = intent.getStringExtra(context.getString(R.string.intentAffiliate));
        accountNumber = intent.getStringExtra(context.getString(R.string.intentAutoNumber));
        userEmail = intent.getStringExtra(context.getString(R.string.intentUserEmail));
        userBalance = intent.getDoubleExtra(context.getString(R.string.intentUserAccountBalance), 0.0);
        amountWithdraw = intent.getDoubleExtra(context.getString(R.string.intentAutoAmount), 0.0);

        System.out.println(affiliate);
        System.out.println(accountNumber);
        System.out.println(userEmail);
        System.out.println(userBalance);
        System.out.println(amountWithdraw);

        readFromDatabaseTest(new MyCallBack() {
            @Override
            public void onCallBack(CustomerModel value) {

                try {
                    currentUser = value;
                    currentUser.setAccounts(value.getAccounts());
                    System.out.println("HAEJKWJEOKAW" + currentUser.getAccounts());

                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);

                    intent.putExtra(getString(R.string.intentUser), currentUser);
                    intent.putParcelableArrayListExtra(getString(R.string.intentAccounts), currentUser.getAccounts());
                    startActivity(intent);
                }catch (NullPointerException npe){
                    return ;
                }
            }

            @Override
            public void onCallBackLocation(Location value) {
            }
        },myref.child(context.getString(R.string.pathSlash) + affiliate + context.getString(R.string.pathUserSlash) + userEmail.replace(".","") + context.getString(R.string.pathAccountSlash) + accountNumber + context.getString(R.string.pathBalance)));

        myref.child(context.getString(R.string.pathSlash) + affiliate + context.getString(R.string.pathUserSlash) + userEmail.replace(".","") + context.getString(R.string.pathAccountSlash) + accountNumber + context.getString(R.string.pathBalance)).setValue(userBalance - amountWithdraw);

        Log.d("DailyAlarmReceiver", affiliate + " // test virk pls");
    }

    private void readFromDatabaseTest(final MyCallBack myCallBack, DatabaseReference myRef){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                CustomerModel user = dataSnapshot.getValue(CustomerModel.class);
                Log.d(TAG, "Value is: " + currentUser);
                myCallBack.onCallBack(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}
