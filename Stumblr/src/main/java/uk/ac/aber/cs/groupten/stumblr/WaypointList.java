package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

/**
 * http://developer.android.com/guide/topics/ui/layout/listview.html
 * For more information on creating a ListView
 */
public abstract class WaypointList extends AbstractActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_abstract);

        if (savedInstanceState == null) {
            // do stuff
        }
    }

    /**
     * Renders Waypoint list on screen.
     * @param r The Route object containing Waypoints to render.
     */
    public abstract void drawWaypointList(Route r);

    /**
     *
     * @param v
     */
    public void startCreateWaypointIntent(View v) {
        startActivity(new Intent(getApplicationContext(), CreateWaypoint.class));
    }
}
