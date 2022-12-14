package com.homecomingday.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

public class Encrypt {

    public static String getSalt() {
        Random random = new Random();
        byte[] salt = new byte[10];

        random.nextBytes(salt);

        StringBuffer sb = new StringBuffer();

        for(int i=0; i<salt.length; i++) {
            sb.append(String.format("%02x", salt[i]));
        }
        return sb.toString();
    }

    public static String getEncrypt(String pwd, String salt) {
        byte[] salt1 = salt.getBytes();
        String result = "";

//        byte[] temp = pwd.getBytes();
//        byte[] bytes = new byte[temp.length + salt1.length];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(pwd.getBytes());
            md.update(salt.getBytes());

            byte[] b = md.digest();

           // StringBuffer sb = new StringBuffer();

            String hex = String.format("%064x", new BigInteger(1, b));
//            for(int i=0; i<b.length; i++) {
//                sb.append(Integer.toString((b[i] & 0xFF) + 256, 16).substring(1));
//            }

            result = hex;

        } catch (Exception e) {
        }

        return result;
    }
}
