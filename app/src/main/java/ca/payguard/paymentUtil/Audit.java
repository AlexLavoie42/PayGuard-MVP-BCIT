package ca.payguard.paymentUtil;

import ca.payguard.dbUtil.DatabaseController;

class Audit {

    private static Runnable dummyRunnable = new Runnable() {
        @Override
        public void run() {}
    };

    /** If invalid serverPin, throws NotAuthorized Exception. */
    static void audit(String pin, String reason) throws NotAuthorized{
        if(pin == null || pin.equals("")){
            throw new NotAuthorized();
        }else{
            DatabaseController dbc = new DatabaseController();
            dbc.checkPin(pin, dummyRunnable); //TODO: Refactor this as this is bad code.
            sendToDb(reason);
        }
    }

    private String retrieveFromDb(){
        return null;
    }

    private static void sendToDb(String reason){
        //TODO: Send the log to Db.
    }
}
