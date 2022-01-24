package it.unimib.travelnotes.ui.flight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import it.unimib.travelnotes.NewTravel;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.databinding.FragmentFlightBinding;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class FlightFragment extends Fragment {


    private FragmentFlightBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFlightBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button button_modifica_volo= (Button) root.findViewById(R.id.modify_volo);
        button_modifica_volo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewTravel.class);
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