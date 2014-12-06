package com.martin.vaxjobicycleguide;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

public class ClickListenerOverlay extends Overlay {

    public interface OnSingleTapUpListener {
        public void onSingleTapUp();
    }

    private OnSingleTapUpListener mOnSingleTapUpListener;

    public ClickListenerOverlay(Context context, OnSingleTapUpListener listener) {
        this(new DefaultResourceProxyImpl(context), listener);
    }

    public ClickListenerOverlay(ResourceProxy resourceProxy, OnSingleTapUpListener listener) {
        super(resourceProxy);

        this.mOnSingleTapUpListener = listener;
    }

    @Override
    protected void draw(Canvas c, MapView osmv, boolean shadow) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
        if (this.mOnSingleTapUpListener != null)
            this.mOnSingleTapUpListener.onSingleTapUp();

        return super.onSingleTapUp(e, mapView);
    }
}
