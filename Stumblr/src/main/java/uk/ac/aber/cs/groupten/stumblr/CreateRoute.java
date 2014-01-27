package uk.ac.aber.cs.groupten.stumblr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public class CreateRoute extends DataEntryActivity {
    private BroadcastReceiver receiver;
    private Route route;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    @Override
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // TODO stuff with savedinstancestate
        // Called by super().onCreate
        setContentView(R.layout.activity_create_route);

        // Create new blank Route object
        route = new Route();

        /*
         * TODO test GPS service code
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction("Stumblr is recording...");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                b.get("lat");
                b.get("lng");
                // todo get coordinates from bundle, add to "route"
            }
        };
        registerReceiver(receiver, filter);
    }

    /**
     * Start the WaypointList activity (list the current Route).
     * Called when the "next" button is clicked in the UI.
     */
    public void startWaypointListIntent(View v) {
        // Get text from fields in UI
        String title = ((TextView) findViewById(R.id.routeTitleBox)).getText().toString();
        String shortDesc = ((TextView) findViewById(R.id.shortDescriptionBox)).getText().toString();
        String longDesc = ((TextView) findViewById(R.id.longDescriptionBox)).getText().toString();

        // Set parameters of current Route object
        route.setTitle(title);
        route.setShortDesc(shortDesc);
        route.setLongDesc(longDesc);

        // Start the new Activity
        Intent i = new Intent(getApplicationContext(), WaypointList.class);
        i.putExtra("route", route);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
