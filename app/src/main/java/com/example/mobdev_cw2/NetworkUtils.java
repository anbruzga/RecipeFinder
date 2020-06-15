package com.example.mobdev_cw2;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkUtils {

    static String getRecipeInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String recipeJSString = null;

        try {
            String uri = "https://www.food2fork.com/api/search?key=" +
                    "719278d7a6d22fceb7b6df07d919b66d&q="+queryString;

            // Convert the URI to a URL.
            URL requestURL = new URL(uri);

            // Taken from https://github.com/google-developer-training/android-fundamentals-apps-v2/blob/master/WhoWroteItLoader/app/src/main/java/com/example/android/whowroteitloader/BookLoader.java
            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Add the current line to the string.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  Exit without parsing.
                return null;
            }
            // The end of taken code
            System.out.println(builder.toString());

            recipeJSString = builder.toString();

            // Taken from https://github.com/google-developer-training/android-fundamentals-apps-v2/blob/master/WhoWroteItLoader/app/src/main/java/com/example/android/whowroteitloader/BookLoader.java
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // The end of taken code

        return recipeJSString;
    }
}