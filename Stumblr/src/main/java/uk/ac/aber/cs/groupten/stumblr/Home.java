package uk.ac.aber.cs.groupten.stumblr;

import android.os.Bundle;

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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * Begin the Route entry activity
     */
    public void startCreateRouteIntent(Route r) {};
}
