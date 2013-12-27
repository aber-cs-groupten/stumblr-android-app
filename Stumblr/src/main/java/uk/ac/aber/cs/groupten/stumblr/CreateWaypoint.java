package uk.ac.aber.cs.groupten.stumblr;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.util.List;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;
import uk.ac.aber.cs.groupten.stumblr.system.AndroidLocation;

public class CreateWaypoint extends AbstractActivity {

    protected Context context;
    protected Criteria criteria;
    protected PendingIntent singleUpdatePI;
    protected LocationListener locationListener;
    protected LocationManager locationManager;
    protected static String SINGLE_LOCATION_UPDATE_ACTION;

    public static final int CAMERA_REQ_CODE = 1337;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_abstract);

        if (savedInstanceState == null) {
            // Do stuff
        }

        getImage();
        getLocation();
    }

    /**
     * Obtain a photo from user and add it to current Waypoint.
     */
    public void getImage(){
        startCamera();
    }

    /**
     * Set a timestamp on the current Waypoint.
     */
    public void getTimestamp(){

    }

    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     */
    public void getLocation() {
        AndroidLocation al = new AndroidLocation(this);
        Location loc = al.getLastBestLocation(0, 0);

        StringBuilder sb = new StringBuilder();
        sb.append("Lat: "); sb.append(loc.getLatitude());
        sb.append(" Lon: "); sb.append(loc.getLongitude());
        Log.v(TAG, sb.toString());
    }

    /*
     * Camera stuff
     */
    public void startCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data");

            if (b != null) {
                // Log a message containing dimensions of image
                StringBuilder sb = new StringBuilder();
                sb.append("Image captured. Height: "); sb.append(b.getHeight());
                sb.append(" Width: "); sb.append(b.getWidth());
                Log.v(TAG, sb.toString());
            }
        }
    }
};




