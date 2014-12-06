package com.martin.vaxjobicycleguide;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Route implements Parcelable {

    //public boolean active;
    public String asphalt;
    public String banner;
    public String description;
    public String distance;
//    public ParseGeoPoint finish;
    public Double gravel;
    public String name;
    public ArrayList<String> photos;
    public Integer rating;
    public String signs;
//    public ParseGeoPoint start;
    public Double time;
    public Double trail;
    public Integer type;
    public String gpx;
    public String tcx;
    public String ownerName;
    public String ownerDescription;

    public Route(ParseObject parseRoute) {
        this.asphalt = parseRoute.getString("Asphalt"); // Converted
        this.banner = parseRoute.getString("Banner");
        this.description = parseRoute.getString("Description");
        this.distance = parseRoute.getString("Distance");
        //this.finish = parseRoute.getParseGeoPoint("Finish");
        this.gravel = parseRoute.getDouble("Gravel");
        this.name = parseRoute.getString("Name");

        JSONArray photosJSONArray = parseRoute.getJSONArray("Photos");
        if (photosJSONArray != null) {
            this.photos = new ArrayList<String>(photosJSONArray.length());
            for (int i=0; i<photosJSONArray.length(); i++) {
                try {
                    photos.add(photosJSONArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        this.rating = parseRoute.getInt("Rating"); // Converted
        this.signs = parseRoute.getString("Signs");
        //this.start = parseRoute.getParseGeoPoint("Start");
        this.time = parseRoute.getDouble("Time");
        this.trail = parseRoute.getDouble("Trail");
        this.type = parseRoute.getInt("Type"); // Converted
        this.gpx = parseRoute.getString("gpx");
        ParseObject owner = parseRoute.getParseObject("Owner");
        this.tcx = parseRoute.getString("tcx");

        String firstName = owner.getString("Firstname");
        String lastName = owner.getString("Lastname");
        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName))
            this.ownerName = firstName + " " + lastName;
        else if (!TextUtils.isEmpty(firstName))
            this.ownerName = firstName;
        else if (!TextUtils.isEmpty(lastName))
            this.ownerName = lastName;

        this.ownerDescription = owner.getString("Description");
    }

    private Route(Parcel in) {
        this.asphalt = in.readString();
        this.banner = in.readString();
        this.description = in.readString();
        this.distance = in.readString();
        //this.finish = (ParseGeoPoint) in.readValue(ParseGeoPoint.class.getClassLoader());
        this.gravel = (Double) in.readValue(Double.class.getClassLoader());
        this.name = in.readString();
        this.photos = in.readArrayList(String.class.getClassLoader());
        this.rating = (Integer) in.readValue(Integer.class.getClassLoader());
        this.signs = in.readString();
        //this.start = (ParseGeoPoint) in.readValue(ParseGeoPoint.class.getClassLoader());
        this.time = (Double) in.readValue(Double.class.getClassLoader());
        this.trail = (Double) in.readValue(Double.class.getClassLoader());
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.gpx = in.readString();
        this.tcx = in.readString();
        this.ownerName = in.readString();
        this.ownerDescription = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(asphalt);
        dest.writeString(banner);
        dest.writeString(description);

        dest.writeString(this.distance);
//        dest.writeValue(this.finish);
        dest.writeValue(this.gravel);
        dest.writeString(this.name);
        dest.writeList(this.photos);
        dest.writeValue(this.rating);
        dest.writeString(this.signs);
//        dest.writeValue(this.start);
        dest.writeValue(this.time);
        dest.writeValue(this.trail);
        dest.writeValue(this.type);
        dest.writeString(this.gpx);
        dest.writeString(this.tcx);
        dest.writeString(this.ownerName);
        dest.writeString(this.ownerDescription);
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
