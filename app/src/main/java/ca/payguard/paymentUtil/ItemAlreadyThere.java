package ca.payguard.paymentUtil;

public class ItemAlreadyThere extends Exception {
    public ItemAlreadyThere(String errorMessage, Throwable err){
        super(errorMessage, err);
    }

    public ItemAlreadyThere(Throwable err){
        super("An Item is already there.", err);
    }

    public ItemAlreadyThere(){
        super("An Item is already there.");
    }
}
