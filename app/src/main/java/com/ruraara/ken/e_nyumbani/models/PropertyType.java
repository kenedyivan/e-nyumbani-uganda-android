package com.ruraara.ken.e_nyumbani.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 1/14/18.
 */

public class PropertyType {
    private String id;
    private String name;

    public List<PropertyType> propertyTypeList = new ArrayList<>();

    public PropertyType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public PropertyType() {
    }


    public String[] typeNames() {
        String[] names = new String[propertyTypeList.size()];
        for (int i = 0; i < propertyTypeList.size(); i++) {
            names[i] = propertyTypeList.get(i).name;
        }

        return names;
    }

    public String typeId(int nameIndex) {
        String[] ids = new String[propertyTypeList.size()];
        for (int i = 0; i < propertyTypeList.size(); i++) {
            ids[i] = propertyTypeList.get(i).id;
        }

        return ids[nameIndex];
    }

    @Override
    public String toString() {
        return name;
    }
}
