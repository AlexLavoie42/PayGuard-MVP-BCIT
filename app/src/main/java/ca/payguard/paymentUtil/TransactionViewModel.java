package ca.payguard.paymentUtil;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TransactionViewModel {
    private static final String TAG = "TransactionViewModel";

    private MutableLiveData<Boolean> mIsProgressUpdating = new MutableLiveData<>();
    private MutableLiveData<TransactionService.MyBinder> mBinder = new MutableLiveData<>();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: connected to service");
            TransactionService.MyBinder binder = (TransactionService.MyBinder) service;
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder.postValue(null);
        }
    };

    public LiveData<Boolean> getIsProgressUpdating(){
        return mIsProgressUpdating;
    }

    public LiveData<TransactionService.MyBinder> getBinder(){
        return mBinder;
    }

    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }

    public void setIsUpdating(Boolean isUpdating){
        mIsProgressUpdating.postValue(isUpdating);
    }
}
