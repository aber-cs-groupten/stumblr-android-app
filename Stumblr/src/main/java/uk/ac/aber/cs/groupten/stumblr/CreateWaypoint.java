package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import uk.ac.aber.cs.groupten.stumblr.data.StumblrData;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class CreateWaypoint extends AbstractActivity {

    /**
     * bundle of waypoint data
     */
    public static final String WAYPOINT_BUNDLE = "waypoint";

    /**
     * Bundle of location data
     */
    public static final String LOCATION_BUNDLE = "loc";

    /**
     * Bundle of return data
     */
    public static final String RETURN_BUNDLE = "return_data";

    /**
     * Waypoint title.
     */
    private String wpTitle;

    /**
     * Short description.
     */
    private String wpShortDesc;

    /**
     * Instance of waypoint.
     */
    private Waypoint waypoint;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     *
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_create_waypoint);

        // Initialise Waypoint Object.
        waypoint = new Waypoint();

        // Unbundle Location from WaypointList
        Bundle b = getIntent().getExtras();

        if (b != null) {
            Log.i(TAG, "Bundle appears to be non-null");

            Location tempLocation = (Location) b.get(LOCATION_BUNDLE);
            if (tempLocation != null) {
                waypoint.setLocation(tempLocation);
            } else {
                Log.i(TAG, "Location is null");
            }

            Waypoint tempWaypoint = (Waypoint) b.get(WAYPOINT_BUNDLE);
            if (tempWaypoint != null) {
                loadWaypoint(tempWaypoint);
            } else {
                Log.i(TAG, "Waypoint is null");
            }
        } else {
            Log.e(TAG, "Bundle is null! Error!");
        }

        // Forces keyboard to close
        // See: http://stackoverflow.com/a/2059394
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Pass in waypoint. Set TextView and ImageView fields to data returned from getting
     * the waypoints title, short description and image
     * @param w passed in waypoint to be loaded.
     */
    public void loadWaypoint(Waypoint w) {
        this.waypoint = w;
        ImageView image = ((ImageView) findViewById(R.id.imageView));
        TextView title = ((TextView) findViewById(R.id.wptitle_box));
        TextView shortDesc = ((TextView) findViewById(R.id.wpshortdesc_box));

        // Set up GUI attributes
        title.setText(waypoint.getTitle());
        shortDesc.setText(waypoint.getShortDesc());

        if (waypoint.getImage() != null) {
            image.setBackgroundResource(0); // Clear image
            image.setImageBitmap(waypoint.getImage());
        }
    }

    /**
     * Gets waypoint title and short description from the user interface.
     */
    public void getTextFromUI() {
        wpTitle = ((TextView) findViewById(R.id.wptitle_box)).getText().toString();
        wpShortDesc = ((TextView) findViewById(R.id.wpshortdesc_box)).getText().toString();
    }

    /**
     * Called when "Create" button in the UI is clicked.
     * Adds data to the current Waypoint object with text specified in UI.
     */
    public void finishWaypoint(View v) {
        getTextFromUI();

        // Sanitise Inputs
        wpTitle = waypoint.sanitiseStringInput(wpTitle);
        wpShortDesc = waypoint.sanitiseStringInput(wpShortDesc);

        // checking the length of the text fields
        if (!StumblrData.isValidData(wpTitle)) {
            Toast.makeText(getBaseContext(), "The Waypoint title is too short.", Toast.LENGTH_LONG).show();
        } else if (!StumblrData.isValidData(wpShortDesc)) {
            Toast.makeText(getBaseContext(), "The short description is too short.", Toast.LENGTH_LONG).show();
        } else {
            waypoint.setTitle(wpTitle);
            waypoint.setShortDesc(wpShortDesc);

            //set time stamp
            Calendar c = Calendar.getInstance();
            waypoint.setTimestamp(c.getTimeInMillis());

            Intent returnIntent = new Intent();
            returnIntent.putExtra(RETURN_BUNDLE, waypoint);
            setResult(RESULT_OK, returnIntent);

            // Graceful finish
            finish();
        }
    }

    /*
     * ****************************************************************
     *                        Camera interaction                      *
     * ****************************************************************
     */
    /**
     * Obtain a photo from user and add it to current Waypoint.
     *
     * @param v The View object.
     */
    public void startCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
        }
    }

    /**
     * on Creation of a waypoint result, if the result is OK then getExtras from intent data.
     * Then get bitmap image for waypoint image and check for null prior to setting the image to the
     * waypoint. Update UI imageView, and log dimensions of image.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQ_CODE) {
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data"); // This may be null - so test for null below

            if (b != null) {
                // Set the Waypoint image.
                waypoint.setImage(b);

                // Update the ImageView in the UI
                findViewById(R.id.imageView).setBackgroundResource(0);
                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(b);

                // Log message containing dimensions of image
                String imgDimensions = "Image captured. Height: " +
                        String.valueOf(b.getHeight()) +
                        " Width: " + String.valueOf(b.getWidth());

                Log.v(TAG, imgDimensions);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        getTextFromUI();

        Log.i(TAG, "CreateWaypoint: onSaveInstanceState");

        savedInstanceState.putString("wpTitle", wpTitle);
        savedInstanceState.putString("wpShortDesc", wpShortDesc);
        savedInstanceState.putParcelable("waypoint", waypoint);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Restore state to savedInstanceState. Set waypoint title, short description and waypoint
     * to appropriate data retrieved from the savedInstanceState. Then update TextViews with
     * newly set title and short description.
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG, "CreateWaypoint: onRestoreInstanceState");

        wpTitle = savedInstanceState.getString("wpTitle");
        wpShortDesc = savedInstanceState.getString("wpShortDesc");
        waypoint = savedInstanceState.getParcelable("waypoint");

        ((TextView) findViewById(R.id.wptitle_box)).setText(wpTitle);
        ((TextView) findViewById(R.id.wpshortdesc_box)).setText(wpShortDesc);
    }
}