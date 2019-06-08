package com.example.bankapp;

import android.location.Location;
import com.example.bankapp.Model.CustomerModel;

public interface MyCallBack {
    void onCallBack(CustomerModel value);

    void onCallBackLocation(Location value);

    void onCallBackBalance(Double value);
}
