package com.doublegun.travelmgmt.dto;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.util.List;

@Data
public class LocationJsonDetails {

    private static final Gson gson = new GsonBuilder().create();
    private List<String> featureList;
    private List<String> activities;
    private List<String> images;
    private List<String> dietaryList;

    public String convertToJson() {
        return gson.toJson(this);
    }

    public static LocationJsonDetails convertToPojo(String details){
        return gson.fromJson(details, LocationJsonDetails.class);
    }
}
