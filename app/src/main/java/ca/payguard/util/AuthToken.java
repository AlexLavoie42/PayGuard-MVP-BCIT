package ca.payguard.util;

import JavaAPI.*;

public class AuthToken {

    private Receipt token;
    private String orderId;

    public AuthToken(Receipt receipt, String orderId){
        token = receipt;
        this.orderId = orderId;
    }

    public void completeTransaction(String amount){
        String store_id = "store5";
        String api_token = "yesguy";//TODO: Get an actual api_token.
        String crypt = "7"; //TODO: Check if right crypt to be using.
        String storeName = "Store's Name"; // Must be <= 13 char.
        String processing_country_code = "CA";
        boolean status_check = false; //TODO: Should change this to true and test.

        Completion completion = new Completion();
        completion.setOrderId(orderId);
        completion.setCompAmount(amount);
        completion.setTxnNumber(token.getTxnNumber());
        completion.setCryptType(crypt);
        completion.setDynamicDescriptor(storeName);

        HttpsPostRequest mpgReq = new HttpsPostRequest();
        mpgReq.setProcCountryCode(processing_country_code);
        mpgReq.setTestMode(true); //false or comment out this line for production transactions
        mpgReq.setStoreId(store_id);
        mpgReq.setApiToken(api_token);
        mpgReq.setTransaction(completion);
        mpgReq.setStatusCheck(status_check);
        mpgReq.send();

        try
        {
            Receipt receipt = mpgReq.getReceipt();

            System.out.println("CardType = " + receipt.getCardType());
            System.out.println("TransAmount = " + receipt.getTransAmount());
            System.out.println("TxnNumber = " + receipt.getTxnNumber());
            System.out.println("ReceiptId = " + receipt.getReceiptId());
            System.out.println("TransType = " + receipt.getTransType());
            System.out.println("ReferenceNum = " + receipt.getReferenceNum());
            System.out.println("ResponseCode = " + receipt.getResponseCode());
            System.out.println("ISO = " + receipt.getISO());
            System.out.println("BankTotals = " + receipt.getBankTotals());
            System.out.println("Message = " + receipt.getMessage());
            System.out.println("AuthCode = " + receipt.getAuthCode());
            System.out.println("Complete = " + receipt.getComplete());
            System.out.println("TransDate = " + receipt.getTransDate());
            System.out.println("TransTime = " + receipt.getTransTime());
            System.out.println("Ticket = " + receipt.getTicket());
            System.out.println("TimedOut = " + receipt.getTimedOut());
            System.out.println("IsVisaDebit = " + receipt.getIsVisaDebit());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
