package com.bcs.andy.strayanimalsshelter.database;

import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;

import java.util.List;

public interface MarkersDatabaseListener {

    void onCallBack(List<AnimalMarker> list);
}
