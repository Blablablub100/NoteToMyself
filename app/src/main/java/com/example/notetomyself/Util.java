package com.example.notetomyself;

import android.app.Activity;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class Util {


    static void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, 2000).show();
    }


    static File createAudioFile(Activity activity) {
        String path = Environment.getExternalStorageDirectory() + "/audio_notes/";
        File tmp = new File(path);
        if (!tmp.exists()) tmp.mkdirs();
        path = path + Calendar.getInstance().getTimeInMillis();
        tmp = new File(path);
        try {
            tmp.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Util.showSnackBar(activity, activity.getString(R.string.err_unable_to_create_file));
            return null;
        }
        return tmp;
    }


    static String getFormattedTime(Activity activity) {
        Calendar cal = Calendar.getInstance();

        DateFormat timeDF = android.text.format.DateFormat
                .getTimeFormat(activity.getApplicationContext());
        DateFormat dateDF = android.text.format.DateFormat
                .getDateFormat(activity.getApplicationContext());

        String time = dateDF.format(cal.getTime()) + ",\t\t" + timeDF.format(cal.getTime());
        return time;
    }
}
