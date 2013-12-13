package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
            // do stuff
        }
    }

    /**
     * Begin the Route entry activity
     */
    public void startCreateRouteIntent(View v) {
        startActivity(new Intent(getApplicationContext(), CreateRoute.class));
    }

    /**
     * Start Camera activity
     */
    public void startCamera(View v) {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageView mImageView = new ImageView(getApplicationContext());
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data");
            if (b != null) {
                mImageView.setImageBitmap(b);
            }
        }
    }
}
