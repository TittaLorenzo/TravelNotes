package it.unimib.travelnotes.ui.attivita;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.travelnotes.Activity_travel_view;
import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.Model.response.ListaAttivitaResponse;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.TravelList;
import it.unimib.travelnotes.databinding.FragmentAttivitaBinding;

public class AttivitaFragment extends Fragment  {

    private static final String TAG = "AttivitaFragment";
    private List<Attivita> attivitaList;
    private FragmentAttivitaBinding binding;
    private Adapter_attivita adapter_attivita;
    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private Activity_travel_view activity_travel_view = new Activity_travel_view();
    private String viaggio_id;
    private Viaggio viaggio;
    private ListaAttivitaViewModel mListaAttivitaViewModel;
    private boolean d;
    private Attivita temp;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_travel_view activity_travel_view = (Activity_travel_view) getActivity();
        mListaAttivitaViewModel = new ViewModelProvider(requireActivity()).get(ListaAttivitaViewModel.class);

        try{
            viaggio_id = (String) getActivity().getIntent().getExtras().get("viaggioId");
        }catch (Exception e){
            viaggio_id = null;
        }
        if(viaggio_id == null){
            Intent intent = new Intent(getActivity().getApplicationContext(), TravelList.class);
        }

        mListaAttivitaViewModel.setViaggioId(viaggio_id);

        if (attivitaList == null) {
            attivitaList = new ArrayList<>();
        }



    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attivita, container, false);
        recyclerView = view.findViewById(R.id.recycler_attivita);
        adapter_attivita = new Adapter_attivita(attivitaList, AttivitaFragment.this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.lattivita_progress_i);
        recyclerView.setAdapter(adapter_attivita);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(getContext()) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    final int position = viewHolder.getAdapterPosition();
                    final Attivita item = attivitaList.get(position);
                    d=true;
                    temp = item;
                    adapter_attivita.removeItem(position);
                    activity_travel_view.onChange(attivitaList);
                   Snackbar snackbar = Snackbar
                           .make(view.findViewById(R.id.recycler_attivita), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d=false;
                            adapter_attivita.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }

                    });

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                    snackbar.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if(d){
                                mListaAttivitaViewModel.deleteAttivitaViewModel(item.getAttivitaId());
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





        final Observer<ListaAttivitaResponse> observer = new Observer<ListaAttivitaResponse>() {
            @Override
            public void onChanged(ListaAttivitaResponse listaAttivitaResponse) {
                if (listaAttivitaResponse.isError()) {
                    //updateUIForFaliure(listaAttivitaResponse.getStatus());
                }
                if (listaAttivitaResponse.getElencoAttivita() != null  &&
                        listaAttivitaResponse.getTotalResults() != -1) {
                    //mListaAttivitaViewModel.setTotalResult(listaAttivitaResponse.getTotalResults());

                    // updateUIForSuccess(listaAttivitaResponse.getElencoAttivita(), listaAttivitaResponse.getViaggio(), listaAttivitaResponse.isLoading());

                    attivitaList.clear();
                    attivitaList.addAll(listaAttivitaResponse.getElencoAttivita());


                    adapter_attivita.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        };
        mListaAttivitaViewModel.getlistaAttivita().observe(getViewLifecycleOwner(), observer);
        mProgressBar.setVisibility(View.VISIBLE);

        return view;
    }









    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}