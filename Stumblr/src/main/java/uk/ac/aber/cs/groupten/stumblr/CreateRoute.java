package uk.ac.aber.cs.groupten.stumblr;

import android.os.Bundle;
import android.view.View;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public class CreateRoute extends DataEntryActivity {
    public CreateRoute() {
        super();
    }

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    @Override
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_create_route);
    }

    public void end(View view) {
        finish();
    }

    /**
     * Start the WaypointList activity (list the current Route).
     */
    public void startWaypointListIntent(Route r) {};
}
