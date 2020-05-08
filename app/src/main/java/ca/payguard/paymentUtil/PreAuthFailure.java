package ca.payguard.paymentUtil;

public class PreAuthFailure extends Exception{
    public PreAuthFailure(String errorMessage){
        super(errorMessage);
    }

    public PreAuthFailure(Throwable err){
        super("Error in Pre-Authorization.", err);
    }

    public PreAuthFailure(){
        super("Error in Pre-Authorization.");
    }
}
