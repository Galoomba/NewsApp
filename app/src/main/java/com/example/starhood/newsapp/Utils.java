package com.example.starhood.newsapp;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Starhood on 7/3/17.
 */

public final class Utils {


    public static List<News> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Utils", "Problem making the HTTP request.", e);
        }

        List<News> news = extractFeatureFromJson(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Utils", "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Utils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Utils", "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<News> extractFeatureFromJson(String JSONString) {

        if (TextUtils.isEmpty(JSONString)) {
            return null;
        }

        List<News> news = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(JSONString);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONArray jsonArray = response.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentObject = jsonArray.getJSONObject(i);

                String Title = null;
                if (currentObject.optString("webTitle") != null)
                    Title = currentObject.getString("webTitle");

                String Type = null;
                if (currentObject.optString("type") != null)
                    Type = currentObject.getString("type");

                String sectionName = null;
                if (currentObject.optString("sectionName") != null)
                    sectionName = currentObject.getString("sectionName");

                String webUrl = null;
                if (currentObject.optString("webUrl") != null)
                    webUrl = currentObject.getString("webUrl");

                String dateNtime = null;
                if (currentObject.optString("webPublicationDate") != null)
                    dateNtime = currentObject.getString("webPublicationDate");

                String date = null;
                String time = null;

                if (dateNtime != null) {
                    String[] buffer = dateNtime.split("T");
                    date = buffer[0];
                    time = buffer[1];
                }

                News newsItem = new News(Title, Type, time, date, sectionName, webUrl);
                news.add(newsItem);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return news;
    }
}