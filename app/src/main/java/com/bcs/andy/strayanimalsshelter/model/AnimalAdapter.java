package com.bcs.andy.strayanimalsshelter.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    private List<Animal> listAnimals;
    private Context context;

    public AnimalAdapter(List<Animal> listAnimals, Context context) {
        this.listAnimals = listAnimals;
        this.context = context;
    }

    // whenever a ViewHolder is created
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_sheltered_animals, parent, false);
        return new ViewHolder(v);
    }

    // called right after onCreateViewHolder method
    // binds the data to the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal animal = listAnimals.get(position);
        String age = String.valueOf(animal.getAge()) + " yrs";
        holder.textViewAge.setText(age);
        holder.textViewName.setText(animal.getAnimalName());

        if(animal.getObservations().length() > 145) {
            String obs = animal.getObservations().substring(0, 142).concat("...");
            holder.textViewObs.setText(obs);
        }
        else {
            holder.textViewObs.setText(animal.getObservations());
        }


        switch (animal.getSpecies()) {
            case "Dog":
                holder.imageViewSpecies.setImageResource(R.drawable.dog_icon);
                break;
            case "Cat":
                holder.imageViewSpecies.setImageResource(R.drawable.cat_icon);
                break;
            default:
                holder.imageViewSpecies.setImageResource(R.drawable.dog_icon);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You have clicked " + holder.textViewName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public int getItemCount() {
        return listAnimals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public TextView textViewName;
        public TextView textViewObs;
        public TextView textViewAge;
        public ImageView imageViewSpecies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.animalNameTV);
            textViewObs = (TextView) itemView.findViewById(R.id.animalObsTV);
            textViewAge = (TextView) itemView.findViewById(R.id.animalAgeTV);
            imageViewSpecies = (ImageView) itemView.findViewById(R.id.imageViewSpecies);
            cardView = (CardView) itemView.findViewById(R.id.list_item_CardView);
        }
    }


}
