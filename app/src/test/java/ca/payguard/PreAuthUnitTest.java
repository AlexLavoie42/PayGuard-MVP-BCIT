package ca.payguard;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.payguard.dbUtil.DatabaseController;
import ca.payguard.paymentUtil.CanadaPreAuth;
import ca.payguard.paymentUtil.Transaction;

import static org.junit.Assert.assertEquals;

public class PreAuthUnitTest {

    @Test
    public void PreAuthFlow() {
        Transaction transaction = new Transaction();
        CanadaPreAuth preAuth = new CanadaPreAuth();
        preAuth.setOrderId("PG-type1-00002");
        preAuth.setPan("4242424242424242"); // Test Visa card
        preAuth.setExpDate("2103"); // YY/MM
        transaction.newCustomer("serverPin", preAuth);
        try{
            transaction.executeTransaction("1", "serverPin", "5.00"); // DO NOT TOUCH AMOUNT
            transaction.completeTransaction("1", "serverPin", "4.00"); //DO NOT TOUCH AMOUNT
        }catch(Exception e){
            System.out.println(e.toString());
            Assert.fail();
        }


    }
}
