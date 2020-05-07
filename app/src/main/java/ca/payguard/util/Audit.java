package ca.payguard.util;

class Audit {

    /** If invalid serverPin, throws NotAuthorized Exception. */
    static void audit(String pin) throws NotAuthorized{
        if(pin == null || pin.equals("")){
            throw new NotAuthorized();
        }
    }

    private String retrieveFromDb(){
        return null;
    }

    private void sendToDb(){

    }
}
