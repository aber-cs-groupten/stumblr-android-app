package uk.ac.aber.cs.groupten.stumblr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.Calendar;

import uk.ac.aber.cs.groupten.stumblr.data.StumblrData;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class CreateWaypoint extends AbstractActivity {

    /**
     * Request code for camera intent
     */
    public static final int CAMERA_REQ_CODE = 1337;

    /**
     * Request code for gallery intent
     */
    public static final int GALLERY_REQ_CODE = 7007;

    /**
     * Bundle string for identifying Waypoint data
     */
    public static final String WAYPOINT_BUNDLE = "waypoint";

    /**
     * Bundle string for identifying location data
     */
    public static final String LOCATION_BUNDLE = "loc";

    /**
     * Bundle string for identifying return data
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

        // Check if camera and gallery are enabled/disabled
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean cam = (cameraIntent.resolveActivity(getPackageManager()) != null);

        final Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        boolean gal = (galleryIntent.resolveActivity(getPackageManager()) != null);

        if ((!cam) && (! gal)) {
            findViewById(R.id.imageView).setVisibility(View.GONE); // Disable
            Toast.makeText(this, "No camera or gallery found on device...", Toast.LENGTH_LONG).show();
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

        // Image from: http://colouringbook.org/art/svg/coloring-book/fotocamera-foto-camera-icon-
        // coloring-book-colouring-sheet-coloring-book-colouring-page-youtube-facebook-
        // linkedin-twitter-google-pinterest/
        // (long link - sorry). Image is Creative Commons licenced.
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

            // Set timestamp
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
     */
    public void startCamera(Intent cameraIntent) {
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
        }
    }

    // http://stackoverflow.com/a/10168114
    public void startGallery(Intent galleryIntent) {
        galleryIntent.setType("image/*");

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
        }
    }

    public void getImage(View v) {
        boolean cam;
        boolean gal;

        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cam = (cameraIntent.resolveActivity(getPackageManager()) != null);

        final Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gal = (galleryIntent.resolveActivity(getPackageManager()) != null);

        if (cam && gal) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_input_get)
                    .setTitle("Add Image")
                    .setMessage("Would you like to get the image from your gallery or camera?")
                    .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGallery(galleryIntent);
                        }
                    })
                    .setNeutralButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startCamera(cameraIntent);
                        }
                    })
                    .show();
        } else if (cam) {
            startCamera(cameraIntent);
        } else if (gal) {
            startGallery(galleryIntent);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_input_get)
                    .setTitle("Could not find gallery or camera!")
                    .setMessage("We can't get an image because we couldn't find a gallery or camera!")
                    .setNeutralButton("OK", null)
                    .show();
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

        if ((resultCode == RESULT_OK)) {
            Bundle extras = data.getExtras();

            if (requestCode == CAMERA_REQ_CODE) {
                Bitmap b = (Bitmap) extras.get("data");
                setImage(b);

            // http://stackoverflow.com/a/5086706
            } else if (requestCode == GALLERY_REQ_CODE) {
                Uri imgUri = data.getData();

                if (imgUri != null) {
                    // http://stackoverflow.com/a/4717740
                    try {
                        double default_restrict = 160.0d;

                        Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);

                        int newHeight = (int) default_restrict;

                        double scaledWidth = ((default_restrict /
                                                (double) b.getHeight()) *
                                                (double) b.getWidth());

                        int newWidth = (int) Math.floor(scaledWidth); // Convert to int

                        Log.e(TAG, String.valueOf(newWidth));
                        Log.e(TAG, String.valueOf(newHeight));

                        b = Bitmap.createScaledBitmap(b, newWidth, newHeight, false);

                        setImage(b);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, fnfe.getMessage());
                    } catch (IOException ioe) {
                        Log.e(TAG, ioe.getMessage());
                    }
                }
            }
        }
    }

    public void setImage(Bitmap b) {
        // Set the Waypoint image.
        waypoint.setImage(b);

        // Update the ImageView in the UI
        findViewById(R.id.imageView).setBackgroundResource(0);
        ((ImageView) findViewById(R.id.imageView)).setImageBitmap(b);
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