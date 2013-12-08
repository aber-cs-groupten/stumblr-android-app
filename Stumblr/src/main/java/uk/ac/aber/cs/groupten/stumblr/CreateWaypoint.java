package uk.ac.aber.cs.groupten.stumblr;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import uk.ac.aber.cs.group10.stumblr.data.Route;
import uk.ac.aber.cs.group10.stumblr.data.Waypoint;

public abstract class CreateWaypoint extends ActionBarActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_abstract);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AbstractActivity.PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * Create a new Waypoint based on user input.
     */
    public abstract void createWaypoint();

    /**
     * Obtain a photo from user and add it to current Waypoint.
     */
    public abstract void getImage(Waypoint w);

    /**
     * Set a timestamp on the current Waypoint.
     */
    public abstract void getTimestamp();

    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     */
}
