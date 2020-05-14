package ca.payguard;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

import ca.payguard.paymentUtil.CanadaPreAuth;
import ca.payguard.paymentUtil.Transaction;
import ca.payguard.paymentUtil.TransactionHandler;

public class TransactionService extends Service {
    private int NOTIFICATION = 1; // Unique identifier for our notification

    public static boolean isRunning = false;
    public static TransactionService instance = null;
    private Transaction transaction;


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
        transaction = new Transaction();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        isRunning = false;
        instance = null;

        notificationManager.cancel(NOTIFICATION); // Remove notification

        super.onDestroy();
    }

    public void executeTransaction(String orderId, String pan, String exp, String amount){
        TransactionHandler thc = new CanadaPreAuth(); //420
        thc.setOrderId(orderId);
        thc.setExpDate(exp);
        thc.setPan(pan);
        transaction.newTransaction(thc);
        try{
            //TODO: ONLY GARRETT IS ALLOWED TO UNCOMMENT THIS
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                System.out.println("Transaction: " + orderId + " would be executed for " + amount);
            }else{
                transaction.executeTransaction(orderId, amount);
                System.out.println("Transaction: " + orderId + " executed for " + amount);
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void completeTransaction(String orderId, String amount){
        //TODO: ONLY GARRETT IS ALLOWED TO UNCOMMENT THIS
        if(transaction.completeTransaction(orderId, amount)){
            System.out.println("Transaction completed");
        }else{
            System.out.println("Error in transaction");
        }
        System.out.println("Transaction: " + orderId + " completed for " + amount);
    }

}