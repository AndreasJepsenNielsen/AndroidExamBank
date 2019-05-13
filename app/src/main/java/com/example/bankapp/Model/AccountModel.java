package com.example.bankapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountModel implements Parcelable {
    private double balance;
    private String type;

    public AccountModel(double balance ,String type) {
        this.balance = balance;
        this.type = type;
    }

    public AccountModel() {
    }

    protected AccountModel(Parcel in) {
        balance = in.readDouble();
        type = in.readString();
    }

    public static final Creator<AccountModel> CREATOR = new Creator<AccountModel>() {
        @Override
        public AccountModel createFromParcel(Parcel in) {
            return new AccountModel(in);
        }

        @Override
        public AccountModel[] newArray(int size) {
            return new AccountModel[size];
        }
    };

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "balance=" + balance +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(balance);
        parcel.writeString(type);
    }
}
