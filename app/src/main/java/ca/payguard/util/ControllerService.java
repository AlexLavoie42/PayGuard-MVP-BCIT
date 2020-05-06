package ca.payguard.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ControllerService extends Service {

    private Transaction transaction;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    /**
     * Runs only if this service isn't already running.
     */
    @Override
    public void onCreate(){
        transaction = new Transaction();
        Thread workThread = new Thread(runnable);
        workThread.start();
    }

    /** This is run whenever a startService() call is made. */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //do something
        return START_STICKY;
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     *
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "ControllerService destroyed...", Toast.LENGTH_SHORT).show();
    }

}
