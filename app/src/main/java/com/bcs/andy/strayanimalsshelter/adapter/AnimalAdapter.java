package com.bcs.andy.strayanimalsshelter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    private List<Animal> listAnimals;
    private Context context;
    private AnimalAdapterListener animalAdapterListener;

    public AnimalAdapter(List<Animal> listAnimals, Context context, AnimalAdapterListener animalAdapterListener) {
        this.listAnimals = listAnimals;
        this.context = context;
        this.animalAdapterListener = animalAdapterListener;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal animal = listAnimals.get(position);
        String aproxAge = String.valueOf(animal.getAproxAge()) + " yrs";
        holder.textViewAproxAge.setText(aproxAge);
        holder.textViewName.setText(animal.getAnimalName());
        holder.textViewObs.setText(animal.getObservations());
        holder.neutredCheckBox.setChecked(animal.isNeutered());
        holder.adultCheckBox.setChecked(animal.isAdult());

        switch (animal.getSpecies()) {
            case "dog":
                holder.speciesImageView.setImageResource(R.drawable.dog_icon);
                break;
            case "cat":
                holder.speciesImageView.setImageResource(R.drawable.cat_icon);
                break;
            default:
                holder.speciesImageView.setImageResource(R.drawable.dog_icon);
        }

        if(animal.isAdoptable()) {
            holder.adoptableImageView.setVisibility(View.VISIBLE);
        } else {
            holder.adoptableImageView.setVisibility(View.INVISIBLE);
        }

        if (animal.getPhotoLink() != null) {
            Picasso.get().load(animal.getPhotoLink()).fit().into(holder.photoImageView);
        }

    }

    @Override
    public int getItemCount() {
        return listAnimals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView textViewName, textViewObs, textViewAproxAge;
        CheckBox neutredCheckBox, adultCheckBox;
        ImageView speciesImageView, photoImageView, adoptableImageView;

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

            this.animalAdapterListener = animalAdapterListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            animalAdapterListener.onAnimalClick(getAdapterPosition(), listAnimals.get(getAdapterPosition()));
        }
    }

    public interface AnimalAdapterListener {
        void onAnimalClick(int position, Animal animal);
    }


}
