package it.unimib.travelnotes.ui.group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.response.ListaUtentiResponse;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.databinding.FragmentGroupBinding;


public class GroupFragment extends Fragment {


    private static final String TAG = "GroupFragment";
    private List<Utente> listaUtenti = new ArrayList<Utente>();
    private UserAdapter UserAdapter;
    private FragmentGroupBinding binding;
    private RecyclerView recyclerView;
    private String viaggioId;
    private ProgressBar mProgressBar;

    private GruppoViaggioViewModel mGruppoViaggioViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mGruppoViaggioViewModel = new ViewModelProvider(requireActivity()).get(GruppoViaggioViewModel.class);

        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getActivity().getApplication());
        viaggioId=sharedPreferencesProvider.getSelectedViaggioId();
        mGruppoViaggioViewModel.setViaggioId(viaggioId);

        View view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.recycler_group);
        UserAdapter = new UserAdapter(listaUtenti, GroupFragment.this);
        recyclerView.setAdapter(UserAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressBar = (ProgressBar) view.findViewById(R.id.flight_progress_i);


        for (int i = 0; i < listaUtenti.size(); i++) {
            Log.d(TAG, "Gson: " + listaUtenti.get(i));
        }


        ImageButton button_add_user = (ImageButton) view.findViewById(R.id.new_user);
        button_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogfragment fragmentdialog=new dialogfragment();
                fragmentdialog.show(getChildFragmentManager(),"custodialog" );

            }
        });

        final Observer<ListaUtentiResponse> observer = new Observer<ListaUtentiResponse>() {
            @Override
            public void onChanged(ListaUtentiResponse listaUtentiResponse) {
                if (listaUtentiResponse.isError()) {
                    //updateUIForFaliure(listaUtentiResponse.getStatus());
                }
                if (listaUtentiResponse.getElencoUtenti() != null) {

                    listaUtenti.clear();
                    listaUtenti.addAll(listaUtentiResponse.getElencoUtenti());
                    UserAdapter.notifyDataSetChanged();

                }
                mProgressBar.setVisibility(View.GONE);
            }
        };
        mGruppoViaggioViewModel.getListaUtenti().observe(getViewLifecycleOwner(), observer);


        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
