package it.unimib.travelnotes.ui.flight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.NewTravel;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.databinding.FragmentFlightBinding;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class FlightFragment extends Fragment {

    private String viaggioId;
    private Viaggio viaggio;
    private FragmentFlightBinding binding;
    private FlightViewModel mFlightViewModel;

    TextView departures;
    TextView destination;
    TextView departureTime;
    TextView time;
    TextView departure2;
    TextView destination2;
    TextView departureTime2;
    TextView time2;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFlightViewModel = new ViewModelProvider(requireActivity()).get(FlightViewModel.class);


        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getActivity().getApplication());
        viaggioId = sharedPreferencesProvider.getSelectedViaggioId();;
        mFlightViewModel.setViaggioId(viaggioId);

        if (viaggio == null) {
            viaggio = new Viaggio();
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_flight, container, false);
        Button button_modifica_volo= (Button) view.findViewById(R.id.modify_volo);
        button_modifica_volo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewTravel.class);
                intent.putExtra("modifica_viaggio", true);
                startActivity(intent);
            }
        });

        final Observer<ViaggioResponse> observer = new Observer<ViaggioResponse>() {
            @Override
            public void onChanged(ViaggioResponse viaggioResponse) {
                if (viaggioResponse.isError()) {
                    //updateUIForFaliure(viaggioResponse.getStatus());
                }
                if (viaggioResponse.getViaggio() != null) {

                    viaggio = viaggioResponse.getViaggio();

                    // TODO: scrivi dettagli viaggio nei campi
                }
            }
        };
        mFlightViewModel.getViaggio().observe(getViewLifecycleOwner(), observer);


        return view;
        }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}