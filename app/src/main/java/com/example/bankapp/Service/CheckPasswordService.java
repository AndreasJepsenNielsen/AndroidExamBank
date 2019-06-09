package com.example.bankapp.Service;

public class CheckPasswordService {
    public boolean checkPassword(String str) {
        char ch;
        boolean hasCapital = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (Character.isUpperCase(ch)) {
                hasCapital = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            }
            if (hasNumber && hasCapital && hasLowerCase)
                return true;
        }
        return false;
    }
}
