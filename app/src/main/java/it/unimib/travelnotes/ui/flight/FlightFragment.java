package it.unimib.travelnotes.ui.flight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import it.unimib.travelnotes.databinding.FragmentFlightBinding;

public class FlightFragment extends Fragment {


    private FragmentFlightBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFlightBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}