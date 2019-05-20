package com.example.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bankapp.Model.AccountModel;

import java.util.ArrayList;

public class DepositActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        AccountModel accounts = getIntent().getParcelableExtra("account");
    }


}
