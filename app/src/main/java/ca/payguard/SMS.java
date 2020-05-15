package ca.payguard;

import ClickSend.Api.SmsApi;
import ClickSend.ApiClient;
import ClickSend.ApiException;
import ClickSend.Model.SmsMessage;
import ClickSend.Model.SmsMessageCollection;

import java.text.DecimalFormat;
import java.util.ArrayList;

// Source attribute??
// E.164 Formatting example 16041234567
// What to do with return result or errors

// TODO Add Receive Function, Send Bill w/ tip prompt, parse tip
// ClickSend SMS API - INACTIVE

public class SMS {

    private static final String USERNAME = "zachary@payguard.ca";
    private static final String API_KEY = "36373A4E-3864-A80C-0BA2-1B1553A134DF";
    private static final String SOURCE = "java";

    private static ApiClient api;
    private static SmsApi client;
    private static ArrayList<SmsMessage> messageQueue = new ArrayList<SmsMessage>();

    private static DecimalFormat formatter = new DecimalFormat("#.##");

    public SMS() {

        api  = new ApiClient();
        api.setUsername(USERNAME);
        api.setPassword(API_KEY);
        client = new SmsApi(api);

    }


    // Add initial SMS showing confirmed pre-auth to
    // Assume phone number is pre-formatted in E.164 format
    public void addAuthConfirmedSMS(String toNum, double authLimit, String restaurantName) {

        SmsMessage message = new SmsMessage();
        message.body("Thank you for using PayGuard at " + restaurantName
            + ". Your pre-authorization of $" + formatter.format(authLimit) + " has been received. " +
                "Please reply DONE to receive your bill.");
        message.to(toNum);
        message.source(SOURCE);

        messageQueue.add(message);
    }

    // Send all messages in queue
    public void sendAll() {

        SmsMessageCollection sendingPackage = new SmsMessageCollection();
        sendingPackage.messages(messageQueue);

        try {
            String result = client.smsSendPost(sendingPackage);
        } catch (ApiException e) {
            System.err.println("Exception when calling SmsApi#smsSendPost");
            e.printStackTrace();
        }
    }



}
