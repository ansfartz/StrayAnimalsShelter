package com.bcs.andy.strayanimalsshelter.dialog;

import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;
import com.bcs.andy.strayanimalsshelter.model.RemovalRequest;

public interface GetRemovalRequestDialogListener {
    void resolveRemovalRequest(AnimalMarker animalMarker, RemovalRequest removalRequest);
    void denyRemovalRequest(AnimalMarker animalMarker);
}
