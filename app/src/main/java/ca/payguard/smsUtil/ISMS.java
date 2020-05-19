package ca.payguard.smsUtil;

/**
 * Interface for interacting with various sms api's.
 * Uses E.164 Formatting for all API's
 * TODO: Receiving tip amount from text and sending bill total after bill is sent
 */
public interface ISMS {

    public void sendAuthConfirmedSMS(String toNum, double authLimit, String restaurantName);
}
