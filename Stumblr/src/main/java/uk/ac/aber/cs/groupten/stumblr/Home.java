package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AbstractActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     *
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_home);
    }

    /**
     * Begin the Route entry activity
     */
    public void startCreateRouteIntent(View v) {
        Intent i = new Intent(getApplicationContext(), CreateRoute.class);
        startActivity(i);
    }

    /**
     * When back is pressed, finish activity.
     * http://stackoverflow.com/questions/15430787/android-go-back-to-previous-activity
     */
    @Override
    public void onBackPressed() {
        finish();
    }
}
