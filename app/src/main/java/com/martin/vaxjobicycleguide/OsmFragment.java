package com.martin.vaxjobicycleguide;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martin.vaxjobicycleguide.ui.VaxjoBikeGuideMapView;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class OsmFragment extends Fragment{
    private VaxjoBikeGuideMapView mMapView;
    private MyLocationNewOverlay mLocationOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = inflater.getContext().getApplicationContext();

        final View view = inflater.inflate(R.layout.fragment_osm, container, false);

        this.mMapView = (VaxjoBikeGuideMapView) view.findViewById(R.id.map_view);

        // Add users location overlay
        this.mLocationOverlay = new MyLocationNewOverlay(context, new GpsMyLocationProvider(context), this.mMapView);
        this.mMapView.getOverlays().add(this.mLocationOverlay);

       // When the server is up and running we can probably do like this
        //mMapView.setTileSource(tileSource);

        // Call this method to turn off hardware acceleration at the View level.
        setHardwareAccelerationOff();


        //TODO:Add code for hiding the actionbar as soon as the user scrolls around in the map.

        // We can only get the height of the actionbar after the layout has been performed
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                TextView headerView = (TextView) getView().findViewById(R.id.actionbar_header_overlay_buffer);
                int height = ((ActionBarActivity)getActivity()).getSupportActionBar().getHeight();
                headerView.setHeight(height);
                headerView.setText("Placeholder");

                setBoundingBox();
            }
        });

        return view;
    }

    private void setBoundingBox() {
        BoundingBoxE6 boundingBoxE6 = new BoundingBoxE6(57.25, 15.3, 56.45, 14.4);
        //this.mMapView.zoomToBoundingBox(boundingBoxE6);
        this.mMapView.setScrollableAreaLimit(boundingBoxE6);
        Location lastKnownLocation = ((LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (lastKnownLocation != null)
            mMapView.getController().setCenter(new GeoPoint(lastKnownLocation));
        else
            mMapView.getController().setCenter(new GeoPoint(Location.convert("56:52.591"), Location.convert("14:48.415")));

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setHardwareAccelerationOff()
    {
        // Turn off hardware acceleration here, or in manifest
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mMapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
