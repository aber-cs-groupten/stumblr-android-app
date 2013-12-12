package uk.ac.aber.cs.groupten.stumblr;

import android.os.Bundle;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public abstract class CreateRoute extends DataEntryActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    @Override
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_create_route);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AbstractActivity.PlaceholderFragment())
                    .commit();
        }
    }


    /**
     * Create a new Route object (using the information entered by the user)
     */
    public abstract void createNewRoute();

    /**
     * Start the WaypointList activity (list the current Route).
     */
    public abstract void startWaypointListIntent(Route r);
}
