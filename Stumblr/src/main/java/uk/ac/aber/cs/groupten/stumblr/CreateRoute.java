package uk.ac.aber.cs.groupten.stumblr;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public class CreateRoute extends DataEntryActivity {

     private String shortDesc, longDesc, title;

    public CreateRoute() {
        super();

    }

    /* New route method */
    public void newRoute(){

        shortDesc = ((TextView) findViewById(R.id.shortDescriptionBox)).getText().toString();
        longDesc = ((TextView) findViewById(R.id.longDescriptionBox)).getText().toString();
        title = ((TextView) findViewById(R.id.title)).getText().toString();
        Route newRoute = new Route(title, shortDesc, longDesc);
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
