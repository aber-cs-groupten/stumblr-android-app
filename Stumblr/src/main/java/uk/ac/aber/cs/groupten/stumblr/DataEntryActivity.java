package uk.ac.aber.cs.groupten.stumblr;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public abstract class DataEntryActivity extends ActionBarActivity {
    /**
     * Load the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public abstract void stumblrOnCreate(Bundle savedInstanceState);
}
