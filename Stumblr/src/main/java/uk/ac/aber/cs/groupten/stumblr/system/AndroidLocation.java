package uk.ac.aber.cs.groupten.stumblr.system;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import uk.ac.aber.cs.groupten.stumblr.AbstractActivity;

import java.util.List;

public class AndroidLocation {
    protected static String SINGLE_LOCATION_UPDATE_ACTION;
    protected Context context;
    protected Criteria criteria;
    protected PendingIntent singleUpdatePI;
    protected LocationListener locationListener;
    protected LocationManager locationManager;

    /**
     * Constructor
     * @param context Context of the current Activity
     */
    public AndroidLocation(Context context) {
        this.context = context;

        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Coarse accuracy is specified here to get the fastest possible result.
        // The calling Activity will likely (or have already) request ongoing
        // updates using the Fine location provider.
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);

        // Construct the Pending Intent that will be broadcast by the oneshot
        // location update.
        Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
        singleUpdatePI = PendingIntent.getBroadcast(context, 0,
                updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * This {@link BroadcastReceiver} listens for a single location
     * update before unregistering itself.
     * The one-shot location update is returned via the {@link LocationListener}.
     */
    protected BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(singleUpdateReceiver);

            String key = LocationManager.KEY_LOCATION_CHANGED;
            Location location = (Location) intent.getExtras().get(key);

            if (locationListener != null && location != null)
                locationListener.onLocationChanged(location);

            locationManager.removeUpdates(singleUpdatePI);
        }
    };

    /*
     * Modified from Google Code -> android_protips_location -> GingerbreadLastLocation.java
     * URL: http://goo.gl/UqhWKD
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public Location getLastBestLocation(int minDistance, long minTime) {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // Iterate through all the providers on the system, keeping
        // note of the most accurate result within the acceptable time limit.
        // If no result is found within maxTime, return the newest Location.
        for (String provider : locationManager.getAllProviders()) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        // If the best result is beyond the allowed time limit, or the accuracy of the
        // best result is wider than the acceptable maximum distance, request a single update.
        // This check simply implements the same conditions we set when requesting regular
        // location updates every [minTime] and [minDistance].
        if (locationListener != null && (bestTime < minTime || bestAccuracy > minDistance)) {
            IntentFilter locIntentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
            context.registerReceiver(singleUpdateReceiver, locIntentFilter);
            locationManager.requestSingleUpdate(criteria, singleUpdatePI);
        }

        return bestResult;
    }

    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     */
    // Define a listener that responds to location updates
    //LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        //     makeUseOfNewLocation(location);
    }
}