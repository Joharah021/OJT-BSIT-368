package com.example.cakeshop;

public class TemporaryState {
    /*
    * TemporaryState class stores temporary data eg user id,accounttype,previous location
    *
    *
    * */

    public static final String APIKEY = "eKMEGhcXg6ilFQYVusMGeibSFKCMsUDl";
    public static double latitude =0;
    public static double longitude =0;
    public static final String USER ="USER";
    public  static final  String VENDOR ="VENDOR";
    public  static boolean isCustomer = false; //if not customer then its vendor
    public static  String userID = null;
    public  static String accountType = null;
    public  static double Servicefee = 30.00;
}
