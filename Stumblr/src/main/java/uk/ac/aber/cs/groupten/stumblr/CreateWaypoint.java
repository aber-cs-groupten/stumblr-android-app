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
    public static final String WAYPOINT_BUNDLE = "waypoint";
    public static final String LOCATION_BUNDLE = "loc";
    public static final String RETURN_BUNDLE = "return_data";

    private Waypoint waypoint;

    /**
     * Loads the activity on creation (using a bundle if one is present)
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
            Log.e(TAG, "Bundle appears to be non-null");

            Location tempLocation = (Location) b.get(LOCATION_BUNDLE);
            if (tempLocation != null) {
                waypoint.setLocation(tempLocation);
            } else {
                Log.e(TAG, "Location passed into CreateWaypoint is null! Trying Waypoint...");
            }

            Waypoint tempWaypoint = (Waypoint) b.get(WAYPOINT_BUNDLE);
            if (tempWaypoint != null) {
                loadWaypoint(tempWaypoint);
            } else {
                Log.e(TAG, "Waypoint is also null, something went wrong here.");
            }
        } else {
            Log.e(TAG, "Bundle is null!");
        }

        // Forces keyboard to close
        // See: http://stackoverflow.com/a/2059394
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void loadWaypoint(Waypoint w) {
        // TODO
        this.waypoint = w;
        ImageView image = ((ImageView) findViewById(R.id.imageView));
        TextView title = ((TextView) findViewById(R.id.wptitle_box));
        TextView shortDesc = ((TextView) findViewById(R.id.wpshortdesc_box));

        // Set up GUI attributes
        title.setText(waypoint.getTitle());
        shortDesc.setText(waypoint.getShortDesc());
        image.setImageBitmap(waypoint.getImage());
    }

    /**
     * Called when "Create" button in the UI is clicked.
     * Adds data to the current Waypoint object with text specified in UI.
     */
    public void finishWaypoint(View v){
        String wpTitle = ((TextView) findViewById(R.id.wptitle_box)).getText().toString();
        String wpShortDesc = ((TextView) findViewById(R.id.wpshortdesc_box)).getText().toString();

        // Sanitise Inputs
        wpTitle = waypoint.sanitiseStringInput(wpTitle);
        wpShortDesc = waypoint.sanitiseStringInput(wpShortDesc);

        // checking the length of the text fields
        if (StumblrData.isValidData(wpTitle) && StumblrData.isValidData(wpShortDesc)) {
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
        } else {
            //TODO Fix toast printing when short description is too short instead of title
            // insufficient title length
            Toast.makeText(getBaseContext(), "The Waypoint title is too short.", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * ****************************************************************
     *                        Camera interaction                      *
     * ****************************************************************
     */
    /**
     * Obtain a photo from user and add it to current Waypoint.
     * @param v The View object.
     */
    public void startCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
        }
    }

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

    // http://stackoverflow.com/questions/15430787/android-go-back-to-previous-activity
    @Override
    public void onBackPressed () {
        Intent i = new Intent(CreateWaypoint.this, WaypointList.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear back stack
        startActivity(i);
        finish();
    }
}