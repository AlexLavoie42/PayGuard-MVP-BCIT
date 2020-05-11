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

    public Customer newCustomer(String serverPin, TransactionHandler preAuthUnit){
        transHandler = preAuthUnit;
        return null;
    }

    /**
     * Executes the initial Pre-authorization. This will get the pre-auth token from Moneris.
     * @param id Id of the customer
     * @param serverPin Servers audit pin. If its invalid will throw NotAuthorized erro.
     * @return If token is recieved will return true. If no token, then returns false.
     */
    public boolean executeTransaction(String id, String serverPin, String amount){
        try{
            String reason = "Pre-authorizing " + amount + " to " + id;
//            Audit.audit(serverPin, reason);
            //TODO: Build the transaction.
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
     * @param serverPin Servers audit pin. If its invalid will throw NotAuthorized error.
     * @param amount Total bill amount.
     * @return If token is received will return true. If no token, then returns false.
     */
    public boolean completeTransaction(String id, String serverPin, String amount){
        try{
            String reason = "Completing " + amount + " to " + id;
//            Audit.audit(serverPin, reason);
            AuthToken token = tokenHash.get(id);
            if(token == null) throw new InvalidKeyException();
            token.completeTransaction(amount);
            //TODO: Delete transaction.
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

    //TODO: Move this to the audit class.

}
