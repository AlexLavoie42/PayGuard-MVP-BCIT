package ca.payguard.util;

public class NotAuthorized extends Exception {
    public NotAuthorized(String errorMessage, Throwable err){
        super(errorMessage, err);
    }

    public NotAuthorized(Throwable err){
        super("You are not authorized to access this.", err);
    }

    public NotAuthorized(){
        super("You are not authorized to access this.");
    }
}
