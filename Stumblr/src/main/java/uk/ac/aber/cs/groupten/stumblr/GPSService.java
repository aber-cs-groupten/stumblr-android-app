package uk.ac.aber.cs.groupten.stumblr;

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
        intent = new Intent("Stumblr is recording...");
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
        // TODO run as AsyncTask

        // Set up location updates (this class implements a Listener)
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);

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
        intent.putExtra("loc", loc);
        sendBroadcast(intent);
    }

    @Override
    public void onProviderDisabled(String s) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}
}
