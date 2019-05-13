package com.example.bankapp.Model;

public class DefaultAccountModel {
    private double balance;

    public DefaultAccountModel(double balance) {
        this.balance = balance;
    }

    public DefaultAccountModel() {
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "DefaultAccountModel{" +
                "balance=" + balance +
                '}';
    }
}
