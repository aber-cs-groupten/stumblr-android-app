package uk.ac.aber.cs.groupten.stumblr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class CreateWaypoint extends AbstractActivity implements LocationListener {
    private Waypoint waypoint;
    private LocationManager lm;

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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);

        // Initialise Waypoint Object.
        waypoint = new Waypoint();
    }

    /**
     * Called when "Create" button in the UI is clicked.
     * Initialises a new Waypoint object with text specified in UI.
     */
    public void createWaypoint(){
        String wpTitle = ((TextView)findViewById(R.id.wpTitle)).getText().toString();
        String wpShortDesc = ((TextView)findViewById(R.id.wpshortdesc_box)).getText().toString();

        waypoint.setTitle(wpTitle);
        waypoint.setShortDesc(wpShortDesc);
    }

    /*
     * ****************************************************************
     *                      Location interaction                      *
     * ****************************************************************
     */
    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     */
    // Adapted from: https://sites.google.com/site/androidhowto/how-to-1/using-the-gps
    @Override
    public void onLocationChanged(Location loc) {
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
     *                         Base64 Encoding                        *
     * ****************************************************************
     */

    // TODO @Martin - find source for this information and reference properly
    public void startBase64Intent(View v) {
        //  startActivity(new Intent())
    }

    public String encodeTobase64(Bitmap image)
    {
        Bitmap imagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e(TAG, imageEncoded);
        return imageEncoded;
    }
}