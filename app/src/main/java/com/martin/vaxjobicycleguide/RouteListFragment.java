package com.martin.vaxjobicycleguide;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class RouteListFragment extends ListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Route");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject route: parseObjects) {
                        Log.d("Växjö Bicycle Guide", route.getString("Name"));
                    }
                } else {
                    // something went wrong
                    Log.d("Växjö Bicycle Guide", e.toString());
                }
            }
        });

        return inflater.inflate(R.layout.fragment_route_list, container, false);
    }
}
