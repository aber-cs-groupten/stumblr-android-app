package com.element84.starter;

import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.util.Config;
import android.widget.TextView;

@RunWith(RobolectricTestRunner.class)
public class FirstTest {
    @Test
    public void testMonkey() {
        assertEquals(true, true);
    }
}