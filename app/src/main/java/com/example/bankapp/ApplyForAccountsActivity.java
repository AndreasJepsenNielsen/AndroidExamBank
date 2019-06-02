package com.example.bankapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ApplyForAccountsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();
    ArrayList<AccountModel> accounts;
    CustomerModel user;
    String accountName;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    Spinner dropDownApplyAccounts;
    Button applyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_accounts);
        user = getIntent().getParcelableExtra("user");
        accounts = getIntent().getParcelableArrayListExtra("accounts");
        init();
        checkAccounts(accounts);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 1:
                if(items.get(1).equals(getResources().getStringArray(R.array.applyForAccountsItems)[1])){
                    accountName = "BUSINESS";
                }else if(items.get(1).equals(getResources().getStringArray(R.array.applyForAccountsItems)[2])){
                    accountName = "SAVINGS";
                }else if(items.get(1).equals(getResources().getStringArray(R.array.applyForAccountsItems)[3])){
                accountName = "PENSION";

                }

                break;

            case 2:
                if(items.get(2).equals(getResources().getStringArray(R.array.applyForAccountsItems)[2])){
                    accountName = "SAVINGS";
                }else if(items.get(2).equals(getResources().getStringArray(R.array.applyForAccountsItems)[3])){
                    accountName = "PENSION";

                }
                break;

            case 3:
                accountName = "PENSION";
                break;
        }

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountModel newAccount = new AccountModel(0, accountName);
                accounts.add(newAccount);
                applyForAccount(accountName, newAccount);

                Intent backToMenu = new Intent(ApplyForAccountsActivity.this, MenuActivity.class);
                backToMenu.putParcelableArrayListExtra("accounts", accounts);
                startActivity(backToMenu);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(ApplyForAccountsActivity.this, "Please choose an account", Toast.LENGTH_LONG).show();
    }


    private void applyForAccount(String type, AccountModel newAccount){
        String number = "0";
        if(type.equals("BUSINESS")) {
            adapter.notifyDataSetChanged();
            number = "2";
        }
        else if(type.equals("SAVINGS")) {
            adapter.notifyDataSetChanged();
            number = "3";
        }
        else if(type.equals("PENSION")) {
            adapter.notifyDataSetChanged();
            number = "4";
        }


        try {
           myref.child("/" + user.getAffiliate() + "/users/" + user.getEmail().replace(".","") + "/accounts/" + number).setValue(newAccount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        dropDownApplyAccounts = findViewById(R.id.applyDropDown);
        applyBtn = findViewById(R.id.applyBtn);

        items = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.applyForAccountsItems)));

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownApplyAccounts.setAdapter(adapter);
        dropDownApplyAccounts.setOnItemSelectedListener(this);
    }

    private void checkAccounts(ArrayList<AccountModel> accountArrayList) {
        System.out.println(accountArrayList);
        System.out.println(getResources().getStringArray(R.array.applyForAccountsItems)[1]);
        System.out.println(getResources().getStringArray(R.array.applyForAccountsItems)[2]);
        System.out.println(getResources().getStringArray(R.array.applyForAccountsItems)[3]);

        for (int i = 0; i < accountArrayList.size(); i++) {
            if (accountArrayList.get(i) != null) {
                for (int j = 0; j < items.size() ; j++) {


                    if (accountArrayList.get(i).getType().equals("BUSINESS") && items.get(j).equals(getResources().getStringArray(R.array.applyForAccountsItems)[1])) {
                        System.out.println(items.get(j).equals(getResources().getStringArray(R.array.applyForAccountsItems)[1]));
                        System.out.println("jeg er business");

                       items.remove(j);

                    }

                    if (accountArrayList.get(i).getType().equals("SAVINGS") && items.get(j).equals(getResources().getStringArray(R.array.applyForAccountsItems)[2])) {
                        System.out.println(getResources().getStringArray(R.array.applyForAccountsItems)[2]);
                        System.out.println(items.get(j));
                        System.out.println("jeg er savings");
                        items.remove(j);


                    }

                    if (accountArrayList.get(i).getType().equals("PENSION") && items.get(j).equals(getResources().getStringArray(R.array.applyForAccountsItems)[3])) {
                        System.out.println("jeg er pension");
                        items.remove(j);


                    }
                }
            }
        }
    }

    /*
    private void checkAccounts(ArrayList<AccountModel> accountArrayList) {
        for (int i = 0; i < accountArrayList.size(); i++) {
            System.out.println(getResources().getStringArray(R.array.applyForAccountsItems)[i]);
            if (accountArrayList.get(i) != null){
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).contains(getResources().getStringArray(R.array.applyForAccountsItems)[1])) {
                        items.remove(j);
                    }

                    if (items.get(j).contains(getResources().getStringArray(R.array.applyForAccountsItems)[2])) {
                        items.remove(j);
                    }

                    if (items.get(j).contains(getResources().getStringArray(R.array.applyForAccountsItems)[3])) {
                        items.remove(j);
                    }
                }
            }
        }
    }
    */


}
