package com.martin.vaxjobicycleguide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.martin.vaxjobicycleguide.ui.NotifyingScrollView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RouteInformationActivity extends ActionBarActivity {

    public static final String EXTRA_ROUTE = "EXTRA_ROUTE";

    private Route mRoute;
    private Drawable mActionBarBackgroundDrawable;

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

        Gallery imageGallery = (Gallery) findViewById(R.id.image_gallery);
        if (mRoute.photos != null && mRoute.photos.size() != 0)
            imageGallery.setAdapter(new PhotoArrayAdapter(this, R.layout.list_item_photo, this.mRoute.photos));
        else
            imageGallery.setVisibility(View.GONE);
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
        ((TextView)findViewById(R.id.created_by_description)).setText("Den här turen är skapad av...");
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
}
