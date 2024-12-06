package com.doublegun.travelmgmt.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

@Data
public class LocationDTO {
    private static final Gson gson = new GsonBuilder().create();

    private Long id;

    private String locationName;

    private String locationAddress;

    private Double cost;

    private LocationJsonDetails details;

    public LocationDTO() {
    }

    public LocationDTO(Long id, String locationName, String locationAddress, Double cost, LocationJsonDetails details) {
        this.id = id;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.cost = cost;
        this.details = details;
    }

    public LocationDTO(Long id, String locationName, String locationAddress, Double cost, String details) {
        this.id = id;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.cost = cost;
        this.details = LocationJsonDetails.convertToPojo(details);
    }

    public String convertToJson(LocationDTO locationDTO) {
        return gson.toJson(locationDTO);
    }

    public static LocationDTO convertToPojo(String location){
        return gson.fromJson(location, LocationDTO.class);
    }
}
