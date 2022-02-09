package it.unimib.travelnotes.ui.attivita;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.travelnotes.Activity_travel_view;
import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.databinding.FragmentAttivitaBinding;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class AttivitaFragment extends Fragment  {

    private static final String TAG = "AttivitaFragment";
    private List<Attivita> attivitaList = new ArrayList<Attivita>();
    private FragmentAttivitaBinding binding;
    private Adapter_attivita adapter_attivita;
    private RecyclerView recyclerView;
    private Activity_travel_view activity_travel_view = new Activity_travel_view();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attivita, container, false);
        recyclerView = view.findViewById(R.id.recycler_attivita);
        adapter_attivita = new Adapter_attivita(attivitaList, AttivitaFragment.this);
        recyclerView.setAdapter(adapter_attivita);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO collegare con database
        attivitaList.add(new Attivita("Degustazione", "degustazione di panini a destra e sul cameriere che poverino sta lavorando da 120 ore di fila"));
        attivitaList.add(new Attivita("Degustazione", "degustazione"));

        for (int i = 0; i < attivitaList.size(); i++) {
            Log.d(TAG, "Gson: " + attivitaList.get(i));
        }





            SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(getContext()) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                    final int position = viewHolder.getAdapterPosition();
                    final Attivita item = attivitaList.get(position);

                    adapter_attivita.removeItem(position);
                    activity_travel_view.onChange(attivitaList);


                   Snackbar snackbar = Snackbar
                           .make(view.findViewById(R.id.recycler_attivita), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapter_attivita.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    });

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

                }
            };

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchhelper.attachToRecyclerView(recyclerView);


        ImageButton button_n_attivita = (ImageButton) view.findViewById(R.id.new_attivita);
        button_n_attivita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewActivityEvent.class);
                startActivity(intent);
            }
        });
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}