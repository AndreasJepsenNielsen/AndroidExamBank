package com.example.bankapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.R;
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
        user = getIntent().getParcelableExtra(getString(R.string.intentUser));
        accounts = getIntent().getParcelableArrayListExtra(getString(R.string.intentAccounts));
        init();
        checkAccounts(accounts);
    }

    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     *
     * moves the accounts forward in the array.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                if (items.get(1).equals(getResources().getStringArray(R.array.applyForAccountsItems)[1])) {
                    accountName = getString(R.string.BUSINESS);
                } else if (items.get(1).equals(getResources().getStringArray(R.array.applyForAccountsItems)[2])) {
                    accountName = getString(R.string.SAVINGS);
                } else if (items.get(1).equals(getResources().getStringArray(R.array.applyForAccountsItems)[3])) {
                    accountName = getString(R.string.PENSION);
                }
                break;

            case 2:
                if (items.get(2).equals(getResources().getStringArray(R.array.applyForAccountsItems)[2])) {
                    accountName = getString(R.string.SAVINGS);
                } else if (items.get(2).equals(getResources().getStringArray(R.array.applyForAccountsItems)[3])) {
                    accountName = getString(R.string.PENSION);
                }
                break;

            case 3:
                accountName = getString(R.string.PENSION);
                break;
            default:
                accountName = getString(R.string.NONE);
        }

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!accountName.equals(getString(R.string.NONE))) {
                    AccountModel newAccount = new AccountModel(0, accountName);
                    accounts.add(newAccount);
                    applyForAccount(accountName, newAccount);

                    Intent backToMenu = new Intent(ApplyForAccountsActivity.this, MenuActivity.class);
                    backToMenu.putParcelableArrayListExtra(getString(R.string.intentAccounts), accounts);
                    startActivity(backToMenu);
                    finish();
                } else {
                    Toast.makeText(ApplyForAccountsActivity.this, getString(R.string.chooseAnAccount), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     *
     * @param type
     * @param newAccount
     *
     * check type of account and put it in the DB
     */
    private void applyForAccount(String type, AccountModel newAccount) {
        String number = getString(R.string.zero);
        if (type.equals(getString(R.string.BUSINESS))) {
            adapter.notifyDataSetChanged();
            number = getString(R.string.two);
        } else if (type.equals(getString(R.string.SAVINGS))) {
            adapter.notifyDataSetChanged();
            number = getString(R.string.three);
        } else if (type.equals(getString(R.string.PENSION))) {
            adapter.notifyDataSetChanged();
            number = getString(R.string.four);
        }
        try {
            myref.child(getString(R.string.pathSlash) + user.getAffiliate() + getString(R.string.pathUserSlash) + user.getEmail().replace(".", "") + getString(R.string.pathAccountSlash) + number).setValue(newAccount);
        } catch (NullPointerException npE) {
            npE.printStackTrace();
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

    /**
     *
     * @param accountArrayList
     *
     * check which account the user have and only show the accounts the user don't have
     */
    private void checkAccounts(ArrayList<AccountModel> accountArrayList) {
        for (int i = 0; i < accountArrayList.size(); i++) {
            if (accountArrayList.get(i) != null) {
                for (int j = 0; j < items.size(); j++) {

                    if (accountArrayList.get(i).getType().equals(getString(R.string.BUSINESS)) && items.get(j).equals(getResources().getStringArray(R.array.applyForAccountsItems)[1])) {
                        items.remove(j);
                    }

                    if (accountArrayList.get(i).getType().equals(getString(R.string.SAVINGS)) && items.get(j).equals(getResources().getStringArray(R.array.applyForAccountsItems)[2])) {
                        items.remove(j);
                    }

                    if (accountArrayList.get(i).getType().equals(getString(R.string.PENSION)) && items.get(j).equals(getResources().getStringArray(R.array.applyForAccountsItems)[3])) {
                        items.remove(j);
                    }
                }
            }
        }
    }
}
