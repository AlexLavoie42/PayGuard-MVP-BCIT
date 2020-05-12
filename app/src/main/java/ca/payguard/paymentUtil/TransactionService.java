package ca.payguard.paymentUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TransactionService extends Service {

    private static final String TAG = "TransactionService";

    private IBinder mBinder = new MyBinder();
    private Handler mHandler;
    private Boolean mIsPaused;
    private Transaction transaction;

    @Override
    public void onCreate(){
        super.onCreate();
        mHandler = new Handler();
        //TODO: init all variables.
        transaction = new Transaction();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyBinder extends Binder {
        public TransactionService getService(){
            return TransactionService.this;
        }
    }

    public void startTask(){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mIsPaused){
                    Log.d(TAG, "run: removing callbacks.");
                    mHandler.removeCallbacks(this);
                    pauseTask();
                }else{
                    Log.d(TAG, "run: running");
                    // Do stuff here

                    mHandler.postDelayed(this, 100); // No point having this loop immediatly
                }
            }
        };
        mHandler.post(runnable);
    }

    public void pauseTask(){
        mIsPaused = true;
    }

    public void restartTask(){
        mIsPaused = false;
        startTask();
    }

    public Boolean getIsPaused(){
        return mIsPaused;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf(); //Hard stop for when app is swiped off.
    }
}
