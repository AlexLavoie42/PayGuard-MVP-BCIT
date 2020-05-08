package ca.payguard.paymentUtil;

import JavaAPI.*;

public class CanadaPreAuth implements TransactionHandler
{

    private PreAuth preAuth;
    private HttpsPostRequest mpgReq;
    private String orderId;

    //TODO: CHANGE API CODE AND STORE FOR

    public CanadaPreAuth(){
        String crypt = "7"; //TODO: Check if this is the right crypt to be using.
        preAuth = new PreAuth();
        mpgReq = new HttpsPostRequest();
        mpgReq.setTestMode(true); //false or comment out this line for production transactions
        preAuth.setCryptType(crypt);
        mpgReq.setProcCountryCode("CA");
        mpgReq.setStoreId("store5"); // TODO: Change this for production
        String api_token = "yesguy"; //TODO: Get a production api key.
        boolean status_check = false; //TODO: Should change this to true and test.
        mpgReq.setApiToken(api_token);
        mpgReq.setStatusCheck(status_check);
    }

//    public static void execute()
//    {
//        String store_id = "store5";
//        String api_token = "yesguy";
//        java.util.Date createDate = new java.util.Date();
//        String order_id = "Test"+createDate.getTime();
//        String amount = "5.00";
//        String pan = "4242424242424242";
//        String expdate = "1902";
//        String crypt = "7";
//        String processing_country_code = "CA";
//        boolean status_check = false;
//
//        PreAuth preauth = new PreAuth();
//        preauth.setOrderId(order_id);
//        preauth.setAmount(amount);
//        preauth.setPan(pan);
//        preauth.setExpdate(expdate);
//        preauth.setCryptType(crypt);
        //preauth.setWalletIndicator(""); //Refer documentation for possible values

        //Optional - Set for Multi-Currency only
        //setAmount must be 0.00 when using multi-currency
        //preauth.setMCPAmount("500"); //penny value amount 1.25 = 125
        //preauth.setMCPCurrencyCode("840"); //ISO-4217 country currency number

        //optional - Credential on File details
//        CofInfo cof = new CofInfo();
//        cof.setPaymentIndicator("U");
//        cof.setPaymentInformation("2");
//        cof.setIssuerId("139X3130ASCXAS9");
//
//        preauth.setCofInfo(cof);

//        HttpsPostRequest mpgReq = new HttpsPostRequest();
//        mpgReq.setProcCountryCode(processing_country_code);
//        mpgReq.setTestMode(true); //false or comment out this line for production transactions
//        mpgReq.setStoreId(store_id);
//        mpgReq.setApiToken(api_token);
//        mpgReq.setTransaction(preauth);
//        mpgReq.setStatusCheck(status_check);
//        mpgReq.send();

//        try
//        {
//            Receipt receipt = mpgReq.getReceipt();
//
//            System.out.println("CardType = " + receipt.getCardType());
//            System.out.println("TransAmount = " + receipt.getTransAmount());
//            System.out.println("TxnNumber = " + receipt.getTxnNumber());
//            System.out.println("ReceiptId = " + receipt.getReceiptId());
//            System.out.println("TransType = " + receipt.getTransType());
//            System.out.println("ReferenceNum = " + receipt.getReferenceNum());
//            System.out.println("ResponseCode = " + receipt.getResponseCode());
//            System.out.println("ISO = " + receipt.getISO());
//            System.out.println("BankTotals = " + receipt.getBankTotals());
//            System.out.println("Message = " + receipt.getMessage());
//            System.out.println("AuthCode = " + receipt.getAuthCode());
//            System.out.println("Complete = " + receipt.getComplete());
//            System.out.println("TransDate = " + receipt.getTransDate());
//            System.out.println("TransTime = " + receipt.getTransTime());
//            System.out.println("Ticket = " + receipt.getTicket());
//            System.out.println("TimedOut = " + receipt.getTimedOut());
//            System.out.println("IsVisaDebit = " + receipt.getIsVisaDebit());
//            //System.out.println("StatusCode = " + receipt.getStatusCode());
//            //System.out.println("StatusMessage = " + receipt.getStatusMessage());
////            System.out.println("MCPAmount = " + receipt.getMCPAmount());
////            System.out.println("MCPCurrencyCode = " + receipt.getMCPCurrencyCode());
//            System.out.println("IssuerId = " + receipt.getIssuerId());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void setDate(String date) {
        //TODO: Check if we actually need this.
    }

    @Override
    public void setOrderId(String id) {
        //TODO: Make this so it increments upwards.
        preAuth.setOrderId(id);
        orderId = id;
    }

    @Override
    public void setPan(String pan) {
        preAuth.setPan(pan);
    }

    @Override
    public void setExpDate(String date) {
        preAuth.setExpdate(date);
    }

    @Override
    public AuthToken executeTransaction(String dollars) {
        mpgReq.setTransaction(preAuth);
        preAuth.setAmount(dollars);
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
            //System.out.println("StatusCode = " + receipt.getStatusCode());
            //System.out.println("StatusMessage = " + receipt.getStatusMessage());
//            System.out.println("MCPAmount = " + receipt.getMCPAmount());
//            System.out.println("MCPCurrencyCode = " + receipt.getMCPCurrencyCode());
            System.out.println("IssuerId = " + receipt.getIssuerId());
            return new AuthToken(receipt, orderId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Token Failed to be created.");
        }
    }
}

                