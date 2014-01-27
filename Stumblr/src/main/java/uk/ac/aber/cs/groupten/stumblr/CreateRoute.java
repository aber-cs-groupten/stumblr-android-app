package uk.ac.aber.cs.groupten.stumblr;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public class CreateRoute extends DataEntryActivity implements LocationListener {
    private LocationManager lm; // TODO this could be a local variable
    private Route route;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    @Override
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_create_route);

        // Set up location updates (this class implements a Listener)
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);

        // Create new blank Route object
        route = new Route();
    }

    /**
     * Start the WaypointList activity (list the current Route).
     * Called when the "next" button is clicked in the UI.
     */
    public void startWaypointListIntent(View v) {
        // Get text from fields in UI
        String title = ((TextView) findViewById(R.id.routeTitle)).getText().toString();
        String shortDesc = ((TextView) findViewById(R.id.shortDescriptionBox)).getText().toString();
        String longDesc = ((TextView) findViewById(R.id.longDescriptionBox)).getText().toString();

        // Set parameters of current Route object
        route.setTitle(title);
        route.setShortDesc(shortDesc);
        route.setLongDesc(longDesc);

        // Start the new Activity
        Intent i = new Intent(getApplicationContext(), WaypointList.class);
        i.putExtra("route", route);
        startActivity(i);
    }

    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     * Adapted from: https://sites.google.com/site/androidhowto/how-to-1/using-the-gps
     */
    @Override
    public void onLocationChanged(Location loc) {
        Log.v(TAG, "Location updated.");
        route.addCoordinate(loc);
    }

    @Override
    public void onProviderDisabled(String s) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}
}
