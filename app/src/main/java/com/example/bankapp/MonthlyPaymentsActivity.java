package com.example.bankapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.Service.AutoPayReceiver;
import com.example.bankapp.Service.MonthlyAutoDepositReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class MonthlyPaymentsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();

    ArrayList<AccountModel> accounts;
    ArrayList<String> items;
    ArrayAdapter<AccountModel> adapter;
    CustomerModel user;
    AccountModel selectedAccount;

    Spinner spinnerAccount;
    EditText amountMonthly;
    Button depositMonthlyBtn;

    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_payments);

        accounts = getIntent().getParcelableArrayListExtra(getString(R.string.intentAccounts));
        user = getIntent().getParcelableExtra(getString(R.string.intentUser));

        init();

        depositMonthlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateMonthlyPayment()) {
                    System.out.println("godkendt");
                    autoPayEveryMonthAlarm(MonthlyPaymentsActivity.this);
                    myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".","") + getString(R.string.pathAccountSlash) + number + getString(R.string.pathBalance)).setValue( selectedAccount.getBalance() + Double.parseDouble(amountMonthly.getText().toString()));
                }else{
                    System.out.println("øv validationen gik ikke");
                }

            }
        });
    }


    private void init() {
        spinnerAccount = findViewById(R.id.spinnerMonthlyAccount);
        amountMonthly = findViewById(R.id.amountMonthly);
        depositMonthlyBtn = findViewById(R.id.depositMonthlyBtn);

        items = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.monthlyAccounts)));

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);

        adapter.add(accounts.get(1));


        for (int i = 0; i < accounts.size() ; i++) {
            try {
                if (accounts.get(i).getType() != null && accounts.get(i).getType().equals(getString(R.string.SAVINGS))){
                    adapter.add(accounts.get(i));
                }
            }
            catch (NullPointerException npE){
                System.out.println("Halløjsovs det gik ikke");
            }

        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccount.setAdapter(adapter);
        spinnerAccount.setOnItemSelectedListener(this);


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

    private boolean validateMonthlyPayment() {
        if (Double.parseDouble(amountMonthly.getText().toString()) < 0) {
            Toast.makeText(this, getString(R.string.amountGreaterThanZero), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void autoPayEveryMonthAlarm(Context context) {
        int HOUR = 60 * 60 * 1000;
        Intent intent = new Intent(context, MonthlyAutoDepositReceiver.class);
        intent.putExtra(getString(R.string.intentAffiliate), user.getAffiliate());
        intent.putExtra(getString(R.string.intentAutoNumber), number);
        intent.putExtra(getString(R.string.intentUserEmail), user.getEmail());
        intent.putExtra(getString(R.string.intentUserAccountBalance), selectedAccount.getBalance());
        intent.putExtra(getString(R.string.intentAutoAmount), Double.parseDouble(amountMonthly.getText().toString()));

        String concatRequestCode = number + context.getString(R.string.one);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, Integer.parseInt(concatRequestCode), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, pendingIntent);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedAccount = (AccountModel) spinnerAccount.getSelectedItem();
        number = getAccountDatabaseNumber(selectedAccount);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}