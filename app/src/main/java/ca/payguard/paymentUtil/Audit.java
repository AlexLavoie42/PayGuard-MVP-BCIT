package ca.payguard.paymentUtil;

import android.content.Intent;

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
                serverAudit(pin, reason);
            }catch(NotAuthorized e){
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

    private static void serverAudit(String serverPin, String reason) throws NotAuthorized {
        Intent auditServiceCall = new Intent();
        auditServiceCall.setType("text/plain");
        auditServiceCall.putExtra("pin", serverPin);
        auditServiceCall.putExtra("reason", reason);
        auditServiceCall.putExtra("complete", "null");
        ControllerService cs = new ControllerService();
        cs.startService(auditServiceCall);
        while(auditServiceCall.getStringExtra("complete").equalsIgnoreCase("null")){
            // Something witty.
        }
        if(auditServiceCall.getStringExtra("complete").equalsIgnoreCase("false")){
            throw new NotAuthorized();
        }
    }
}
