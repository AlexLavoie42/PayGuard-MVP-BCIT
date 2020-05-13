package ca.payguard.paymentUtil;

import android.content.Intent;

import java.security.InvalidKeyException;
import java.util.Hashtable;

import ca.payguard.Customer;

public class Transaction {
    private Hashtable<String, AuthToken> tokenHash;
    private TransactionHandler transHandler;

    public Transaction(){
        tokenHash = new Hashtable<>();
    }

    public void newTransaction(TransactionHandler preAuthUnit){
        transHandler = preAuthUnit;
    }

    /**
     * Executes the initial Pre-authorization. This will get the pre-auth token from Moneris.
     * @param id Id of the customer
     * @return If token is recieved will return true. If no token, then returns false.
     */
    public boolean executeTransaction(String id, String amount){
        try{
            String reason = "Pre-authorizing " + amount + " to " + id;
//            Audit.audit(serverPin, reason);
            AuthToken token = transHandler.executeTransaction(amount); //Enter dollars here.
            tokenHash.put(id, token);
            return true;
//        }catch(NotAuthorized e){
//            System.out.println(e.toString());
//            return false;
        }catch(Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * Completes the transaction. This will send the final bill total to Moneris.
     * @param id Id of the customer
     * @param amount Total bill amount.
     * @return If token is received will return true. If no token, then returns false.
     */
    public boolean completeTransaction(String id, String amount){
        try{
            String reason = "Completing " + amount + " to " + id;
//            Audit.audit(serverPin, reason);
            AuthToken token = tokenHash.get(id);
            if(token == null) throw new InvalidKeyException();
            token.completeTransaction(amount);
            tokenHash.remove(id);
            return true;
//        }catch(NotAuthorized e){
//            System.out.println(e.toString());
//            return false;
        }catch(InvalidKeyException e){
            System.out.println(e.toString());
            return false;
        }catch(Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

}
