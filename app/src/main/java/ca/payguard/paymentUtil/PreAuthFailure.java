package ca.payguard.paymentUtil;

public class PreAuthFailure extends Exception{
    public PreAuthFailure(String errorMessage, Throwable err){
        super(errorMessage, err);
    }

    public PreAuthFailure(Throwable err){
        super("Error in Pre-Authorization.", err);
    }

    public PreAuthFailure(){
        super("Error in Pre-Authorization.");
    }
}
