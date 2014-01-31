//package com.element84.starter;
package uk.ac.aber.cs.groupten.stumblr;

import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.location.Location;
import android.os.Parcel;
import android.util.Config;
import android.widget.TextView;

import junit.framework.Assert;

import uk.ac.aber.cs.groupten.stumblr.*;

@RunWith(RobolectricTestRunner.class)
public class WaypointTest {

    @Test
    public void testFirstWaypointConstructor() {
        //Get System time
        Calendar c = Calendar.getInstance();
        long testTime = c.getTimeInMillis();

        //Construct the waypoint (which results in getting the system time inside Waypoint)
        Waypoint testWaypoint = new Waypoint();
        long waypointTime = testWaypoint.getTimestamp();

        //Compare that the results are within a second of eachother
        Assert.assertTrue(waypointTime < (testTime + 1000) && waypointTime > (testTime - 1000));
    }

    @Test
    public void testSecondWaypointConstructor() {
        //Create the First Test Waypoint
        Waypoint firstTestWaypoint = new Waypoint();

        //Populate the First Test Waypoint
        firstTestWaypoint.setTimestamp(12345);
        firstTestWaypoint.setTitle("Test");
        firstTestWaypoint.setShortDesc("TestSD");

        //Create the test Parcle
        Parcel testParcel = new Parcel();

        //Add the Test Waypoint to the Test Parcel
        firstTestWaypoint.writeToParcel(testParcel, 0);

        //Add the test Parcle to the testWaypoint via the the Waypoint constructor
        Waypoint secondTestWaypoint = new Waypoint(testParcel);

        //Test the data in the Second Test Waypoint is the same as the initial test data
        Assert.assertTrue(secondTestWaypoint.getTimestamp() == 12345);
        Assert.assertTrue(secondTestWaypoint.getTitle().equals("Test"));
        Assert.assertTrue(secondTestWaypoint.getShortDesc().equals("TestSD"));


        Assert.assertNotNull(testWaypoints);
    }

    @Test
    public void testParceling(){
        //Create the First Test Waypoint
        Waypoint firstTestWaypoint = new Waypoint();

        //Populate the First Test Waypoint
        firstTestWaypoint.setTimestamp(12345);
        firstTestWaypoint.setTitle("Test");
        firstTestWaypoint.setShortDesc("TestSD");

        //Create the test Parcle
        Parcel testParcel = new Parcel();

        //Add the Test Waypoint to the Test Parcel
        firstTestWaypoint.writeToParcel(testParcel, 0);

        //Create a second Test Waypoint
        Waypoint secondTestWaypoint = new Waypoint();

        //Read the data from the Test Parcel into the Second Test Waypoint
        secondTestWaypoint.readFromParcel(testParcel);

        //Test the data in the Second Test Waypoint is the same as the initial test data
        Assert.assertTrue(secondTestWaypoint.getTimestamp() == 12345);
        Assert.assertTrue(secondTestWaypoint.getTitle().equals("Test"));
        Assert.assertTrue(secondTestWaypoint.getShortDesc().equals("TestSD"));
    }
}