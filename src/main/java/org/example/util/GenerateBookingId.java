package org.example.util;

import java.security.SecureRandom;

public class GenerateBookingId {

    private final static String generatedNumber = "AB0CDEFGH9IJKL1MNOPQRST2UVWXYZab5cdefg0hijk3lmno7pq4rs6tuvwxy8z1234567890";
    private final static SecureRandom secureRandom = new SecureRandom();

    public static  String generateId() {
        String generatedValue = "";
        for (int count = 0; count < 3; count++) {
            String number = generatedNumber.charAt(secureRandom.nextInt(62)) + "";
            generatedValue += number;
        }
        return generatedValue;
    }
}
