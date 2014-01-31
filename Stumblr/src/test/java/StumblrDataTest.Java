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
public class StumblrDataTest {

    @Test
    public void testStumblrDataConstructor() {
        StumblrData stumblrData = new StumblrData("testTitle", "testShortDesc") {};
        Assert.assertTrue(stumblrData.getTitle().equals("testTitle"));
        Assert.assertTrue(stumblrData.getShortDesc().equals("testShortDesc"));
    }

    @Test
    public void testCorrectIsValidData(){
        StumblrData testStumblrData = new StumblrData() {};
        String testString = "Hello World";
        boolean isValid = testStumblrData.isValidData(testString);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIncorrectIsValidData(){
        StumblrData testStumblrData = new StumblrData() {};
        String testString = "Hi";
        boolean isValid = testStumblrData.isValidData(testString);
        Assert.assertTrue(!isValid);
    }

    @Test
    public void testSanitiseStringInput(){
        StumblrData testStumblrData = new StumblrData() {};
        String testString = "Hello' 'World!";
        String sanitisedTestString = testStumblrData.sanitiseStringInput(testString);
        Assert.assertTrue(sanitisedTestString.equals("Hello World!"));
    }
}