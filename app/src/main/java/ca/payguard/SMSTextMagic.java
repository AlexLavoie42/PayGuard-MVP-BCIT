package ca.payguard;


import com.textmagic.sdk.RestClient;
import com.textmagic.sdk.RestException;
import com.textmagic.sdk.resource.instance.TMNewMessage;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;

// TextMagic Trial Account. Expires June 4, 2020

public class SMSTextMagic {

    private static final String USERNAME = "alexlavoie";
    private static final String API_KEY = "cj8PSX8Mcao7QFraqwCJouS6STPdeE";
    private static final String TEST_NUM = "16048322275";

    private static RestClient client;

    private static DecimalFormat formatter = new DecimalFormat("#.##");


    public SMSTextMagic() {
        client = new RestClient(USERNAME, API_KEY);
    }

    // Send initial SMS showing confirmed pre-auth to
    // Assume phone number is pre-formatted in E.164 format - Eg. 16041234567
    public void sendAuthConfirmedSMS(String toNum, double authLimit, String restaurantName) {

        TMNewMessage message = client.getResource(TMNewMessage.class);
        message.setText("Thank you for using PayGuard at " + restaurantName
                + ". Your pre-authorization of $" + formatter.format(authLimit) + " has been received. " +
                "Please reply DONE to receive your bill.");
        message.setPhones(Collections.singletonList(toNum));
        try {
            message.send();
        }
        catch (final RestException e) {
            System.out.println(e.getErrorCode() + " " + e.getErrorMessage() + " Num: " + toNum);
        }
        System.out.println(message.getId());

    }

    // Test method to ping registered number
    public void sendTestSMS(String body) {
        TMNewMessage message = client.getResource(TMNewMessage.class);
        message.setText(body);
        message.setPhones(Collections.singletonList(TEST_NUM));
        try {
            message.send();
        }
        catch (final RestException e) {
            System.out.println(e.getErrorCode() + e.getErrorMessage());
        }
        System.out.println(message.getId());
    }
}
