package uk.ac.aber.cs.groupten.stumblr;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class CreateWaypoint extends AbstractActivity implements LocationListener {
    private LocationManager lm;
    private int gpsUpdateCount;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_create_waypoint);

        if (savedInstanceState == null) {
            // Do stuff
        }

        // Set up location updates (this class implements a Listener)
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // TODO - use Daniel's code to determine best location based upon freshness, etc
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);

        gpsUpdateCount = 0;
    }


    /*
     * ****************************************************************
     *                      Location interaction                      *
     * ****************************************************************
     */

    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     */
    /* Adapted from:
     * https://sites.google.com/site/androidhowto/how-to-1/using-the-gps */
    @Override
    public void onLocationChanged(Location loc) {
        String latString = "Lat: " + String.valueOf(loc.getLatitude());
        String lonString = "Lon: " + String.valueOf(loc.getLongitude());
        Log.v(TAG, latString + " " + lonString);

        ((TextView) findViewById(R.id.latitude)).setText(latString);
        ((TextView) findViewById(R.id.longitude)).setText(lonString);

        gpsUpdateCount++;
        ((TextView) findViewById(R.id.gpsUpdates)).setText(
                "GPS Updates: " + String.valueOf(gpsUpdateCount));

        // TODO - add to waypoint object
    }

    @Override
    public void onProviderDisabled(String s) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}


    /*
     * ****************************************************************
     *                        Camera interaction                      *
     * ****************************************************************
     */

    /**
     * Obtain a photo from user and add it to current Waypoint.
     */
    public void startCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
        }

        // TODO - test return value with ImageView in R.layout.activity_create_waypoint
        // TODO - add Bitmap to waypoint object
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data"); // This may be null - so test for null below

            if (b != null) {
                // Log message containing dimensions of image
                String imgDimensions = "Image captured. Height: " +
                        String.valueOf(b.getHeight()) +
                        " Width: " + String.valueOf(b.getWidth());
                Log.v(TAG, imgDimensions);
            }
        }
    }


    /*
     * ****************************************************************
     *                        Other information                       *
     * ****************************************************************
     */

    /**
     * Set a timestamp on the current Waypoint.
     */
    public void getTimestamp() {
        Date date = new Date();
        Log.v(TAG, date.toString());
    }
}