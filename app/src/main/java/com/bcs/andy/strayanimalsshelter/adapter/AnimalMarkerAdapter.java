package com.bcs.andy.strayanimalsshelter.adapter;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.MarkersDatabase;
import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;

import java.util.List;

public class AnimalMarkerAdapter extends RecyclerView.Adapter<AnimalMarkerAdapter.ViewHolder> {

    List<AnimalMarker> listAnimalMarkers;
    private Context context;
    private AnimalMarkerAdapterListener animalMarkerAdapterListener;

    public AnimalMarkerAdapter(List<AnimalMarker> listAnimalMarkers, Context context, AnimalMarkerAdapterListener animalMarkerAdapterListener) {
        this.listAnimalMarkers = listAnimalMarkers;
        this.context = context;
        this.animalMarkerAdapterListener = animalMarkerAdapterListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_markers, parent, false);
        return new ViewHolder(view, animalMarkerAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AnimalMarker animalMarker = listAnimalMarkers.get(position);
        viewHolder.markerLocationTV.setText(animalMarker.getLocation());
        viewHolder.markerLatitudeTV.setText(animalMarker.getLatitude().toString());
        viewHolder.markerLongitudeTV.setText(animalMarker.getLongitude().toString());

        if (animalMarker.getRemovalRequest() != null) {
            viewHolder.markerIconIV.setImageResource(R.drawable.ic_marker_yellow);
            viewHolder.innerRelativeLayout.setBackgroundResource(R.drawable.list_item_marker_gradient_yellow);
            viewHolder.markerWarningIV.setVisibility(View.VISIBLE);
        } else {
            viewHolder.markerIconIV.setImageResource(R.drawable.ic_marker_green);
            viewHolder.innerRelativeLayout.setBackgroundResource(R.drawable.list_item_marker_gradient_blue);
            viewHolder.markerWarningIV.setVisibility(View.INVISIBLE);
        }

        viewHolder.markerDeleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveMarkerDialog(animalMarker);
            }
        });

        viewHolder.markerWarningIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.animalMarkerAdapterListener.onWarningClick(viewHolder.getAdapterPosition(), listAnimalMarkers.get(viewHolder.getAdapterPosition()));
            }
        });

        viewHolder.innerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.animalMarkerAdapterListener.onAnimalMarkerClick(viewHolder.getAdapterPosition(), listAnimalMarkers.get(viewHolder.getAdapterPosition()));
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
        public ImageView markerDeleteIV, markerIconIV, markerWarningIV;
        public RelativeLayout innerRelativeLayout;
        public AnimalMarkerAdapterListener animalMarkerAdapterListener;

        public ViewHolder(@NonNull View itemView, AnimalMarkerAdapterListener animalMarkerAdapterListener) {
            super(itemView);

            markerLocationTV = (TextView) itemView.findViewById(R.id.markerLocationTV);
            markerLatitudeTV = (TextView) itemView.findViewById(R.id.markerLatitudeTV);
            markerLongitudeTV = (TextView) itemView.findViewById(R.id.markerLongitudeTV);
            markerDeleteIV = (ImageView) itemView.findViewById(R.id.markerItemDeleteIV);
            markerIconIV = (ImageView) itemView.findViewById(R.id.markerIconImageView);
            markerWarningIV = (ImageView) itemView.findViewById(R.id.markerItemWarningIV);
            cardView = (CardView) itemView.findViewById(R.id.list_item_markers_CardView);
            innerRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_markers_innerRL);

            this.animalMarkerAdapterListener = animalMarkerAdapterListener;
        }
    }

    private void showRemoveMarkerDialog(AnimalMarker animalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete marker");
        builder.setMessage("Deleting this marker will permanently remove it from all users.\nAre you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MarkersDatabase markersDatabase = new MarkersDatabase();
                markersDatabase.destroyMarker(animalMarker);
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

    public interface AnimalMarkerAdapterListener {
        void onAnimalMarkerClick(int position, AnimalMarker animalMarker);
        void onWarningClick(int position, AnimalMarker animalMarker);
    }


}
