package uk.ac.aber.cs.groupten.stumblr;

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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.List;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class CreateWaypoint extends ActionBarActivity {

    protected Context context;
    protected Criteria criteria;
    protected PendingIntent singleUpatePI;
    protected LocationListener locationListener;
    protected LocationManager locationManager;
    protected static String SINGLE_LOCATION_UPDATE_ACTION;

    public CreateWaypoint() {

    }



    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */


    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_create_waypoint);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AbstractActivity.PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * Create a new Waypoint based on user input.
     */
    public  void createWaypoint(String title, String shortDesc, int index, Context context){
        Waypoint wp = new Waypoint(title, shortDesc, index);

            this.context = context;
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            // Coarse accuracy is specified here to get the fastest possible result.
            // The calling Activity will likely (or have already) request ongoing
            // updates using the Fine location provider.
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_LOW);

            // Construct the Pending Intent that will be broadcast by the oneshot
            // location update.
            Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
            singleUpatePI = PendingIntent.getBroadcast(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        wp.addCoordinate(getLastBestLocation(0,0));
        }


    /**
     * This {@link BroadcastReceiver} listens for a single location
     * update before unregistering itself.
     * The oneshot location update is returned via the {@link LocationListener}
     * specified in {@link setChangedLocationListener}.
     */
    protected BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(singleUpdateReceiver);

            String key = LocationManager.KEY_LOCATION_CHANGED;
            Location location = (Location)intent.getExtras().get(key);

            if (locationListener != null && location != null)
                locationListener.onLocationChanged(location);

            locationManager.removeUpdates(singleUpatePI);
        }
    };

  /* REFERENCE - GingerbreadLastLocation
   * https://code.google.com/p/android-protips-location/source/browse/trunk/src/com/radioactiveyak/location_best_practices/utils/GingerbreadLastLocationFinder.java
   */
  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  public Location getLastBestLocation(int minDistance, long minTime) {
      Location bestResult = null;
      float bestAccuracy = Float.MAX_VALUE;
      long bestTime = Long.MIN_VALUE;

      // Iterate through all the providers on the system, keeping
      // note of the most accurate result within the acceptable time limit.
      // If no result is found within maxTime, return the newest Location.
      List<String> matchingProviders = locationManager.getAllProviders();
      for (String provider: matchingProviders) {
          Location location = locationManager.getLastKnownLocation(provider);
          if (location != null) {
              float accuracy = location.getAccuracy();
              long time = location.getTime();

              if ((time > minTime && accuracy < bestAccuracy)) {
                  bestResult = location;
                  bestAccuracy = accuracy;
                  bestTime = time;
              }
              else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
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
          locationManager.requestSingleUpdate(criteria, singleUpatePI);
      }

      return bestResult;
  }



    /**
     * Obtain a photo from user and add it to current Waypoint.
     */
    public void getImage(Waypoint w){

    }

    /**
     * Set a timestamp on the current Waypoint.
     */
    public void getTimestamp(){

    }

    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     */

    // Acquire a reference to the system Location Manager
    //LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    // Define a listener that responds to location updates
    //LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
       //     makeUseOfNewLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };




