package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class CreateWaypoint extends AbstractActivity {
    private Waypoint waypoint;


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

        // Initialise Waypoint Object.
        waypoint = new Waypoint();

        // Forces keyboard to close
        // See: http://stackoverflow.com/a/2059394
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Called when "Create" button in the UI is clicked.
     * Adds data to the current Waypoint object with text specified in UI.
     */
    public void finishWaypoint(View v){
        String wpTitle = ((TextView)findViewById(R.id.wptitle_box)).getText().toString();
        String wpShortDesc = ((TextView)findViewById(R.id.wpshortdesc_box)).getText().toString();

        waypoint.setTitle(wpTitle);
        waypoint.setShortDesc(wpShortDesc);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("data", waypoint);
        setResult(RESULT_OK, returnIntent);

        finish();
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

        Log.v(TAG, imageEncoded);
        return imageEncoded;
    }
}