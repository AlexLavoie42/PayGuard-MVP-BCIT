package ca.payguard.util;

public interface TransactionHandler {
    public void setStoreId(String id);

    public void setDate(String date);

    public void setOrderId(String id);

    /** The numbers on the front of the card. */
    public void setPan(String pan);

    public void setExpDate(String date);

    /** Executes the initial transaction. Takes the pre-auth $ amount and returns an AuthToken. */
    public AuthToken executeTransaction(String dollars);

    /** Completes the Pre-auth transaction. Takes the final total $ amount. */
    public boolean completeTransaction(String dollars);
}
