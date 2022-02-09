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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.unimib.travelnotes.Activity_travel_view;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.NewTravel;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.databinding.FragmentFlightBinding;

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

    String pattern = "MM/dd/yyyy HH:mm:ss";
    DateFormat df = new SimpleDateFormat(pattern);



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_travel_view activity_travel_view = (Activity_travel_view) getActivity();
        mFlightViewModel = new ViewModelProvider(requireActivity()).get(FlightViewModel.class);

        viaggioId =activity_travel_view.getDatiViaggio() ;


        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getActivity().getApplication());
        mFlightViewModel.setViaggioId(viaggioId);

        if (viaggio == null) {
            viaggio = new Viaggio();
        }


    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_flight, container, false);

        departures = view.findViewById(R.id.departure);
        destination = view.findViewById(R.id.destination);
        departureTime = view.findViewById(R.id.departureTime);
        time = view.findViewById(R.id.time);
        departure2 = view.findViewById(R.id.departure2);
        destination2 = view.findViewById(R.id.destination2);
        departureTime2 = view.findViewById(R.id.departureTime2);
        time2 = view.findViewById(R.id.time2);

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
                    departures.setText(viaggio.getPartenzaAndata());
                    destination.setText(viaggio.getDestinazioneAndata());
                    String ora = Double.toString(viaggio.getDurataAndata())+ " ore";
                    time.setText(ora);
                    departure2.setText(viaggio.getPartenzaRitorno());
                    destination2.setText(viaggio.getDestinazioneRitorno());
                    String ora2 = Double.toString(viaggio.getDurataRitorno()) + " ore";
                    time2.setText(ora2);
                    Date dataAndata = viaggio.getDataAndata();
                    String stringaDataAndata = df.format(dataAndata);
                    departureTime.setText(stringaDataAndata);
                    Date dataRitorno = viaggio.getDataRitorno();
                    String stringaDataRitorno = df.format(dataRitorno);
                    departureTime2.setText(stringaDataRitorno);


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