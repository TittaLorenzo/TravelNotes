package it.unimib.travelnotes.ui.attivita;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.travelnotes.Activity_travel_view;
import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.NewActivityEvent;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.databinding.FragmentAttivitaBinding;
import it.unimib.travelnotes.databinding.FragmentFlightBinding;

public class AttivitaFragment extends Fragment {

    private Attivita[] mAttivitaArray;
    private List<Attivita> mAttivitaList;
    private FragmentAttivitaBinding binding;
   

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAttivitaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button button_n_attivita = (Button) root.findViewById(R.id.new_attivita);
        button_n_attivita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewActivityEvent.class);
                startActivity(intent);
            }
        });

        /*final Button attivitaUno = getView().findViewById(R.id.button_nuova_attivita);
        attivitaUno.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), NewActivityEvent.class);
            i.putExtra("idAttivita", 1);
            startActivity(i);
        });*/




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}