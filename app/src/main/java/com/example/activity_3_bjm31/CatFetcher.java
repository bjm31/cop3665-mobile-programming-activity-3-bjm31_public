package com.example.activity_3_bjm31;

import com.example.activity_3_bjm31.models.GalleryItem;
import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CatFetcher {

    private static final String TAG = "CatFetcher";
    private static final String API_KEY = "73c64301-fff0-4dbe-9d05-f177e4f6640c";

    public byte[] getURLBytes(String urlSpec) throws IOException
    {
        URL url = new URL(urlSpec);
        //setup http connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("x-api-key", API_KEY);

        try {
            //setup input and output
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();


            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            //continuously read data from the input stream into the output stream
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally
        {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException
    {
        return new String(getURLBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems() {
        List<GalleryItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.thecatapi.com/v1/images/search")
                    .buildUpon()
                    .appendQueryParameter("size", "thumb")
                    .appendQueryParameter("limit", "15") //changed from 100 to 15
                    //.appendQueryParameter("page", )
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            parseItems(items, jsonString);

        } catch(IOException ioe) {
            Log.e(TAG, "Failed to fetch items: ", ioe);

        } catch(JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    public void parseItems(List<GalleryItem> items, String jsonString) throws IOException, JSONException {
        JSONArray catsJSONArray = new JSONArray(jsonString);

        for (int i = 0; i < catsJSONArray.length(); ++i) {
            JSONObject catJSONObject = catsJSONArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(catJSONObject.getString("id"));
            item.setUrl(catJSONObject.getString("url"));

            items.add(item);
        }
    }
}
