package com.bcs.andy.strayanimalsshelter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context context;

    public MarkerInfoWindowAdapter(Context context) {
        this.context = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);
    }

    private void renderWindowText(Marker marker, View view) {
        String title = marker.getTitle();
        String snippet = marker.getSnippet();
        TextView titleTextView = (TextView) view.findViewById(R.id.miwLocation);
        TextView snippetTextView = (TextView) view.findViewById(R.id.miwAnimalName);

        if(!title.equals("")) {
            titleTextView.setText(title);
        }
        if(!snippet.equals("")) {
            snippetTextView.setText(snippet);
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
