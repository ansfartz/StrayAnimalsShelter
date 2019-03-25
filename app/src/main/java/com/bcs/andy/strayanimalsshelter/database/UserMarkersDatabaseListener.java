package com.bcs.andy.strayanimalsshelter.database;

import com.bcs.andy.strayanimalsshelter.model.Marker;

import java.util.List;

public interface UserMarkersDatabaseListener {

    void onCallBack(List<Marker> list);
}
