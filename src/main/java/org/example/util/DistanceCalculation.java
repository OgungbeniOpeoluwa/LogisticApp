package org.example.util;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import org.example.dto.response.LongitudeLatitudeResponse;


public class DistanceCalculation {

    public static LongitudeLatitudeResponse location(String address) {
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("3d8355ca43f44016ad9505dfe71eee53");
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
        LongitudeLatitudeResponse longitudeLatitudeResponse = new LongitudeLatitudeResponse();
        longitudeLatitudeResponse.setLongitude(Double.parseDouble(firstResultLatLng.getLng().toString()));
        longitudeLatitudeResponse.setLatitude(Double.parseDouble(firstResultLatLng.getLat().toString()));
        return longitudeLatitudeResponse;
    }


        public  static double distance(double lat1, double lon1, double lat2, double lon2) {
            final int earthRadius = 6371;

            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);

            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);

            double result = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return earthRadius * result;
            }


}
