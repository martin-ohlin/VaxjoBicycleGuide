package com.martin.vaxjobicycleguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
        final View view = inflater.inflate(R.layout.fragment_route_list, container, false);

        ListView listview = (ListView) view.findViewById(android.R.id.list);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int currentFirstVisibleItem = view.getFirstVisiblePosition();

                if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                    getActivity().getActionBar().hide();
                } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                    getActivity().getActionBar().show();
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });

        // We can only get the height of the actionbar after the layout has been performed
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // TODO: There are some rumours out there saying that the headers must be added before the
                // adapter is set on the listview. Investigate and act on this.

                TextView headerView = new TextView(getActivity());
                // For some reason there is an empty space between the header and the first element.
                // Even if header dividers are disabled. So remove the extra divider space.
                // https://code.google.com/p/android/issues/detail?id=28701
                // http://stackoverflow.com/questions/10119132/empty-space-between-listview-header-and-first-item
                int height = getActivity().getActionBar().getHeight() - getListView().getDividerHeight();
                headerView.setHeight(height);
                Log.d("Actionbar height", Integer.toString(getActivity().getActionBar().getHeight()));
                headerView.setText("Something");
                headerView.setPadding(0,0,0,0);

                getListView().addHeaderView(headerView);
            }
        });

        return view;
    }

    // TODO: This will be rewritten to a CursorAdapter backed by a ContentProvider
    private class RouteArrayAdapter extends ArrayAdapter<Route> {
        private final Context mContext;
        private final int imageWidth;

        public RouteArrayAdapter(Context context, int resource, List<Route> objects) {
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
                rowView = inflater.inflate(R.layout.list_item_route, parent, false);
            }

            Route route = getItem(position);

            TextView nameTextView = (TextView) rowView.findViewById(R.id.list_item_route_name);
            nameTextView.setText(route.name);

            TextView difficultyTextView = (TextView) rowView.findViewById(R.id.list_item_route_difficulty);
            difficultyTextView.setText(Integer.toString(route.rating));

            TextView lengthTextView = (TextView) rowView.findViewById(R.id.list_item_route_length);
            lengthTextView.setText(route.distance);

            String urlForPreScaledBanner = String.format("http://www.vaxjobicycleguide.se/php/timthumb.php?src=%1$s&w=%2$d", route.banner, imageWidth);

            ImageView bannerImageView = (ImageView) rowView.findViewById(R.id.list_item_route_banner_image);
            int maxSide = bannerImageView.getWidth() > bannerImageView.getHeight() ? bannerImageView.getWidth() : bannerImageView.getHeight();
            Picasso.with(mContext)
                    .load(urlForPreScaledBanner)
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
