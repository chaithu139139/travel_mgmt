package com.doublegun.travelmgmt.service;

import com.doublegun.travelmgmt.dto.LocationDTO;
import com.doublegun.travelmgmt.dto.LocationJsonDetails;
import com.doublegun.travelmgmt.model.Location;
import com.doublegun.travelmgmt.repository.LocationRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Value("${UPLOAD_DIR}")
    private String UPLOAD_DIR;


    public void insertLocation(LocationDTO locationDTO, List<MultipartFile> imageList) throws Exception {

        String locName = locationDTO.getLocationName();
        String locationAddress = locationDTO.getLocationAddress();
        LocationJsonDetails locationJsonDetails = locationDTO.getDetails();
        Double cost = locationDTO.getCost();

        if (StringUtils.isEmpty(locName)) {
            throw new Exception("invalid location name found!!!");
        }
        if (StringUtils.isEmpty(locationAddress)) {
            throw new Exception("invalid locationAddress found!!!");
        }
        if (cost == null || cost < 0) {
            throw new Exception("invalid cost found!!!");
        }

        if (locationJsonDetails == null) {
            locationJsonDetails = new LocationJsonDetails();
        }

        if (imageList != null && !imageList.isEmpty()) {
            List<String> imagePathList = saveImages(imageList);
            locationJsonDetails.setImages(imagePathList);
        }

        Location location = new Location();
        location.setLocationName(locName);
        location.setLocationAddress(locationAddress);
        location.setCost(cost);
        location.setDetails(locationJsonDetails.convertToJson());

        logger.info("insertLocation() :: location :: {}", location);
        Long refTs = System.currentTimeMillis();
        locationRepository.insertLocation(locName, locationAddress, cost, location.getDetails(), refTs, refTs);
    }

    private List<String> saveImages(List<MultipartFile> files) {
        List<String> filePathList = new ArrayList<>();

        // Create the directory if it doesn't exist
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            logger.info("directory :: {} does not exist. trying to create .. ", directory.getAbsolutePath());
            boolean isDirectoryCreated = directory.mkdirs();
            logger.info("directory :: {} creation status :: {}", directory.getAbsolutePath(), isDirectoryCreated);
        }

        // Process each uploaded file
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("invalid file received!!!");
            }

            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

            // Generate a UUID-based file name and append the file extension
            String uuidFileName = UUID.randomUUID() + fileExtension;

            String filePath = UPLOAD_DIR + uuidFileName;

            try {
                // Save the file to the server
                file.transferTo(new File(filePath));

                filePathList.add(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("caught exception while saving images");
            }
        }

        return filePathList;
    }

    public List<LocationDTO> getLocations() {
        logger.info("getLocations() :: got called!!");
        List<Location> locationList = locationRepository.getLocations();
        if (locationList == null || locationList.isEmpty()) {
            logger.info("getLocations() :: locationList is null or empty!!!");
            return null;
        }
        List<LocationDTO> locationDTOList = new ArrayList<>();
        for (Location location : locationList) {
            LocationDTO locationDTO = new LocationDTO(location.getId(), location.getLocationName(), location.getLocationAddress(), location.getCost(), location.getDetails());
            locationDTOList.add(locationDTO);
        }
        return locationDTOList;
    }
}
