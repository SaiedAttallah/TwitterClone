package com.cosmos.saiedattallah.twitterclone.helpers;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by saeed on 23/08/17.
 */

public class RelativeTimeHelper {
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // change what is sent back to smaller format
        if(relativeDate.contains("second")) {
            relativeDate = relativeDate.replaceAll("second[s]? ago","s");
        } else if (relativeDate.contains("minute")) {
            relativeDate = relativeDate.replaceAll("minute[s]? ago","m");
        } else if (relativeDate.contains("hour")) {
            relativeDate = relativeDate.replaceAll("hour[s]? ago","h");
        } else if (relativeDate.contains("day")) {
            relativeDate = relativeDate.replaceAll("day[s]? ago","d");
        } else if (relativeDate.contains("month")) {
            relativeDate = relativeDate.replaceAll("month[s]? ago","d");
        }

        return relativeDate;
    }
}
