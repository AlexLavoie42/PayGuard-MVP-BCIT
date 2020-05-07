package ca.payguard.util;

import java.security.InvalidKeyException;
import java.util.Hashtable;

import ca.payguard.Customer;

public class Transaction {
    private Hashtable<String, AuthToken> tokenHash;
    private TransactionHandler transHandler;

    Transaction(){
        tokenHash = new Hashtable<>();
    }

    public Customer newCustomer(String serverPin){
        transHandler = new CanadaPreAuth();
        return null;
    }

    /**
     * Executes the initial Pre-authorization. This will get the pre-auth token from Moneris.
     * @param id Id of the customer
     * @param serverPin Servers audit pin. If its invalid will throw NotAuthorized erro.
     * @return If token is recieved will return true. If no token, then returns false.
     */
    boolean executeTransaction(String id, String serverPin, String amount){
        try{
            Audit.audit(serverPin);
            AuthToken token = transHandler.executeTransaction(amount); //Enter dollars here.
            tokenHash.put(id, token);
            return true;
        }catch(NotAuthorized e){
            System.out.println(e.toString());
            return false;
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
    boolean completeTransaction(String id, String serverPin, String amount){
        try{
            Audit.audit(serverPin);
            AuthToken token = tokenHash.get(id);
            if(token == null) throw new InvalidKeyException();
            token.completeTransaction(amount);
            return true;
        }catch(NotAuthorized e){
            System.out.println(e.toString());
            return false;
        }catch(InvalidKeyException e){
            System.out.println(e.toString());
            return false;
        }catch(Exception e){
            System.out.println(e.toString());
            return false;
        }
    }
}
