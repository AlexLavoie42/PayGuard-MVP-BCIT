package ca.payguard.util;

public interface TransactionHandler {
    public boolean setStoreId(String id);

    public boolean setDate(String date);

    public boolean setOrderId(String id);

    public boolean setPan(String pan);

    public boolean setExpDate(String date);

    public AuthToken executeTransaction(String dollars);

    public boolean completeTransaction(String dollars);
}
