package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class AbstractActivity extends ActionBarActivity {

    public static final String TAG = "STUMBLR";
    public static final int CAMERA_REQ_CODE = 1337;

    /**
     * @param savedInstanceState saved state of the application.
     * onCreate set state to saved previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stumblrOnCreate(savedInstanceState);
    }

    /**
     * @param savedInstanceState
     * Called by super class
     */
    public abstract void stumblrOnCreate(Bundle savedInstanceState);

    /**
     * @param menu inflate the menu; this adds items to the action bar if it is present.
     * @return true once menu has inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will automatically handle
     * clicks on the Home/Up button, so long as you specify a parent activity in
     * AndroidManifest.xml
     * @param item passed in MenuItem when it is selected
     * @return selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel) {
            finish();
            startActivity(new Intent(this, Home.class));
        }

        return ((id == R.id.action_cancel) || super.onOptionsItemSelected(item));
    }

    /**
     * When back button is pressed, call finish. Drop activity from memory.
     */
    @Override
    public void onBackPressed() {
        finish();
    }
}
