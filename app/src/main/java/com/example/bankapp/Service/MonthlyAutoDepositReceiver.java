package com.example.bankapp.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.MyCallBack;
import com.example.bankapp.R;
import com.google.firebase.database.*;

import static android.content.ContentValues.TAG;

public class MonthlyAutoDepositReceiver extends BroadcastReceiver {
    String affiliate, accountNumber, userEmail;
    Double amountDeposit, userBalance, currentBalance;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();

    @Override
    public void onReceive(final Context context, Intent intent) {

        affiliate = intent.getStringExtra(context.getString(R.string.intentAffiliate));
        accountNumber = intent.getStringExtra(context.getString(R.string.intentAutoNumber));
        userEmail = intent.getStringExtra(context.getString(R.string.intentUserEmail));

        amountDeposit = intent.getDoubleExtra(context.getString(R.string.intentAutoAmount), 0.0);


        readFromDatabaseTest(new MyCallBack() {
            @Override
            public void onCallBackBalance(Double value) {

                try {
                    Intent intent = new Intent(context, MonthlyAutoDepositReceiver.class);
                    intent.putExtra(context.getString(R.string.intentAffiliate), affiliate);
                    intent.putExtra(context.getString(R.string.intentAutoNumber), accountNumber);
                    intent.putExtra(context.getString(R.string.intentUserEmail), userEmail);
                    intent.putExtra(context.getString(R.string.intentUserAccountBalance), userBalance);
                    intent.putExtra(context.getString(R.string.intentAutoAmount), amountDeposit);

                    String concatRequestCode = accountNumber + context.getString(R.string.one);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, Integer.parseInt(concatRequestCode), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, pendingIntent);

                    currentBalance = value;
                    myref.child(context.getString(R.string.pathSlash) + affiliate + context.getString(R.string.pathUserSlash) + userEmail.replace(".", "") + context.getString(R.string.pathAccountSlash) + accountNumber + context.getString(R.string.pathBalance)).setValue(currentBalance + amountDeposit);


                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }

            @Override
            public void onCallBack(CustomerModel value) {

            }

            @Override
            public void onCallBackLocation(Location value) {
            }


        }, myref.child(context.getString(R.string.pathSlash) + affiliate + context.getString(R.string.pathUserSlash) + userEmail.replace(".", "") + context.getString(R.string.pathAccountSlash) + accountNumber + context.getString(R.string.pathBalance)));


        Log.d("DailyAlarmReceiver", affiliate + " // test virk pls");
    }

    private void readFromDatabaseTest(final MyCallBack myCallBack, DatabaseReference myRef) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Double currentBalance = dataSnapshot.getValue(Double.class);
                Log.d(TAG, "MonthlyAuto Value is: " + currentBalance);
                myCallBack.onCallBackBalance(currentBalance);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
