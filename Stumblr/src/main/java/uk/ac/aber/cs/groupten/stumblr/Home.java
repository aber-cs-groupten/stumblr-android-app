package uk.ac.aber.cs.groupten.stumblr;

import android.os.Bundle;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public abstract class Home extends AbstractActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_abstract);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * Begin the Route entry activity
     */
    public abstract void startCreateRouteIntent(Route r);
}
