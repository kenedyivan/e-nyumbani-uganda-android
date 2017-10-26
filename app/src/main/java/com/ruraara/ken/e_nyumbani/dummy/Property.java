package com.ruraara.ken.e_nyumbani.dummy;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ken on 10/25/17.
 */

public class Property {
    
    static String TAG = "PropertyItem";

    /**
     * An array of sample (dummy) items.
     */
    public static List<Property.PropertyItem> ITEMS = new ArrayList<Property.PropertyItem>();

    public static List<String> IT = new ArrayList<String>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Property.PropertyItem> ITEM_MAP = new HashMap<String, Property.PropertyItem>();
    

    /*static {

        IT.add("Akena");
        IT.add("Kenedy");
        IT.add("Ivan");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.3.2:8000/api/listings", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG,"Started request");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d(TAG,"Status: "+statusCode);
                String resp = new String(response);
                Log.d(TAG,"S: "+resp);

                try {
                    JSONArray jsonArray = new JSONArray(resp);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String image = jsonObject.getString("image");
                        addPropertyItem(createPropertyItem(id,title,image));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG,"failed "+statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG,"retryNO: "+retryNo);
            }
        });
    }*/

    public static void addPropertyItem(PropertyItem item) {
        Log.w(TAG,"Creating property item");
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        Log.w(TAG,"Finished creating property item");
        Log.w(TAG,String.valueOf(ITEM_MAP.size()));
        Log.w(TAG,String.valueOf(ITEMS.size()));

    }

    public static PropertyItem createPropertyItem(int id, String title, String image) {
        return new PropertyItem(String.valueOf(id), "Item " + title, image);
    }
    
    /**
     * A property item representing a piece of data.
     */
    public static class PropertyItem {
        public final String id;
        public final String title;
        public final String image;

        public PropertyItem(String id, String title, String image) {
            this.id = id;
            this.title = title;
            this.image = image;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
