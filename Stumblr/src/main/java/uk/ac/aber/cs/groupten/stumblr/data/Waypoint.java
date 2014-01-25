package uk.ac.aber.cs.groupten.stumblr.data;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Waypoint extends StumblrData {
    /* title and shortDesc are declared in StumblrData and are accessed through get/set methods */

    /**
     * Arrival timestamp for Waypoint.
     */
    private long timestamp;

    /**
     * Image contained within Waypoint.
     */
    private Bitmap image;

    /**
     * Constructor for a Waypoint object.
     * @param title Title of the waypoint.
     * @param shortDesc A short description.
     */
    public Waypoint(String title, String shortDesc) {
        /* Calls superclass constructor */
        super(title, shortDesc);
        timestamp = getCurrentTime();
    }

    /**
     * To be implemented.
     * @return Validity of data (true = valid)
     */
    public boolean isValidData() {
        return false;
    }

    /**
     * Sets the current image.
     * @param image The image to add to the Waypoint.
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Returns current image.
     * @return The current image that the Waypoint has,
     */
    public Bitmap getImage() {
        return this.image;
    }

    // TODO
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the Waypoint into a Parcel for moving between Activities.
     * @param parcel The parcel to be written to.
     * @param i Flags.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timestamp);
        parcel.writeString(this.getTitle());
        parcel.writeString(this.getShortDesc());
        parcel.writeValue(this.image);
    }

    /**
     * Reads Route data from a parcel.
     * @param inParcel
     */
    public void readFromParcel(Parcel inParcel) {
        this.timestamp = inParcel.readLong();
        this.setTitle(inParcel.readString());
        this.setShortDesc(inParcel.readString());
        this.image = (Bitmap) inParcel.readValue(null); // TODO test this. Not sure.

    }
}
