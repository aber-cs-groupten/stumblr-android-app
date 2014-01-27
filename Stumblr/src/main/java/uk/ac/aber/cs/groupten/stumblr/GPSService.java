package uk.ac.aber.cs.groupten.stumblr;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by charlie on 27/01/2014.
 */
public class GPSService extends Service implements LocationListener {
    public final static String INTENT_STRING = "STUMBLR_GPS_SERVICE";
    public final static String LOC_BUNDLE_STRING = "loc";

    // Service ID
    private final int FG_SERVICE_ID = 80085;

    // Location objects
    private LocationManager lm;

    // Broadcast intent
    private Intent intent;

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(INTENT_STRING);
    }

    /**
     *
     * @param intent
     * @param flags
     * @param startID
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        // TODO run as AsyncTask?

        // Set up location updates (this class implements a Listener)
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);

        // Run as foreground task
        // See: http://stackoverflow.com/a/6636893
        Notification notice = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Stumblr is running...")
                .setContentText("Stumblr is running...")
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        notice.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(FG_SERVICE_ID, notice);

        return 0;
    }

    /**
     *
     * @param intent
     * @return
     */
    public IBinder onBind(Intent intent) {
        // TODO
        return null;
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        lm.removeUpdates(this);
        super.onDestroy();
    }

    // Location interaction
    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     * Adapted from: https://sites.google.com/site/androidhowto/how-to-1/using-the-gps
     */
    @Override
    public void onLocationChanged(Location loc) {
        intent.putExtra(LOC_BUNDLE_STRING, loc);
        sendBroadcast(intent);
    }

    @Override
    public void onProviderDisabled(String s) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}
}
