package com.doublegun.travelmgmt.controllers;


import com.doublegun.travelmgmt.dto.ApiResponse;
import com.doublegun.travelmgmt.dto.LocationDTO;
import com.doublegun.travelmgmt.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/travel")
@CrossOrigin("*")
public class TravelMgmtController {

    private static final Logger logger = LoggerFactory.getLogger(TravelMgmtController.class);


    private final LocationService locationService;

    public TravelMgmtController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/insertLocation")
    public ApiResponse insertLocation(@RequestParam("images") List<MultipartFile> imageList, @RequestParam String locationDetails) {
        logger.info("insertLocation() :: locationDetails :: {}", locationDetails);

        if (locationDetails == null)
            return new ApiResponse("invalid locationDetails received!!!", null, HttpStatus.BAD_REQUEST.value());

        LocationDTO locationDTO = LocationDTO.convertToPojo(locationDetails);
        if (locationDTO == null)
            return new ApiResponse("invalid locationDetails received!!!", null, HttpStatus.BAD_REQUEST.value());

        try {
            locationService.insertLocation(locationDTO, imageList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getLocalizedMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ApiResponse("location inserted successfully!!!", null, HttpStatus.OK.value());
    }

    @GetMapping("/getLocations")
    public ApiResponse getLocations() {
        logger.info("getLocations() :: got called!!!");
        try {
            List<LocationDTO> locationList = locationService.getLocations();
            return new ApiResponse("locations", locationList, HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getLocalizedMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
