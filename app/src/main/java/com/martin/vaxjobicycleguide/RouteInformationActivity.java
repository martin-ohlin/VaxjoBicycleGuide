package com.martin.vaxjobicycleguide;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class RouteInformationActivity extends Activity {

    public static final String EXTRA_ROUTE = "EXTRA_ROUTE";

    private Route mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_information);

        Bundle bundle = savedInstanceState;
        if (bundle == null || bundle.isEmpty())
            bundle = getIntent().getExtras();

        if (bundle != null) {
            this.mRoute = bundle.getParcelable(EXTRA_ROUTE);
            updateInformation();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_ROUTE, mRoute);
    }

    private void updateInformation() {
        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;
        // Take the largest so that it will fit both in portrait and landscape mode
        final int imageWidth = width >= height ? width : height;
        String urlForPreScaledBanner = String.format("http://www.vaxjobicycleguide.se/php/timthumb.php?src=%1$s&w=%2$d", mRoute.banner, imageWidth);

        ImageView bannerImageView = (ImageView) findViewById(R.id.list_item_route_banner_image);
        int maxSide = bannerImageView.getWidth() > bannerImageView.getHeight() ? bannerImageView.getWidth() : bannerImageView.getHeight();
        Picasso.with(this)
                .load(urlForPreScaledBanner)
                .fit()
                .centerCrop()
                .into(bannerImageView);
    }
}
