package com.martin.vaxjobicycleguide.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.martin.vaxjobicycleguide.R;
import com.martin.vaxjobicycleguide.tileprovider.MapTileProvider;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.views.MapView;

public class VaxjoBikeGuideMapView extends MapView {

    /**
     * Constructor used by XML layout resource
     */
    public VaxjoBikeGuideMapView(final Context context, final AttributeSet attrs) {
        super(context, 256, new DefaultResourceProxyImpl(context), new MapTileProvider(context.getApplicationContext(), new XYTileSource("Bellander_map", null, 8, 15, 256, ".png", new String[] {"http://www.bellander.net/zip/Tiles/"})), null, attrs);

        if (isInEditMode())
            return;

        this.setMultiTouchControls(true);
        this.getController().setZoom(12);

        // Add the contour lines as the first layer
        // The contour only has zoom levels 13-15, but we need to specify the full zoom level here otherwise we can't zoom
        //final ITileSource contourTileSource = new XYTileSource("Bellander_contour", null, 13, 15, 256, ".png", new String[] {"http://www.vaxjobicycleguide.se/contourTiles/"});
        //final ITileSource mapTileSource = new XYTileSource("Bellander_map", null, 8, 15, 256, ".png", new String[] {"http://www.bellander.net/zip/Tiles/"});
        //this.setTileSource(contourTileSource);
        //this.setTileSource(mapTileSource);
        this.setBackgroundColor(context.getResources().getColor(R.color.map_background));
        this.getOverlayManager().getTilesOverlay().setLoadingLineColor(context.getResources().getColor(R.color.map_background));

        //This is mostly working, but there is a problem with the caching when using different zoom-levels for the overlays.
/*
        //create an overlay with the actual map
        final ITileSource mapTileSource = new XYTileSource("Bellander_map", null, 13, 15, 256, ".png", new String[] {"http://www.bellander.net/zip/Tiles/"});
        final MapTileProviderBasic mapTileProvider = new MapTileProviderBasic(context.getApplicationContext(), mapTileSource);
        final TilesOverlay mapTilesOverlay = new TilesOverlay(mapTileProvider, context);
        // Transparent loading background color implies transparent line color as well
        mapTilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        this.getOverlays().add(mapTilesOverlay);
*/
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
