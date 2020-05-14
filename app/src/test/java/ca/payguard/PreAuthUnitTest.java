package ca.payguard;

import org.junit.Test;
import ca.payguard.paymentUtil.CanadaPreAuth;
import ca.payguard.paymentUtil.Transaction;

import static org.junit.Assert.*;

public class PreAuthUnitTest {

    @Test
    public void PreAuthFlow() {
        Transaction transaction = new Transaction();
        CanadaPreAuth preAuth = new CanadaPreAuth();
        preAuth.setOrderId("PG-type1-00007");
        preAuth.setPan("4242424242424242"); // Test Visa card
        preAuth.setExpDate("2103"); // YY/MM
        transaction.newTransaction(preAuth);
        try{
            if(!transaction.executeTransaction("1", "5.00")) throw new Exception(); // DO NOT TOUCH AMOUNT
            if(!transaction.completeTransaction("1", "4.00")) throw new Exception(); //DO NOT TOUCH AMOUNT
        }catch(Exception e){
            fail("Error occurred: " + e.toString());
        }


    }
}
