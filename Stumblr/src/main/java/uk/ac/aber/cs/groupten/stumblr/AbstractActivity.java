package uk.ac.aber.cs.groupten.stumblr;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public abstract class AbstractActivity extends ActionBarActivity {
    public static final String TAG = "STUMBLR";
    public static final int CAMERA_REQ_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stumblrOnCreate(savedInstanceState);
    }

    public abstract void stumblrOnCreate(Bundle savedInstanceState);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return ((id == R.id.action_settings) || super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        // Do nothing!
        Log.v(TAG, "Back button pressed... Ignoring!");
    }
}
