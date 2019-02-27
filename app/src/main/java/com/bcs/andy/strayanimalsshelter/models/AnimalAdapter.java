package com.bcs.andy.strayanimalsshelter.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    private List<Animal> listAnimals;
    private Context context;

    public AnimalAdapter(List<Animal> listAnimals, Context context) {
        this.listAnimals = listAnimals;
        this.context = context;
    }

    // whenever our ViewHolder is created = Whenever an instance of the bottom class ViewHolder is created
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    // called right after above method, this will bind the data to the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal animal = listAnimals.get(position);
        holder.textViewName.setText(animal.getAnimalName());
        holder.textViewObs.setText(animal.getObservations());

    }

    @Override
    public int getItemCount() {
        return listAnimals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewObs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewObs = (TextView) itemView.findViewById(R.id.textViewObs);
        }
    }



}
