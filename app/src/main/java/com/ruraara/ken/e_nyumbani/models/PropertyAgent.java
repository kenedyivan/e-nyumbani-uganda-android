package com.ruraara.ken.e_nyumbani.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ken on 12/11/17.
 */

public class PropertyAgent {

    static String TAG = "PropertyAgent";

    /**
     * An array of sample items.
     */
    public static List<PropertyAgent.Agent> AGENTS = new ArrayList<Agent>();

    /**
     * A map of sample agent, by ID.
     */
    public static Map<String, PropertyAgent.Agent> AGENT_MAP = new HashMap<String, PropertyAgent.Agent>();

    public static void addPropertyAgent(PropertyAgent.Agent agent) {
        Log.w(TAG, "Creating property agent");
        AGENTS.add(agent);
        AGENT_MAP.put(agent.id, agent);
        Log.w(TAG, "Finished creating property agent");
        Log.w(TAG, String.valueOf(AGENT_MAP.size()));
        Log.w(TAG, String.valueOf(AGENTS.size()));

    }

    public static PropertyAgent.Agent createPropertyAgent(String id, String firstName,
                                                           String lastName, String company, String all, String sale, String rent, String image) {
        return new PropertyAgent.Agent(id, firstName, lastName, company, all, sale, rent, image);
    }


    /**
     * A property agent representing a piece of data.
     */
    public static class Agent {
        public final String id;
        public final String firstName;
        public final String lastName;
        public final String company;
        public final String all;
        public final String sale;
        public final String rent;
        public final String image;

        public Agent(String id, String firstName, String lastName, String company, String all, String sale, String rent, String image) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.company = company;
            this.all = all;
            this.sale = sale;
            this.rent = rent;
            this.image = image;
        }

        @Override
        public String toString() {
            return firstName;
        }
    }
}
