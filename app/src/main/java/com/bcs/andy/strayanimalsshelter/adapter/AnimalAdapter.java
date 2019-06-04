package com.bcs.andy.strayanimalsshelter.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabaseListener;
import com.bcs.andy.strayanimalsshelter.model.AdoptionRequest;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.utils.UUIDGenerator;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "AnimalAdapter";
    private List<Animal> animalList;
    private List<Animal> animalListFull;
    private Context context;
    private AnimalAdapterListener animalAdapterListener;
    private boolean descriptionVisible = true, ageVisible = false, detailsVisible = false;

    public AnimalAdapter(List<Animal> animalList, Context context, AnimalAdapterListener animalAdapterListener) {
        this.animalList = animalList;
        this.context = context;
        this.animalAdapterListener = animalAdapterListener;
        animalListFull = new ArrayList<>(animalList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_animals, parent, false);
        return new ViewHolder(view, animalAdapterListener);
    }

    // called right after onCreateViewHolder method
    // binds the data to the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Animal animal = animalList.get(position);

        viewHolder.textViewName.setText(animal.getAnimalName());
        if (ageVisible) {
            String aproxAge = String.valueOf(animal.getAproxAge()) + " years";
            viewHolder.textViewAproxAge.setText(aproxAge);
            viewHolder.textViewAproxAge.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewAproxAge.setVisibility(View.GONE);
        }

        if (descriptionVisible) {
            viewHolder.textViewObs.setText(animal.getObservations());
            viewHolder.textViewObs.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewObs.setVisibility(View.GONE);
        }

        if (detailsVisible) {
            viewHolder.neutredCheckBox.setChecked(animal.isNeutered());
            viewHolder.adultCheckBox.setChecked(animal.isAdult());

            viewHolder.neutredCheckBox.setVisibility(View.VISIBLE);
            viewHolder.adultCheckBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.neutredCheckBox.setVisibility(View.GONE);
            viewHolder.adultCheckBox.setVisibility(View.GONE);
        }


        switch (animal.getSpecies()) {
            case "dog":
                viewHolder.speciesImageView.setImageResource(R.drawable.dog_icon);
                break;
            case "cat":
                viewHolder.speciesImageView.setImageResource(R.drawable.cat_icon);
                break;
            default:
                viewHolder.speciesImageView.setImageResource(R.drawable.dog_icon);
        }

        if (animal.isAdoptable()) {
            viewHolder.adoptableImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.adoptableImageView.setVisibility(View.INVISIBLE);
        }

        if (animal.getAdoptionRequest() != null) {
            viewHolder.adoptionRequestImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.adoptionRequestImageView.setVisibility(View.INVISIBLE);
        }

        if (animal.getPhotoLink() != null) {
            Picasso.get().load(animal.getPhotoLink()).fit().into(viewHolder.photoImageView);
        }

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.animalAdapterListener.onAnimalClick(viewHolder.getAdapterPosition(), animalList.get(viewHolder.getAdapterPosition()));
            }
        });

        viewHolder.adoptionRequestImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.animalAdapterListener.onHelpingHandClick(animalList.get(viewHolder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public Animal getAnimalAt(int position) {
        return animalList.get(position);
    }

    public void sendAdoptionRequestAnimalAt(int position) {

        Log.d(TAG, "sendAdoptionRequestAnimalAt: Creating AdoptionRequest :" + position);
        Dialog dialog = dialogSendAdoptionRequest(position).create();
        dialog.show();
    }

    public void impossibleAdoptionRequest(int position) {
        Toast.makeText(context, "Impossible AdoptionRequest : " + position, Toast.LENGTH_SHORT).show();
        Dialog dialog = dialogImpossibleAdoptionRequest(position).create();
        dialog.show();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {

        // this method will return the filteredList to the publishResults methods
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Animal> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(animalListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Animal animal : animalListFull) {
                    if (animal.getAnimalName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(animal);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            animalList.clear();
            animalList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName, textViewAproxAge, textViewObs;
        CheckBox neutredCheckBox, adultCheckBox;
        ImageView speciesImageView, photoImageView, adoptableImageView, adoptionRequestImageView;
        RelativeLayout relativeLayout;

        AnimalAdapterListener animalAdapterListener;

        public ViewHolder(@NonNull View itemView, AnimalAdapterListener animalAdapterListener) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.animalNameTV);
            textViewObs = (TextView) itemView.findViewById(R.id.animalObsTV);
            textViewAproxAge = (TextView) itemView.findViewById(R.id.animalAgeTV);
            speciesImageView = (ImageView) itemView.findViewById(R.id.animalIconImageView);
            neutredCheckBox = (CheckBox) itemView.findViewById(R.id.neuteredCheckBox);
            adultCheckBox = (CheckBox) itemView.findViewById(R.id.adultCheckBox);
            cardView = (CardView) itemView.findViewById(R.id.list_item_animals_CardView);
            photoImageView = (ImageView) itemView.findViewById(R.id.animalPhotoImageView);
            adoptableImageView = (ImageView) itemView.findViewById(R.id.animalAdoptableImageView);
            adoptionRequestImageView = (ImageView) itemView.findViewById(R.id.animalAdoptionRequestImageView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_animal_relative_layout);

            this.animalAdapterListener = animalAdapterListener;
        }
    }

    private AlertDialog.Builder dialogSendAdoptionRequest(int position) {

        Animal swipedAnimal = getAnimalAt(position);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle("Adopt " + swipedAnimal.getAnimalName())
                .setMessage("The owner of this animal will be notified that you would like to adopt it. Are you sure ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(context, "PRESSED YES", Toast.LENGTH_SHORT).show();

                        AdoptionRequest adoptionRequest = new AdoptionRequest(UUIDGenerator.createUUID(), UserUtils.getCurrentUserId(), UserUtils.getCurrentUserName(), UserUtils.getCurrentUserEmail());
                        AnimalsDatabase animalsDatabase = new AnimalsDatabase();
                        animalsDatabase.addAdoptionRequestToAnimal(swipedAnimal, swipedAnimal.getUserUid(), adoptionRequest, new AnimalsDatabaseListener() {
                            @Override
                            public void onCallback(List<Animal> list) {
                                // list is null here
                            }
                        });
                    }
                });

        return dialogBuilder;
    }

    private AlertDialog.Builder dialogImpossibleAdoptionRequest(int position) {

        Animal swipedAnimal = getAnimalAt(position);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle("Adopt " + swipedAnimal.getAnimalName())
                .setMessage("You can't ask to adopt this animal, it has already been requested by someone else.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return dialogBuilder;
    }


    // textViewObs,
    // textViewAproxAge,
    // neutredCheckBox, adultCheckBox
    public void makeObservationsVisible() {
        descriptionVisible = true;
        ageVisible = false;
        detailsVisible = false;
    }

    public void makeAgeVisible() {
        descriptionVisible = false;
        ageVisible = true;
        detailsVisible = false;
    }

    public void makeDetailsVisible() {
        descriptionVisible = false;
        ageVisible = false;
        detailsVisible = true;
    }


    public interface AnimalAdapterListener {
        void onAnimalClick(int position, Animal animal);

        void onHelpingHandClick(Animal animal);
    }


}
