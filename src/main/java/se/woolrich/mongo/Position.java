package se.woolrich.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.annotation.Id;


@JsonSerialize
public class Position {

    @Id
    private String id;
    private float lat;
    private float lng;

    public Position() { }

    public String getId() {
        return id;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLat() {
        return lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLng() {
        return lng;
    }
}