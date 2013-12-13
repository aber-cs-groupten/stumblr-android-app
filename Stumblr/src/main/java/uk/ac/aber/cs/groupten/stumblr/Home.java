package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public class Home extends AbstractActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            // do stuff
        }
    }

    /**
     * Begin the Route entry activity
     */
    public void startCreateRouteIntent(View v) {
        startActivity(new Intent(getApplicationContext(), CreateRoute.class));
    }

    /**
     * Start Camera activity
     */
    public void startCamera(View v) {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
    }
}
