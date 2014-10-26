package com.martin.vaxjobicycleguide.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

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
        this.setMultiTouchControls(true);
        this.getController().setZoom(12);

        // Typical url for a map tile on this server
        //http://www.bellander.net/zip/Tiles/12/2216/1258.png

        // Add tiles layer with custom tile source
        //final MapTileProviderBasic tileProvider = new MapTileProviderBasic(context);
        final ITileSource tileSource = new XYTileSource("Bellander", null, 8, 15, 256, ".png", new String[] {"http://www.bellander.net/zip/Tiles/"});
        //tileProvider.setTileSource(tileSource);

        //final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, context);
        //tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        //this.getOverlays().add(tilesOverlay);
        this.setTileSource(tileSource);

        // When the server is up and running we can probably do like this
        //mMapView.setTileSource(tileSource);
    }

}
