package uk.ac.aber.cs.groupten.stumblr;

import android.app.AlertDialog;
import android.content.Context;

import java.lang.reflect.Method;

public class Dialogger {
    public static void twoOptionBox(String title, String message, String option1, String option2,
                                    Method action1, Method action2) {


    }

    public static AlertDialog informationBox(Context context, String title,
                                             String message, String button_text) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(button_text, null);

        return builder.create();
    }
}