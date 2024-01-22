package org.example.util;

public class Verification {

    public static boolean verifyPassword(String password){
        return password.matches("[A-Z][A-Za-z]{2,}[1-9@#+$%^<:;_!&*>?/|]{1,7}");

    }
    public static  boolean verifyEmail(String email){
        return email.matches("[a-zA-Z\\d+/_@.-]+@[a-z]+[/.][a-z]{2,3}");
    }
    public static boolean verifyPhoneNumber(String phoneNumber){
        if(phoneNumber.startsWith("+")) return phoneNumber.matches("[+][1-9][0-9]{6,12}");
        else return phoneNumber.matches("0[7-9][0-1][0-9]{8}");
    }


}
