package it.unimib.travelnotes.ui.attivita;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import it.unimib.travelnotes.databinding.FragmentAttivitaBinding;

public class AttivitaFragment extends Fragment {

    //private DashboardViewModel dashboardViewModel;
    private FragmentAttivitaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // dashboardViewModel =
        //        new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentAttivitaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}