package com.martin.vaxjobicycleguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

public class MapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, container, false);

        WebView webview = (WebView) view.findViewById(R.id.webview_map);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setSupportZoom(false);
        //webview.getSettings().setGeolocationEnabled(true); //TODO: Kräver att man implementerar en callback  onGeolocationPermissionsShowPrompt

        webview.loadUrl("http://vaxjobicycleguide.se/map.html?lat=56.869993547084384&lng=14.801158905029297&zoom=12&id=null");
        return view;
    }
}
