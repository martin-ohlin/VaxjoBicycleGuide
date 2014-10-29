package com.martin.vaxjobicycleguide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.martin.vaxjobicycleguide.ui.NotifyingScrollView;
import com.martin.vaxjobicycleguide.ui.VaxjoBikeGuideMapView;
import com.squareup.picasso.Picasso;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class RouteInformationActivity extends ActionBarActivity {

    public static final String EXTRA_ROUTE = "EXTRA_ROUTE";

    private Route mRoute;
    private Drawable mActionBarBackgroundDrawable;
    private VaxjoBikeGuideMapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_route_information);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = savedInstanceState;
        if (bundle == null || bundle.isEmpty())
            bundle = getIntent().getExtras();

        if (bundle != null) {
            this.mRoute = bundle.getParcelable(EXTRA_ROUTE);
            updateInformation();

            if (mRoute.gpx != null) {
                String fileName = mRoute.gpx.substring(mRoute.gpx.lastIndexOf('/'));
                downloadGPXFile(mRoute.gpx, fileName);
            }
        }

        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ab_solid_vaxjobikeguide);
        //mActionBarBackgroundDrawable = new ColorDrawable(R.color.cyan_500);
        //mActionBarBackgroundDrawable = new ColorDrawable(android.R.color.black);
        mActionBarBackgroundDrawable.setAlpha(0);

        getSupportActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);

        ((NotifyingScrollView) findViewById(R.id.scroll_view)).setOnScrollChangedListener(mOnScrollChangedListener);
        ((NotifyingScrollView) findViewById(R.id.scroll_view)).setOverScrollEnabled(false);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }

        this.mMapView = (VaxjoBikeGuideMapView) findViewById(R.id.map_view);

        mMapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    mMapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mMapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                centerMap();
            }
        });
    }

    private void centerMap() {
        mMapView.getController().setCenter(new GeoPoint(Location.convert("56:52.591"), Location.convert("14:48.415")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = findViewById(R.id.list_item_route_banner_image).getHeight() - getSupportActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
            //mActionBarBackgroundDrawable.setAlpha(255);
        }
    };

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getSupportActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_ROUTE, mRoute);
    }

    private void updateInformation() {
        setTitle(mRoute.name);

        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;
        // Take the largest so that it will fit both in portrait and landscape mode
        final int imageWidth = width >= height ? width : height;
        String urlForPreScaledBanner = String.format("http://www.vaxjobicycleguide.se/php/timthumb.php?src=%1$s&w=%2$d", mRoute.banner, imageWidth);

        ImageView bannerImageView = (ImageView) findViewById(R.id.list_item_route_banner_image);
        Picasso.with(this)
                .load(urlForPreScaledBanner)
                .fit()
                .centerCrop()
                .into(bannerImageView);

        ((TextView)findViewById(R.id.description)).setText(this.mRoute.description);
        //((TextView)findViewById(R.id.type)).setText(this.mRoute.type);
        ((TextView)findViewById(R.id.length)).setText(this.mRoute.distance);
        //((TextView)findViewById(R.id.rating)).setText(this.mRoute.rating);
        ((TextView)findViewById(R.id.signs)).setText(this.mRoute.signs);
        //((TextView)findViewById(R.id.terrain)).setText();

        String createdBy = String.format(getResources().getString(R.string.created_by), this.mRoute.ownerName);
        ((TextView)findViewById(R.id.created_by_name)).setText(createdBy);
        ((TextView)findViewById(R.id.created_by_description)).setText(this.mRoute.ownerDescription);
    }

    private class PhotoArrayAdapter extends ArrayAdapter<String> {
        private final Context mContext;
        private final int imageWidth;

        public PhotoArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.mContext = context;
            final int width = context.getResources().getDisplayMetrics().widthPixels;
            final int height = context.getResources().getDisplayMetrics().heightPixels;
            // Take the largest so that it will fit both in portrait and landscape mode
            this.imageWidth = width >= height ? width : height;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.list_item_photo, parent, false);
                //((ImageView) rowView).getLayoutParams().width = imageWidth - 100;
            }

            String imageUrl = getItem(position);

            String urlForPreScaledBanner = String.format("http://www.vaxjobicycleguide.se/php/timthumb.php?src=%1$s&w=%2$d", imageUrl, imageWidth);

            ImageView bannerImageView = (ImageView) rowView.findViewById(R.id.list_item_photo);
            Picasso.with(mContext)
                    .load(urlForPreScaledBanner)
                    .fit()
                    .centerInside()
                    .into(bannerImageView);

            return rowView;
        }
    }

    private void updatePathOverlay(List<GpxParser.Entry> entries) {
        if (entries == null || entries.size() == 0)
            return;

        GpxParser.Entry firstEntry = entries.get(0);
        double minLatitude = firstEntry.latitude;
        double maxLatitude = firstEntry.latitude;
        double minLongitude = firstEntry.longitude;
        double maxLongitude = firstEntry.longitude;

        PathOverlay pathOverlay = new PathOverlay(getResources().getColor(R.color.pink_500), this);
        for (GpxParser.Entry entry : entries) {
            pathOverlay.addPoint((int)(entry.latitude * 1e6), (int)(entry.longitude * 1e6));

            if (entry.latitude < minLatitude)
                minLatitude = entry.latitude;

            if (entry.latitude > maxLatitude)
                maxLatitude = entry.latitude;

            if (entry.longitude < minLongitude)
                minLongitude = entry.longitude;

            if (entry.longitude > maxLongitude)
                maxLongitude = entry.longitude;
        }

        this.mMapView.getOverlays().add(pathOverlay);

        BoundingBoxE6 boundingBoxE6 = new BoundingBoxE6(maxLatitude, maxLongitude, minLatitude, minLongitude);
        this.mMapView.zoomToBoundingBox(boundingBoxE6);
        this.mMapView.setScrollableAreaLimit(boundingBoxE6);
    }

    private void downloadGPXFile(final String downloadString, final String fileName) {
        URL url;
        try {
            url = new URL(downloadString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        new AsyncTask<URL, Void, List<GpxParser.Entry>>() {
            @Override
            protected List<GpxParser.Entry> doInBackground(URL... url) {
                File path = getDownloadFolderPath();
                File file = new File(path, fileName);
                // If the file already exists and was updated within the last day
                long compareDate = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS;
                if (file.exists() && file.lastModified() > compareDate) {
                    return parseFile(file);
                } else if (FileDownloader.DownloadFile(url[0], file)) {
                    return parseFile(file);
                } else {
                    return null;
                }
            }

            private List<GpxParser.Entry> parseFile(final File file) {
                GpxParser parser = new GpxParser();
                try {
                    return parser.parse(new BufferedInputStream(new FileInputStream(file)));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            private File getDownloadFolderPath() {
                File path;
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    path = getExternalFilesDir("gpx");
                } else {
                    path = new File(getFilesDir(), "gpx");
                }

                if (!path.exists())
                    path.mkdirs();

                return path;
            }

            protected void onPostExecute(List<GpxParser.Entry> entries) {
                if (entries != null) {
                    updatePathOverlay(entries);
                }
            }
        }.execute(url);
    }
}
