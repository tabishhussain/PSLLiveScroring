package com.android.tabishhussain.psllivescoring.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by Tabish Hussain on 12/24/2015.
 */
public class HttpUtils {

    public static boolean ifDataChanged(String url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("HEAD");
            return (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET THE LAST MODIFIED TIME
    public static Date LastModified(String url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            long date = httpURLConnection.getLastModified();
            return date != 0 ? new Date(date) : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
