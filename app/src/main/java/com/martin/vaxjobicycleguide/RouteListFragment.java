package com.martin.vaxjobicycleguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteListFragment extends ListFragment{

    ArrayAdapter<Route> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Route");
        // Means we will be called twice, once for the cache and once for the network
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.whereEqualTo("Active", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    ArrayList<Route> routeArray = new ArrayList<Route>(parseObjects.size());
                    for (ParseObject parseRoute: parseObjects) {
                        routeArray.add(new Route(parseRoute));
                    }

                    mAdapter = new RouteArrayAdapter(getActivity(), R.layout.list_item_route, routeArray);
                    setListAdapter(mAdapter);
                } else {
                    // something went wrong
                    Log.d("Växjö Bicycle Guide", e.toString());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_list, container, false);
    }

    private class RouteArrayAdapter extends ArrayAdapter<Route> {
        private final Context mContext;

        public RouteArrayAdapter(Context context, int resource, List<Route> objects) {
            super(context, resource, objects);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.list_item_route, parent, false);
            }

            Route route = getItem(position);

            TextView nameTextView = (TextView) rowView.findViewById(R.id.list_item_route_name);
            nameTextView.setText(route.name);

            TextView difficultyTextView = (TextView) rowView.findViewById(R.id.list_item_route_difficulty);
            difficultyTextView.setText(Integer.toString(route.rating));

            TextView lengthTextView = (TextView) rowView.findViewById(R.id.list_item_route_length);
            lengthTextView.setText(route.distance);

            ImageView bannerImageView = (ImageView) rowView.findViewById(R.id.list_item_route_banner_image);
            Picasso.with(mContext)
                    .load(route.banner)
                    .fit()
                    .centerCrop()
                    .into(bannerImageView);

            return rowView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Route route = mAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), RouteInformationActivity.class);
        intent.putExtra(RouteInformationActivity.EXTRA_ROUTE, route);
        startActivity(intent);
    }
}
