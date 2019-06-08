package com.example.bankapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CustomerModel implements Parcelable {

    private String email;
    private String password;
    private String address;
    private String firstName;
    private String lastName;
    private String SSN;
    private String phoneNumber;
    private ArrayList<AccountModel> accounts;
    private AccountModel defaultAccount;
    private AccountModel budgetAccount;
    private String affiliate;


    public CustomerModel(String SSN, String email, String password, String address, String firstName, String lastName, String phoneNumber, String affiliate) {
        this.SSN = SSN;
        this.email = email;
        this.password = password;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
        defaultAccount = new AccountModel(0.0, "DEFAULT");
        budgetAccount = new AccountModel(0.0, "BUDGET");
        this.accounts.add(defaultAccount);
        this.accounts.add(budgetAccount);
        this.affiliate = affiliate;
    }

    public CustomerModel() {
    }

    protected CustomerModel(Parcel in) {
        email = in.readString();
        password = in.readString();
        address = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        SSN = in.readString();
        phoneNumber = in.readString();
        affiliate = in.readString();
    }

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel in) {
            return new CustomerModel(in);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<AccountModel> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<AccountModel> accounts) {
        this.accounts = accounts;
    }

    public String getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(String affiliate) {
        this.affiliate = affiliate;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", SSN='" + SSN + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", accounts=" + accounts +
                ", affiliate='" + affiliate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(address);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(SSN);
        parcel.writeString(phoneNumber);
        parcel.writeString(affiliate);
    }
}
