package com.ruraara.ken.e_nyumbani;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.*;
import com.ruraara.ken.e_nyumbani.dummy.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_list_content);



        /*AsyncHttpClient client = new AsyncHttpClient();
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

                Log.e(TAG,String.valueOf(Property.ITEMS.size()));

                if(Property.ITEMS.size() > 0){
                    Property.ITEMS.clear();
                }

                try {
                    JSONArray jsonArray = new JSONArray(resp);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String image = jsonObject.getString("image");
                        Property.addPropertyItem(Property.createPropertyItem(id,title,image));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Property.PropertyItem> mValues = Property.ITEMS;

                //List<String> mValues = Property.IT;

                Log.w(TAG,String.valueOf(mValues.size()));

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

        //Log.d(TAG,"Howdy up to");



        //Log.d(TAG,String.valueOf(mValues.get(0)));



        /*AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.3.2:8000/api/listings", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG,"Started request");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d(TAG,"Status: "+statusCode);
                String s = new String(response);
                Log.d(TAG,"S: "+s);
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
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(Property.ITEMS !=null || Property.ITEM_MAP != null){
            Property.ITEMS = null;
            Property.ITEM_MAP = null;
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if(Property.ITEMS !=null || Property.ITEM_MAP != null){
            Property.ITEMS = null;
            Property.ITEM_MAP = null;
        }*/

    }
}
