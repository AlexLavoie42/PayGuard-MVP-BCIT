package ca.payguard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import ca.payguard.paymentUtil.CanadaPreAuth;
import ca.payguard.paymentUtil.Transaction;
import ca.payguard.paymentUtil.TransactionHandler;

public class TransactionService extends Service {
    private int NOTIFICATION = 1; // Unique identifier for our notification

    public static boolean isRunning = false;
    public static TransactionService instance = null;
    private Transaction transaction = new Transaction();


    private NotificationManager notificationManager = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        instance = this;
        isRunning = true;

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)        // the status icon
                .setTicker("Service running...")           // the status text
                .setWhen(System.currentTimeMillis())       // the time stamp
                .setContentTitle("My App")                 // the label of the entry
                .setContentText("Service running...")      // the content of the entry
                .setContentIntent(contentIntent)           // the intent to send when the entry is clicked
                .setOngoing(true)                          // make persistent (disable swipe-away)
                .build();

        // Start service in foreground mode
        startForeground(NOTIFICATION, notification);

        return START_STICKY;
    }


    @Override
    public void onDestroy(){
        isRunning = false;
        instance = null;

        notificationManager.cancel(NOTIFICATION); // Remove notification

        super.onDestroy();
    }


    public void executeTransaction(String id, String pan, String exp, String amount){
        TransactionHandler thc = new CanadaPreAuth(); //420
        thc.setOrderId(id);
        thc.setExpDate(exp);
        thc.setPan(pan);
        transaction.newTransaction(thc);
        try{
            //TODO: ONLY GARRETT IS ALLOWED TO UNCOMMENT THIS
//            transaction.executeTransaction(id, amount);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void completeTransaction(String id, String amount){
        //TODO: ONLY GARRETT IS ALLOWED TO UNCOMMENT THIS
//        if(transaction.completeTransaction(id, amount)){
//            System.out.println("Transaction completed");
//        }else{
//            System.out.println("Error in transaction");
//        }
    }

}