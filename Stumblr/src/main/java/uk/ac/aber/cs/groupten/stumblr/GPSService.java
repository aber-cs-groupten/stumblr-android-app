package uk.ac.aber.cs.groupten.stumblr;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class GPSService extends Service implements LocationListener {

    /**
     * String name for GPS_INTENT.
     */
    public final static String GPS_INTENT = "STUMBLR_GPS";

    /**
     * String name for GPS_DIALOG.
     */
    public final static String GPS_DIALOG = "STUMBLR_GPS_DIALOG";

    /**
     * String name for bundle of location strings.
     */
    public final static String LOC_BUNDLE_STRING = "loc";

    /**
     * Service ID.
     */
    private static final int FG_SERVICE_ID = 101;

    /**
     * Instance of Intent.
     */
    private Intent intent;

    /**
     * Instance of LocationManager.
     */
    private LocationManager lm;

    /**
     * Instance of Notification.
     */
    private Notification notice;

    /**
     * onCreate start new intent for GPS_INTENT
     */
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(GPS_INTENT);
    }

    /**
     * @param intent  The intent that the Service was started from
     * @param flags   Startup flags
     * @param startID Service ID
     * @return The service status (Sticky, non-sticky, etc)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        // Set up location updates (this class implements a Listener)
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);

        // Run as foreground task
        // See: http://stackoverflow.com/a/16919410
        Intent i = new Intent(getApplicationContext(), WaypointList.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        // See: http://stackoverflow.com/a/6636893
        notice = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Stumblr is recording walk...")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pi)
                .build();

        // Be nice to older versions of Android...
        // See: http://stackoverflow.com/questions/11947928/startforeground-bad-notification-error
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, GPSService.class), 0);

            // Set the info for the views that show in the notification panel.
            notice.setLatestEventInfo(this, "Stumblr is recording walk...",
                    "Tap to resume", contentIntent);
        }


        notice.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(FG_SERVICE_ID, notice);

        return Service.START_NOT_STICKY;
    }

    /**
     * onDestroy remove LocationManager updates and stop foreground task.
     */
    @Override
    public void onDestroy() {
        lm.removeUpdates(this); // Remove locationManager updates
        stopForeground(true); // Stop foreground task
        super.onDestroy();
    }

    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     * Adapted from: https://sites.google.com/site/androidhowto/how-to-1/using-the-gps
     */
    @Override
    public void onLocationChanged(Location loc) {
        intent.putExtra(LOC_BUNDLE_STRING, loc);
        sendBroadcast(intent);
    }

    /**
     * Prompt for GPS, and error handling
     * See: http://hedgehogjim.wordpress.com/2013/03/20/programmatically-enable-android-location-services/
     */
    @Override
    public void onProviderDisabled(String s) {
        // Tell WaypointList to show a dialog
        Intent i = new Intent(GPS_DIALOG);
        sendBroadcast(i);
    }

    /**
     * Empty method to abide by implementation requirements.
     * @param s
     */
    @Override
    public void onProviderEnabled(String s) {
    }

    /**
     * Empty method to abide by implementation requirements.
     * @param s
     * @param i
     * @param b
     */
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {
    }

    /**
     * Empty method to abide by implementation requirements.
     * @param intent
     * @return
     */
    public IBinder onBind(Intent intent) { // Unused
      return null;
    }
}
