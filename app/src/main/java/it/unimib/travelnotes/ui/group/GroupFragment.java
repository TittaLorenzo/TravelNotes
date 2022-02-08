package it.unimib.travelnotes.ui.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class GroupFragment extends Fragment {


    private RecyclerView recyclerView;

    private UserAdapter UserAdapter;
    private List<Utente> listaUtenti;
    private String viaggioId;

    private GruppoViaggioViewModel mGruppoViaggioViewModel;

    @Override
    public void onResume(){
        super.onResume();
        Button bottone_agg_user= getView().findViewById(R.id.new_user);
        bottone_agg_user.setOnClickListener(v -> {
            add_user_group();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group,container,false);
        mGruppoViaggioViewModel=new ViewModelProvider(requireActivity()).get(GruppoViaggioViewModel.class);
        recyclerView = view.findViewById(R.id.recycler_group);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        listaUtenti= new ArrayList<>();
        UserAdapter = new UserAdapter(getActivity().getApplicationContext(), (ArrayList<Utente>) listaUtenti);
        recyclerView.setAdapter(UserAdapter);

        viaggioId = "-MtZ4XYo_IZa2DZ66eif";
        mGruppoViaggioViewModel.setViaggioId(viaggioId);

        if (listaUtenti == null) {
            listaUtenti = new ArrayList<>();
        }


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
            }
        };
        mGruppoViaggioViewModel.getListaUtenti().observe(getViewLifecycleOwner(), observer);
        return view;
    }
    //inserimento mail per aggiunta utente
    public void add_user_group() {
        EditText Confirm_email = new EditText(getActivity().getApplicationContext());
        AlertDialog.Builder pwLostDialog = new AlertDialog.Builder(getActivity().getApplicationContext());
        pwLostDialog.setTitle("Inserisci Mail Utente");
        pwLostDialog.setMessage("inserire la mail dell' utente da voler aggiungere al gruppo");
        pwLostDialog.setView(Confirm_email);

        pwLostDialog.setPositiveButton("invia", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int wihich) {
                String resEmail = Confirm_email.getText().toString();

                mGruppoViaggioViewModel.aggiungiAlGruppo(resEmail.trim());
            }
        });

        pwLostDialog.setNegativeButton("chiudi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //close dialog
            }
        });
        pwLostDialog.create().show();
    }
}