package com.bcs.andy.strayanimalsshelter.dialog;

import com.bcs.andy.strayanimalsshelter.model.Animal;

public interface GetAdoptionRequestDialogListener {

    void resolveAdoptionRequest(Animal animal);
    void denyAdoptionRequest(Animal animal);

}
