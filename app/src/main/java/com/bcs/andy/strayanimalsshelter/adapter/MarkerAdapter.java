package com.bcs.andy.strayanimalsshelter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.model.Marker;

import java.util.List;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.ViewHolder> {

    List<Marker> listMarkers;
    private Context context;

    public MarkerAdapter(List<Marker> listMarkers, Context context) {
        this.listMarkers = listMarkers;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_tem_my_markers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Marker marker = listMarkers.get(position);
        viewHolder.markerLocationTV.setText(marker.getLocationName());
        viewHolder.markerLatitudeTV.setText(marker.getLatitude().toString());
        viewHolder.markerLongitudeTV.setText(marker.getLongitude().toString());

        viewHolder.markerDeleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listMarkers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public TextView markerLocationTV;
        public TextView markerLatitudeTV;
        public TextView markerLongitudeTV;
        public ImageView markerDeleteIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            markerLocationTV = (TextView) itemView.findViewById(R.id.markerLocationTV);
            markerLatitudeTV = (TextView) itemView.findViewById(R.id.markerLatitudeTV);
            markerLongitudeTV = (TextView) itemView.findViewById(R.id.markerLongitudeTV);
            markerDeleteIV = (ImageView) itemView.findViewById(R.id.markerDeleteIV);
            cardView = (CardView) itemView.findViewById(R.id.list_item_markers_CardView);
        }
    }
}
