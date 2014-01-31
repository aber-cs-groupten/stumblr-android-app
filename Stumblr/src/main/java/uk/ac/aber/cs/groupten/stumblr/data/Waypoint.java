package uk.ac.aber.cs.groupten.stumblr.data;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Waypoint extends StumblrData implements Parcelable {
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
     * Sets timestamp.
     *
     * @param l The timestamp.
     */
    public void setTimestamp(long l) {
        this.timestamp = l;
    }

    /**
     * Returns timestamp.
     *
     * @return The timestamp.
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * Sets the current location.
     *
     * @param l The current location to set.
     */
    public void setLocation(Location l) {
        this.location = l;
        Log.v(TAG, l.toString());
    }


    /**
     * Sets the current location.
     *
     * @param l The current location to set.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Returns current Bitmap.
     *
     * @return The current Bitmap that the Waypoint has,
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * Sets the current Bitmap.
     *
     * @param b The current Bitmap.
     */
    public void setImage(Bitmap b) {
        this.image = b;
    }

    /**
     * Returns a String with the title.
     *
     * @return The title string.
     */
    public String toString() {
        return getTitle();
    }

    /**
     * Writes the Waypoint into a Parcel for moving between Activities.
     *
     * @param parcel The parcel to be written to.
     * @param i      Flags.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timestamp);
        parcel.writeString(this.getTitle());
        parcel.writeString(this.getShortDesc());
        parcel.writeValue(this.image);
        parcel.writeValue(this.location);
    }

    /**
     * Reads Route data from a parcel.
     *
     * @param inParcel
     */
    public void readFromParcel(Parcel inParcel) {
        this.timestamp = inParcel.readLong();
        this.setTitle(inParcel.readString());
        this.setShortDesc(inParcel.readString());
        this.image = (Bitmap) inParcel.readValue(null); // Gets Image
        this.location = (Location) inParcel.readValue(null); // Gets location
    }

    /**
     * From: http://stackoverflow.com/a/18167140
     */
    public static final Parcelable.Creator<Waypoint> CREATOR = new Parcelable.Creator<Waypoint>() {
        public Waypoint createFromParcel(Parcel in) {
            return new Waypoint(in);
        }

        /**
         * @param size pass in size of new array.
         * @return Waypoint type array with passed in size.
         */
        public Waypoint[] newArray(int size) {
            return new Waypoint[size];
        }
    };

   @Override // Unused
   /**
    * Unused method. Required for parcelable implementation.
    */
   public int describeContents() {
        return 0;
    }
}
