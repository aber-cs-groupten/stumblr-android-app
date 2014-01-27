package uk.ac.aber.cs.groupten.stumblr;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by charlie on 27/01/2014.
 */
public class GPSService extends Service implements LocationListener {
    public static String intent_string = "STUMBLR_GPS_SERVICE";
    public static String location_bundle_string = "loc";

    // 
    private final int foreground_id = 80085;

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
        intent = new Intent(intent_string);
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
        Intent i = new Intent(this, SomeActivityToLaunch.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, i, 0);

        //This constructor is deprecated. Use Notification.Builder instead
        Notification notice = new Notification(R.drawable.icon_image, "Ticker text", System.currentTimeMillis());

        //This method is deprecated. Use Notification.Builder instead.
        notice.setLatestEventInfo(this, "Title text", "Content text", pendIntent);

        notice.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(foreground_id, notice);

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
        intent.putExtra(location_bundle_string, loc);
        sendBroadcast(intent);
    }

    @Override
    public void onProviderDisabled(String s) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}
}
