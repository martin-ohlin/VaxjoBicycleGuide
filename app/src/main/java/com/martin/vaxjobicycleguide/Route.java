package com.martin.vaxjobicycleguide;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

public class Route implements Parcelable {

    //public boolean active;
    public Number asphalt;
    public String banner;
    public String description;
    public String distance;
    public ParseGeoPoint finish;
    public Number gravel;
    public String name;
    public List<String> photos;
    public int rating;
    public String signs;
    public ParseGeoPoint start;
    public Number time;
    public Number trail;
    public int type;
    public String gpx;
    public String tcx;
    public Date createdAt;
    public Date updatedAt;

    public Route(ParseObject parseRoute) {
        this.asphalt = parseRoute.getNumber("Asphalt"); // Converted
        this.banner = parseRoute.getString("Banner");
        this.description = parseRoute.getString("Description");
        this.distance = parseRoute.getString("Distance");
        this.finish = parseRoute.getParseGeoPoint("Finish");
        this.gravel = parseRoute.getNumber("Gravel");
        this.name = parseRoute.getString("Name");
        this.photos = parseRoute.getList("photos");
        this.rating = parseRoute.getInt("Rating"); // Converted
        this.signs = parseRoute.getString("Signs");
        this.start = parseRoute.getParseGeoPoint("Start");
        this.time = parseRoute.getNumber("Time");
        this.trail = parseRoute.getNumber("Trail");
        this.type = parseRoute.getInt("Type"); // Converted
        this.gpx = parseRoute.getString("gpx");
        this.tcx = parseRoute.getString("tcx");
        this.createdAt = parseRoute.getDate("createdAt");
        this.updatedAt = parseRoute.getDate("updatedAt");
    }

    private Route(Parcel in) {
        this.asphalt = (Number) in.readValue(Number.class.getClassLoader());
        this.banner = in.readString();
        this.description = in.readString();
        this.distance = in.readString();
        this.finish = (ParseGeoPoint) in.readValue(ParseGeoPoint.class.getClassLoader());
        this.gravel = (Number) in.readValue(Number.class.getClassLoader());
        this.name = in.readString();
        this.photos = (List<String>) in.readValue(List.class.getClassLoader());
        this.rating = in.readInt();
        this.signs = in.readString();
        this.start = (ParseGeoPoint) in.readValue(ParseGeoPoint.class.getClassLoader());
        this.time = (Number) in.readValue(Number.class.getClassLoader());
        this.trail = (Number) in.readValue(Number.class.getClassLoader());
        this.type = in.readInt();
        this.gpx = in.readString();
        this.tcx = in.readString();
        this.createdAt = (Date) in.readValue(Date.class.getClassLoader());
        this.updatedAt = (Date) in.readValue(Date.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.asphalt);
        dest.writeValue(this.banner);
        dest.writeValue(this.description);
        dest.writeValue(this.distance);
        dest.writeValue(this.finish); Den här går inte att skriva
        dest.writeValue(this.gravel);
        dest.writeValue(this.name);
        dest.writeValue(this.photos);
        dest.writeValue(this.rating);
        dest.writeValue(this.signs);
        dest.writeValue(this.start);
        dest.writeValue(this.time);
        dest.writeValue(this.trail);
        dest.writeValue(this.type);
        dest.writeValue(this.gpx);
        dest.writeValue(this.tcx);
        dest.writeValue(this.createdAt);
        dest.writeValue(this.updatedAt);
    }

    public static final Parcelable.Creator<Route> CREATOR
            = new Parcelable.Creator<Route>() {
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}
