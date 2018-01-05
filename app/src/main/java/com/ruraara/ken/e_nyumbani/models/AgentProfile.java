package com.ruraara.ken.e_nyumbani.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 1/2/18.
 */

public class AgentProfile {
    public String image = null;
    public String firstName = null;
    public String lastName = null;
    public String company = null;
    public String officePhone = null;
    public String mobilePhone = null;
    public String email = null;
    public JSONArray agentPropertiesJson = null;

    public static List<AgentProperties> agentPropertyList = new ArrayList<>();

    public AgentProfile(String image, String firstName, String lastName, String company,
                        String officePhone, String mobilePhone, String email, JSONArray agentPropertiesJson){
        clearAgentProperties();

        this.image = image;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.officePhone = officePhone;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.agentPropertiesJson = agentPropertiesJson;
    }

    //Returns related properties
    public List<AgentProperties> getAgentProeperties() {
        try {
            for (int i = 0; i < agentPropertiesJson.length(); i++) {
                JSONObject jsonObject = agentPropertiesJson.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String image = jsonObject.getString("image");
                String status = jsonObject.getString("status");
                int rating = jsonObject.getInt("rating");

                agentPropertyList.add(new AgentProperties(id, title, image, status, (double) rating));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return agentPropertyList;

    }

    //clears related properties list
    private void clearAgentProperties() {
        if (agentPropertyList != null) {
            agentPropertyList.clear();
        }
    }

    public static class AgentProperties {
        public final String id;
        public final String title;
        public final String image;
        public final String status;
        public final double rating;

        AgentProperties(String id, String title, String image, String status, double rating) {
            this.id = id;
            this.title = title;
            this.image = image;
            this.status = status;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
