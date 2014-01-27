package uk.ac.aber.cs.groupten.stumblr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public class CreateRoute extends DataEntryActivity {
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

        // checking the length of the text fields
        if (title.length() > 3) {
            if (shortDesc.length() > 3) {
                // Set parameters of current Route object
                route.setTitle(title);
                route.setShortDesc(shortDesc);
                route.setLongDesc(longDesc);

                // Start the new Activity
                Intent i = new Intent(getApplicationContext(), WaypointList.class);
                i.putExtra("route", route);
                startActivity(i);
            }
            else {
                // insufficient shortDesc length
                Toast.makeText(getBaseContext(), "The short description is to short.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            // insufficient title length
            Toast.makeText(getBaseContext(), "The title is to short.", Toast.LENGTH_LONG).show();
        }


    }
}
