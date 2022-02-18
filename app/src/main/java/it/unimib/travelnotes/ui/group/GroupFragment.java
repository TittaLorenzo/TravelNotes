package it.unimib.travelnotes.ui.group;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.response.ListaUtentiResponse;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.databinding.FragmentGroupBinding;
import it.unimib.travelnotes.ui.attivita.SwipeToDeleteCallBack;


public class GroupFragment extends Fragment {


    private static final String TAG = "GroupFragment";
    private List<Utente> listaUtenti = new ArrayList<Utente>();
    private UserAdapter UserAdapter;
    private FragmentGroupBinding binding;
    private RecyclerView recyclerView;
    private String viaggioId;
    private ProgressBar mProgressBar;
    private Boolean d;
    private Utente temp;
    private UserAdapter userAdapter;

    private GruppoViaggioViewModel mGruppoViaggioViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mGruppoViaggioViewModel = new ViewModelProvider(requireActivity()).get(GruppoViaggioViewModel.class);

        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getActivity().getApplication());
        viaggioId=sharedPreferencesProvider.getSelectedViaggioId();
        mGruppoViaggioViewModel.setViaggioId(viaggioId);

        View view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.recycler_group);
        userAdapter = new UserAdapter(listaUtenti, GroupFragment.this);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Utente item = listaUtenti.get(position);
                d=true;
                temp = item;
                userAdapter.removeItem(position);
                Snackbar snackbar = Snackbar
                        .make(view.findViewById(R.id.recycler_group), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d=false;
                        userAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }

                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if(d){
                            mGruppoViaggioViewModel.deleteUserViewModel(item.getUtenteId());
                        }

                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
            }


        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);


        mProgressBar = (ProgressBar) view.findViewById(R.id.flight_progress_i);


        for (int i = 0; i < listaUtenti.size(); i++) {
            Log.d(TAG, "Gson: " + listaUtenti.get(i));
        }


        ImageButton button_add_user = (ImageButton) view.findViewById(R.id.new_user);
        ImageButton button_link = (ImageButton) view.findViewById(R.id.new_user_2);
        button_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogfragment fragmentdialog=new dialogfragment();
                fragmentdialog.show(getChildFragmentManager(),"custodialog" );

            }
        });

        button_link.setOnClickListener(v -> {
            mGruppoViaggioViewModel.createDynamicLink();
            Toast.makeText(getActivity(), "Link copiato negli appunti", Toast.LENGTH_SHORT).show();
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
                    userAdapter.notifyDataSetChanged();

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
