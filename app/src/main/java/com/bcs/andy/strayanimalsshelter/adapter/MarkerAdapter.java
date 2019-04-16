package com.bcs.andy.strayanimalsshelter.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.MarkersDatabase;
import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;

import java.util.List;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.ViewHolder> {

    List<AnimalMarker> listAnimalMarkers;
    private Context context;

    public MarkerAdapter(List<AnimalMarker> listAnimalMarkers, Context context) {
        this.listAnimalMarkers = listAnimalMarkers;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_markers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AnimalMarker animalMarker = listAnimalMarkers.get(position);
        viewHolder.markerLocationTV.setText(animalMarker.getLocation());
        viewHolder.markerLatitudeTV.setText(animalMarker.getLatitude().toString());
        viewHolder.markerLongitudeTV.setText(animalMarker.getLongitude().toString());

        if (animalMarker.getRemovalRequest() != null) {
            viewHolder.markerIconIV.setImageResource(R.drawable.ic_marker_yellow);
            viewHolder.markerWarninigIV.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.markerIconIV.setImageResource(R.drawable.ic_marker_green);
            viewHolder.markerWarninigIV.setVisibility(View.INVISIBLE);
        }

        viewHolder.markerDeleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(animalMarker);
            }
        });

        viewHolder.markerWarninigIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listAnimalMarkers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public TextView markerLocationTV;
        public TextView markerLatitudeTV;
        public TextView markerLongitudeTV;
        public ImageView markerDeleteIV, markerIconIV, markerWarninigIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            markerLocationTV = (TextView) itemView.findViewById(R.id.markerLocationTV);
            markerLatitudeTV = (TextView) itemView.findViewById(R.id.markerLatitudeTV);
            markerLongitudeTV = (TextView) itemView.findViewById(R.id.markerLongitudeTV);
            markerDeleteIV = (ImageView) itemView.findViewById(R.id.markerItemDeleteIV);
            markerIconIV = (ImageView) itemView.findViewById(R.id.markerIconImageView);
            markerWarninigIV = (ImageView) itemView.findViewById(R.id.markerItemWarningIV);
            cardView = (CardView) itemView.findViewById(R.id.list_item_markers_CardView);
        }
    }

    private void showDialog(AnimalMarker animalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete marker");
        builder.setMessage("Deleting this marker will permanently remove it from all user maps.\nAre you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                MarkersDatabase markersDatabase = new MarkersDatabase();
                markersDatabase.removeMarker(animalMarker);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }



}
