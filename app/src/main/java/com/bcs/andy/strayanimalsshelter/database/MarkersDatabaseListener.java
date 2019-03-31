package com.bcs.andy.strayanimalsshelter.database;

import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;

import java.util.List;

public interface MarkersDatabaseListener {

    // readCurrentUserMarkers
    void onCurrentUserMarkersCallBack(List<AnimalMarker> list);

    // readAllMarkers
    void onAllMarkersCallBack(List<AnimalMarker> list);
}
