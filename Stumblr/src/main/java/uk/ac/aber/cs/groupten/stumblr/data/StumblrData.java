package uk.ac.aber.cs.group10.stumblr.data;

/**
 * Abstract class containing basic common structure for all Stumblr data formats.
 */
public abstract class StumblrData {
    /**
     * The title of the given piece of data. This can be extended to Waypoints, Routes
     * and any other piece of relevant data inside the structure of Stumblr.
     */
    private String title;

    /**
     * The short description pertaining to the given piece of data. This will also be
     * extended to other component classes of StumblrData
     */
    private String shortDesc;

    /**
     * @param title The title to set.
     * @param shortDesc The short description to set.
     */
    public StumblrData(String title, String shortDesc) {
        this.title = title;
        this.shortDesc = shortDesc;
    }

    /**
     * Checks the StumblrData item for validity. Returns a boolean. (true = valid)
     * @return Whether the data is valid or not. (true = valid)
     *
     * MUST be implemented in any subclasses.
     */
    public abstract boolean isValidData();

    /**
     * Returns the title.
     * @return this.title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the current title.
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the short description
     * @return this.shortDesc
     */
    public String getShortDesc() {
        return this.getShortDesc();
    }

    /**
     * Sets the current short description.
     * @param shortDesc The short description to set.
     */
    public void getShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}
