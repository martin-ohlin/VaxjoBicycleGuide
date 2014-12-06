package com.martin.vaxjobicycleguide.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class VaxjoBikeGuideMapView extends MapView {

    /**
     * Constructor used by XML layout resource (uses default tile source).
     */
    public VaxjoBikeGuideMapView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    public VaxjoBikeGuideMapView(Context context) {
        super(context, 256);

        initialize();
    }

    private void initialize() {
        if (isInEditMode())
            return;

        this.setMultiTouchControls(true);
        this.getController().setZoom(12);


        final ITileSource tileSource = new XYTileSource("Bellander", null, 8, 15, 256, ".png", new String[] {"http://www.bellander.net/zip/Tiles/"});
        this.setTileSource(tileSource);

        BoundingBoxE6 boundingBoxE6 = new BoundingBoxE6(57.25, 15.3, 56.45, 14.4);
        this.setScrollableAreaLimit(boundingBoxE6);

        // Call this method to turn off hardware acceleration at the View level.
        setHardwareAccelerationOff();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setHardwareAccelerationOff()
    {
        // Turn off hardware acceleration here, or in manifest
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
}
