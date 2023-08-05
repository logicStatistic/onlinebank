package com.onlinebanking.onlinebank.entityutils;

import java.time.Year;

public class AccountUtility {
    //custom response messages
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This customer already exist";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account registered successfully";
    public static final String ACCOUNTNUMBER_EXISTS_CODE = "003";
    public static final String ACCOUNTNUMBER_EXISTS_MESSAGE = "Customer not found";
    public static final String ACCOUNTNUMBER_EXISTS_SUCCESS = "004";
    public static final String ACCOUNTNUMBER_EXISTS_MESSAGESUCCESS = "Customer found";
    public static final String ACCOUNT_CREDIT_CODE = "005";
    public static final String ACCOUNT_CREDIT_SUCCESSMSG = "Your account have been credited successfully";
    public static final String ACCOUNT_DEBIT_CODE = "006";
    public static final String ACCOUNT_DEBIT_FAILURE ="Insufficient fund";
    public static final String ACCOUNT_DEBIT_SUCCESS = "Withdrawn successfully";
    public static final String ACCOUNT_TRANSFER_CODE = "007";
    public static final String ACCOUNT_TRANSFER_SUCCESS = "Transferred successfully";
    public static String generateAccountNumber(){
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        int i = (max -min + 1) + min;
        int randomNumber = (int) (Math.floor(Math.random() * i));
        //convert to string
        String year = String.valueOf(currentYear);
        String randNumber = String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randNumber).toString();
    }
}
