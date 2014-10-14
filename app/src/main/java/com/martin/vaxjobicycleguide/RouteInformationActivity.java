package com.martin.vaxjobicycleguide;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class RouteInformationActivity extends Activity {

    public static final String EXTRA_ROUTE = "EXTRA_ROUTE";

    private Route mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_information);

        Bundle bundle = savedInstanceState;
        if (bundle == null)
            bundle = getIntent().getExtras();

        if (bundle != null) {
            this.mRoute = bundle.getParcelable(EXTRA_ROUTE);
        }

    }
}
