package com.martin.vaxjobicycleguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RouteListFragment extends ListFragment{

    ArrayAdapter<Route> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Route");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    ArrayList<Route> routeArray = new ArrayList<Route>(parseObjects.size());
                    for (ParseObject parseRoute: parseObjects) {
                        Route route = new Route();
                        route.name = parseRoute.getString("Name");
                        route.description = parseRoute.getString("Description");
                        route.banner = parseRoute.getString("Banner");
                        route.distance = parseRoute.getString("Distance");
                        route.rating = parseRoute.getInt("Rating");
                        routeArray.add(route);
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

    // TODO: This will be rewritten to a CursorAdapter backed by a ContentProvider
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
            int maxSide = bannerImageView.getWidth() > bannerImageView.getHeight() ? bannerImageView.getWidth() : bannerImageView.getHeight();
            Picasso.with(mContext)
                    .load(route.banner)
                    // https://github.com/square/picasso/issues/226
                    // TODO: Calculate correct values here
                    .fit()
                    //.resize(400, 400) // Center crop is supposed to make this keep the aspect ratio
                    .centerCrop()
                    .into(bannerImageView);

            return rowView;
        }
    }

    private class Route {
        public String banner;
        public String description;
        public String name;
        public String distance;
        public int rating;
    }
}
