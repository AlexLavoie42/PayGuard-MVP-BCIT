package ca.payguard.util;

import android.media.session.MediaSession;

import java.util.Hashtable;

import ca.payguard.Customer;

public class Transaction {
    private Hashtable<Integer, AuthToken> tokenHash;
    private TransactionHandler transHandler;

    public Transaction(){
        tokenHash = new Hashtable<>();
    }

    public Customer newCustomer(String serverPin){
        transHandler = new CanadaPreAuth();
        return null;
    }

    public Boolean executeTransaction(String id, String serverPin){
        return null;
    }
}
