package ca.payguard.smsUtil;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;

import java.io.IOException;
import java.text.DecimalFormat;

// Vonage SMS API Service - Trial Account
// Receiving and Sending messages cost Euros

// TODO Add Receive Function, Send Bill w/ tip prompt, parse tip
// TODO String result currently SOP

public class VonageSMS implements ISMS{

    private static final String KEY = "f88badc2";
    private static final String SECRET = "lDHAl35PoS7nplHG";
    private static final String FROM_NUMBER = "16139630547"; // Trial virtual Ottawa number

    private static AuthMethod auth;
    private static NexmoClient client;

    private static DecimalFormat formatter = new DecimalFormat("#.##");

    public VonageSMS() {

        auth = new TokenAuthMethod(KEY, SECRET);
        client = new NexmoClient(auth);

    }

    // Add initial SMS showing confirmed pre-auth to
    // Assume phone number is pre-formatted in E.164 format
    // E.164 Formatting example 16041234567
    public void sendAuthConfirmedSMS(String toNum, double authLimit, String restaurantName) {

        String authBody = "Thank you for using PayGuard at " + restaurantName
                + ". Your pre-authorization of $" + formatter.format(authLimit) + " has been received. " +
                "Please reply DONE to receive your bill.";

        TextMessage message = new TextMessage(FROM_NUMBER, toNum, authBody);
        try {
            SmsSubmissionResult result = client.getSmsClient().submitMessage(message)[0];
            System.out.println(result);
        } catch (NexmoClientException | IOException e) {
            System.err.println("Exception when calling API");
            e.printStackTrace();
        }
    }

    // Test method to ping Zach's phone from virtual Ottawa number
    // Costs 0.0122 Euros per message
    public void sendTestSMS(String body) {
        TextMessage message = new TextMessage(FROM_NUMBER, "16046448811", body);

        try {
            SmsSubmissionResult result = client.getSmsClient().submitMessage(message)[0];
            System.out.println(result);
        } catch (NexmoClientException | IOException e) {
            System.err.println("Exception when calling API");
            e.printStackTrace();
        }
    }

}
