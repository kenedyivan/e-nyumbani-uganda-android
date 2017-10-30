package com.ruraara.ken.e_nyumbani.appData;

/**
 * Created by ken on 10/30/17.
 */

public class AppData {
    //public static final String HOST = "http://10.0.3.2:8000";
    public static final String HOST = "http://192.168.43.28:5000";

    public static String getHost(){
        return HOST;
    }

    public static String getFeatured(){
        return HOST+"/api/listings";
    }

    public static String getRent(){
        return HOST+"/api/listings/for-rent";
    }

    public static String getSale(){
        return HOST+"/api/listings/for-sale";
    }

    public static String getImagesPath(){
        return HOST+"/images/properties/agent_properties_120x120/";
    }
}
