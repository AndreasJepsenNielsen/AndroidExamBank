package com.example.bankapp.Service;

import android.content.Context;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.R;

public class GetNumberService {

    public String getNumber(Context context, AccountModel account) {
        String number = context.getString(R.string.zero);
        if(account.getType().equals(context.getString(R.string.BUDGET))){
            number = context.getString(R.string.one);
        }

        if(account.getType().equals(context.getString(R.string.BUSINESS))){
            number = context.getString(R.string.two);

        }

        if(account.getType().equals(context.getString(R.string.SAVINGS))){
            number = context.getString(R.string.three);

        }
        if(account.getType().equals(context.getString(R.string.PENSION))){
            number = context.getString(R.string.four);

        }

        return number;
    }

}
