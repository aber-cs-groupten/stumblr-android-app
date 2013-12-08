package uk.ac.aber.cs.group10.stumblr.data;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;
import java.util.LinkedList;

/*
     FAIL WHALE

              '.
  .-""-._     \ \.--|
 /       "-..__) .-'
|     _         /
\'-.__,   .__.,'
 `'----'._\--'

 Source: (http://strawp.net/files/fail_whale.txt)
 */

/**
 * Created by charles on 29/11/13.
 */
public class Waypoint extends StumblrData {
    /* title and shortDesc are declared in StumblrData and are accessed through get/set methods */

    /**
     * Image contained within Waypoint.
     */
    private Bitmap image;

    /**
     * Time at which Waypoint was created.
     */
    private Date timestamp;

    /**
     * List of coordinates (from the last location to the current location)
     */
    private LinkedList<Location> coordList;

    private int index;

    /**
     * Constructor for a Waypoint object.
     * @param title Title of the waypoint.
     * @param shortDesc A short description.
     */
    public Waypoint(String title, String shortDesc, int index) {
        /* Calls superclass constructor */
        super(title, shortDesc);

        /* Sets index variable (used for locating position in array */
        this.index = index;

        /* Uses Android system to get time. */
        this.timestamp = new Date();
        /* Initialise LinkedList */
        this.coordList = new LinkedList<Location>();
    }

    /**
     * To be implemented.
     * @return Validity of data (true = valid)
     */
    public boolean isValidData() {
        return false;
    }

    /**
     * Add a coordinate to the list.
     * @param c The location to be added to the tail of the LinkedList.
     */
    public void addCoordinate(Location c) {
        this.coordList.addLast(c);
    }

    /**
     * Returns coordinate instance at specified index.
     * @param index Specified index of coordinate.
     * @return The specified coordinate.
     */
    public Location getCoordinate(int index) {
        return this.coordList.get(index);
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

    /**
     * Returns current timestamp.
     * @return The current timestamp.
     */
    public Date getTimestamp() {
        return this.timestamp;
    }


}
