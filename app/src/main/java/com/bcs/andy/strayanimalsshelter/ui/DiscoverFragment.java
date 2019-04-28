package com.bcs.andy.strayanimalsshelter.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.adapter.AnimalAdapter;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabaseListener;
import com.bcs.andy.strayanimalsshelter.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment implements AnimalAdapter.AnimalAdapterListener {


    private static final String TAG = "DiscoverFragment";

    // Firebase
    private AnimalsDatabase animalsDatabase;

    // UI
    private ConstraintLayout CL;
    private ProgressBar animalsDiscoverProgressBar;
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private SearchView searchView;

    // vars
    private List<Animal> discoverAnimalList = new ArrayList<>();


    public DiscoverFragment() {

    }

    private void initFirebase() {
    }

    private void initUI() {

        animalsDiscoverProgressBar = (ProgressBar) CL.findViewById(R.id.loadingDiscoverAnimalsProgressBar);

        searchView = (SearchView) CL.findViewById(R.id.discoverAnimalsSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // we dont need this method, our search is in RealTime, we have no "submit"
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                animalAdapter.getFilter().filter(newText);
                return false;
            }
        });
        
        recyclerView = (RecyclerView) CL.findViewById(R.id.discoverAnimalsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animalsDatabase = new AnimalsDatabase();
        animalsDatabase.readAllAnimals(new AnimalsDatabaseListener() {
            @Override
            public void onCallback(List<Animal> list) {
                discoverAnimalList.clear();
                discoverAnimalList.addAll(list);
                animalsDiscoverProgressBar.setVisibility(View.GONE);

                animalAdapter = new AnimalAdapter(discoverAnimalList, getActivity(), DiscoverFragment.this);
                recyclerView.setAdapter(animalAdapter);

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    private final ColorDrawable background = new ColorDrawable(Color.parseColor("#72B90E"));

                    // onMove is for drag and drop, not needed here.
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                        return false;
                    }

                    // i = direction
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        int position = viewHolder.getAdapterPosition();
                        Animal animal = animalAdapter.getAnimalAt(position);
                        if (animal.getAdoptionRequest() == null) {
                            animalAdapter.sendAdoptionRequestAnimalAt(position);
                        } else {
                            animalAdapter.impossibleAdoptionRequest(position);
                        }

                        animalAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX / 8, dY, actionState, isCurrentlyActive);

                        View itemView = viewHolder.itemView;
                        // backgroundCornerOffset = used to push the background behind the edge of the parent view so that it appears underneath the rounded corners
                        // The larger the corner radius of your view, the larger backgroundCornerOffset should be.
                        int backgroundCornerOffset = 20;

                        if (dX > 0) { // Swiping to the right
                            background.setBounds(itemView.getLeft(),
                                    itemView.getTop() + 7,
                                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                                    itemView.getBottom() - 7);

                        } else if (dX < 0) { // Swiping to the left
                            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                                    itemView.getTop() + 7,
                                    itemView.getRight(),
                                    itemView.getBottom() - 7);
                        } else { // view is unSwiped
                            background.setBounds(0, 0, 0, 0);
                        }
                        background.draw(c);


                    }
                }).attachToRecyclerView(recyclerView);


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CL = (ConstraintLayout) inflater.inflate(R.layout.fragment_discover, container, false);
        initFirebase();
        initUI();

        return CL;
    }


    @Override
    public void onAnimalClick(int position, Animal animal) {
        Log.d(TAG, "onAnimalClick: clicked an animal in discover");
        Toast.makeText(getActivity(), "clicked pos: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHelpingHandClick(Animal animal) {

    }

}
