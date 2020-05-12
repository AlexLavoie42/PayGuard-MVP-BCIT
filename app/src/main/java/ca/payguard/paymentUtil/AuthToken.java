package ca.payguard.paymentUtil;

import JavaAPI.*;

class AuthToken {

    private Receipt token;
    private String orderId;

    AuthToken(Receipt receipt, String orderId){
        token = receipt;
        this.orderId = orderId;
    }

    boolean completeTransaction(String amount) throws PreAuthFailure {
        //TODO: Get Production keys & id
        String store_id = "store5"; // TestAPI, switch when in production
        String api_token = "yesguy";// TestAPI, switch when in production.
        String crypt = "7"; //TODO: Check if right crypt to be using.
        String storeName = "PayGuard"; // Must be <= 13 char.
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
            if(receipt.getComplete().equalsIgnoreCase("false")){
                throw new PreAuthFailure("Token failed completion: " + receipt.getMessage());
            }
            return true;
        }
        catch (Exception e)
        {
            throw new PreAuthFailure("Token failed completion: " + e.toString());
        }
    }
}
