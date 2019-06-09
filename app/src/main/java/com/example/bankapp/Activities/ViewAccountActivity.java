package com.example.bankapp.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.R;
import com.example.bankapp.Service.AutoPayReceiver;
import com.example.bankapp.Service.GetNumberService;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewAccountActivity extends AppCompatActivity {

    CustomerModel userDetails;
    AccountModel account;
    ArrayList<AccountModel> accounts;
    String number;
    GetNumberService numberService;

    Button accountView, transferMoneyBetweenAccounts, transferMoneyToOtherAccounts, depositButton, payBillsBtn, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        userDetails = getIntent().getParcelableExtra(getString(R.string.intentUser));
        account = getIntent().getParcelableExtra(getString(R.string.intentAccount));
        accounts = getIntent().getParcelableArrayListExtra(getString(R.string.intentAccounts));

        init();

        accountView.setText(account.getType() + " " + getString(R.string.AccountInViewActivity) + " " + getString(R.string.balance) + account.getBalance());

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAccountActivity.this, DepositActivity.class);
                intent.putExtra(getString(R.string.intentUser), userDetails);
                intent.putExtra(getString(R.string.intentAccount), account);
                startActivity(intent);
            }
        });

        final Calendar today = Calendar.getInstance();
        today.getTime();
        final Calendar userIs77 = Calendar.getInstance();

        userIs77.set(Calendar.DAY_OF_MONTH, getBirthDate().get(Calendar.DAY_OF_MONTH));
        userIs77.set(Calendar.MONTH, getBirthDate().get(Calendar.MONTH));
        userIs77.set(Calendar.YEAR, getBirthDate().get(Calendar.YEAR) + 77);

        transferMoneyBetweenAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getType().equals(getString(R.string.PENSION)) && !today.after(userIs77)) {
                    Toast.makeText(ViewAccountActivity.this, getString(R.string.notOldEnough), Toast.LENGTH_LONG).show();

                } else {
                    Intent transferBetweenAccounts = new Intent(ViewAccountActivity.this, TransferMoneyAccounts.class);
                    transferBetweenAccounts.putExtra(getString(R.string.intentUser), userDetails);
                    transferBetweenAccounts.putExtra(getString(R.string.intentAccount), account);
                    transferBetweenAccounts.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                    startActivity(transferBetweenAccounts);
                }
            }
        });

        transferMoneyToOtherAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getType().equals(getString(R.string.PENSION)) && !today.after(userIs77)) {
                    Toast.makeText(ViewAccountActivity.this, getString(R.string.notOldEnough), Toast.LENGTH_LONG).show();

                } else {
                    Intent transferToOthers = new Intent(ViewAccountActivity.this, NemIdActivity.class);
                    transferToOthers.putExtra(getString(R.string.intentUser), userDetails);
                    transferToOthers.putExtra(getString(R.string.intentAccount), account);
                    transferToOthers.putExtra(getString(R.string.clicked), getString(R.string.transfer));
                    startActivity(transferToOthers);
                }
            }
        });

        payBillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account.getType().equals(getString(R.string.PENSION)) && !today.after(userIs77)) {
                    Toast.makeText(ViewAccountActivity.this, getString(R.string.notOldEnough), Toast.LENGTH_LONG).show();

                } else {
                    Intent payBills = new Intent(ViewAccountActivity.this, NemIdActivity.class);
                    payBills.putExtra(getString(R.string.intentUser), userDetails);
                    payBills.putExtra(getString(R.string.intentAccount), account);
                    payBills.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                    payBills.putExtra(getString(R.string.clicked), getString(R.string.payBills));

                    startActivity(payBills);

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPayments();
            }
        });
    }

    private Calendar getBirthDate() {
        Calendar birthDate = Calendar.getInstance();

        int day = Integer.parseInt(userDetails.getSSN().substring(0, 2));
        int month = Integer.parseInt(userDetails.getSSN().substring(2, 4));
        int year = Integer.parseInt(userDetails.getSSN().substring(4, 6));

        if (year < 20) {
            if (year < 10) {
                String concatYear = Integer.toString(200) + year;
                year = Integer.parseInt(concatYear);
            } else {
                String concatYear = Integer.toString(20) + year;
                year = Integer.parseInt(concatYear);
            }
        } else if (year > 20) {
            String concatYear = Integer.toString(19) + year;
            year = Integer.parseInt(concatYear);
        }

        birthDate.set(year, month, day);
        return birthDate;
    }

    private void cancelPayments() {

        Intent intent = new Intent(this, AutoPayReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), Integer.parseInt(number), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, pendingIntent);

        alarmManager.cancel(pendingIntent);
    }

    private void init() {
        accountView = findViewById(R.id.AccountBtn2);
        transferMoneyBetweenAccounts = findViewById(R.id.transferMoneyBetweenAccountsBtn);
        transferMoneyToOtherAccounts = findViewById(R.id.transferMoneyToOtherAccountsBtn);
        depositButton = findViewById(R.id.DepositBtn);
        payBillsBtn = findViewById(R.id.payBillsBtn);
        cancelButton = findViewById(R.id.cancelBtn);
        numberService = new GetNumberService();

        number = numberService.getNumber(this, account);
        System.out.println("MYNUMBER" + number);
    }
}
