package com.martin.vaxjobicycleguide;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.martin.vaxjobicycleguide.ui.VaxjoBikeGuideMapView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
// TODO: Use this one instead of MyLocationNewOverlay and feed it with our own location data
//import org.osmdroid.views.overlay.DirectedLocationOverlay

//TODO: Use this to display a path
import org.osmdroid.views.overlay.PathOverlay;

public class OsmFragment extends Fragment{
    private VaxjoBikeGuideMapView mMapView;
    private MyLocationNewOverlay mLocationOverlay;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = inflater.getContext().getApplicationContext();

        final View view = inflater.inflate(R.layout.fragment_osm, container, false);

        this.mMapView = (VaxjoBikeGuideMapView) view.findViewById(R.id.map_view);
        this.mHandler = new Handler();

        // Add users location overlay
        this.mLocationOverlay = new MyLocationNewOverlay(context, new GpsMyLocationProvider(context), this.mMapView);
        this.mMapView.getOverlays().add(this.mLocationOverlay);

        //Hides the actionbar for 7 seconds if the user touches the map.
        //Adds a small delay to the hiding to let the activity detect if this is maybe
        //a touch that will drag the page away.
        this.mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mHandler.removeCallbacks(hideActionBarRunnable);
                    mHandler.removeCallbacks(showActionBarRunnable);

                    mHandler.postDelayed(hideActionBarRunnable, 200);

                    mHandler.postDelayed(showActionBarRunnable, DateUtils.SECOND_IN_MILLIS * 7);
                }
                return false;
            }
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                centerMap();
            }
        });

        return view;
    }

    private Runnable hideActionBarRunnable = new Runnable() {
        @Override
        public void run() {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null)
                activity.showActionBar(false);
        }
    };

    private Runnable showActionBarRunnable = new Runnable() {
        @Override
        public void run() {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null)
                activity.showActionBar(true);
        }
    };

    private void centerMap() {
        mMapView.getController().setCenter(new GeoPoint(Location.convert("56:52.591"), Location.convert("14:48.415")));
    }

    @Override
    public void onPause() {
        super.onPause();

        this.mLocationOverlay.disableMyLocation();
    }

    @Override
    public void onResume() {
        super.onResume();

        this.mLocationOverlay.enableMyLocation();
    }
}
