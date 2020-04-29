package ca.payguard.util;

public interface TransactionHandler {
    public boolean setStoreId(String id);

    public boolean setDate(String date);

    public boolean setOrderId(String id);

    /** The numbers on the front of the card. */
    public boolean setPan(String pan);

    public boolean setExpDate(String date);

    /** Executes the initial transaction. Takes the pre-auth $ amount and returns an AuthToken. */
    public AuthToken executeTransaction(String dollars);

    /** Completes the Pre-auth transaction. Takes the final total $ amount. */
    public boolean completeTransaction(String dollars);
}
