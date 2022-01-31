package it.unimib.travelnotes.ui.attivita;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.MyAdapter;
import it.unimib.travelnotes.databinding.FragmentAttivitaBinding;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class AttivitaFragment extends Fragment  {

    private static final String TAG = "AttivitaFragment";
    private Attivita[] attivitaArray ;
    private List<Attivita> AttivitaList;
    private FragmentAttivitaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAttivitaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_attivita);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //TODO collegare con database
        attivitaArray = new Attivita[]{
                new Attivita("Degustazione", "degustazione nel culo con scappellamento a destra e salto mortale sulle palle del cameriere che poverino sta lavorando da 120 ore di fila"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
                new Attivita("Degustazione", "degustazione nel culo"),
        };

        for (int i = 0; i < attivitaArray.length; i++) {
            Log.d(TAG, "Gson: " + attivitaArray[i]);
        }

        Adapter_attivita adapter_Attivita = new Adapter_attivita(attivitaArray, AttivitaFragment.this);
        recyclerView.setAdapter(adapter_Attivita);

        Button button_n_attivita = (Button) root.findViewById(R.id.new_attivita);
        button_n_attivita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewActivityEvent.class);
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}