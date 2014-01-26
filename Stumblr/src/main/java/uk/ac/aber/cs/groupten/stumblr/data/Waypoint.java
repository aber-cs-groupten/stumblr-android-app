package uk.ac.aber.cs.groupten.stumblr.data;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Waypoint extends StumblrData {

    // CONSTRUCTORS
    /**
     * Default constructor for Waypoint.
     */
    public Waypoint() {
        initWaypoint();
    }

    /**
     * Constructor for a Waypoint object from a Parcel.
     */
    public Waypoint(Parcel in) {
        this.readFromParcel(in);
    }

    // INSTANCE VARIABLES

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
     * Location for Waypoint
     */
    private Location location;

    /**
     * Helper method for initialising Waypoint objects.
     */
    private void initWaypoint() {
        timestamp = getCurrentTime();
    }

    /**
     * To be implemented.
     * @return Validity of data (true = valid)
     */
    // TODO
    public boolean isValidData() {
        return false;
    }

    /**
     * Returns current Bitmap.
     * @return The current Bitmap that the Waypoint has,
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * Sets the current Bitmap.
     * @param b The current Bitmap.
     */
    public void setImage(Bitmap b) {
        this.image = b;
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

    /*
     * From: http://stackoverflow.com/a/18167140
     */
    public static final Parcelable.Creator<Waypoint> CREATOR
            = new Parcelable.Creator<Waypoint>() {
        public Waypoint createFromParcel(Parcel in) {
            return new Waypoint(in);
        }

        public Waypoint[] newArray(int size) {
            return new Waypoint[size];
        }
    };
}
