package ca.payguard.paymentUtil;

import ca.payguard.dbUtil.DatabaseController;

public class Audit {

    private static Runnable dummyRunnable = new Runnable() {
        @Override
        public void run() {}
    };

    /** If invalid serverPin, throws NotAuthorized Exception. */
    public static void audit(String pin, String reason) throws NotAuthorized{

        if(pin == null || pin.equals("")){
            throw new NotAuthorized();
        }else{
            try{
                DatabaseController dbc = new DatabaseController();
                dbc.checkPin(pin, dummyRunnable);
                sendToDb(reason);
            }catch(DatabaseController.AuthNotFoundError e){
                System.out.println("Auth Exception: " + e.getLocalizedMessage());
                throw new NotAuthorized();
            }catch(Exception e){
                System.out.println("Unknown Exception: " + e.getLocalizedMessage());
                throw new NotAuthorized();
            }
        }
    }

    private String retrieveFromDb(){
        return null;
    }

    private static void sendToDb(String reason){
        //TODO: Send the log to Db.
    }
}
