package it.unimib.travelnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.Model.response.ListaViaggiResponse;
import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;
import it.unimib.travelnotes.ui.attivita.SwipeToDeleteCallBack;

public class TravelList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TravelListViewModel mTravelListViewModel;
    private String utenteId;
    private ProgressBar mProgressBar;
    private boolean d;
    private Viaggio temp;

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Viaggio> list;

    public static final String FIREBASE_DATABASE_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        utenteId = sharedPreferencesProvider.getSharedUserId();
        mTravelListViewModel = new ViewModelProvider(this).get(TravelListViewModel.class);
        mTravelListViewModel.setUtenteId(utenteId);
        mProgressBar = (ProgressBar) findViewById(R.id.travellist_progress_i);

        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("travel");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Viaggio item = list.get(position);
                d=true;
                temp = item;
                myAdapter.removeTravel(position);
                //activity_travel_view.onChange(attivitaList);
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.recyclerView), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d=false;
                        myAdapter.restoreTravel(item, position);
                        recyclerView.scrollToPosition(position);
                    }

                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if(d){
                            mTravelListViewModel.deleteViaggioViewModel(item.getViaggioId(),mAuth.getUid());
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

        if (list == null) {
            list = new ArrayList<>();
        }

        listaViaggiListenerProva(utenteId);

        //list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        final FloatingActionButton mButtonNext = findViewById(R.id.new_travel);
        mButtonNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTravel.class);
            intent.putExtra("modifica_viaggio", false);
            startActivity(intent);
        });

        final Observer<ListaViaggiResponse> observer = new Observer<ListaViaggiResponse>() {
            @Override
            public void onChanged(ListaViaggiResponse listaViaggiResponse) {
                if (listaViaggiResponse.isError()) {
                    //updateUIForFaliure(listaViaggiResponse.getStatus());
                }
                if (listaViaggiResponse.getElencoViaggi() != null) {

                    list.clear();
                    list.addAll(listaViaggiResponse.getElencoViaggi());

                    myAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
            }
        };
        mTravelListViewModel.getlistaViaggi().observe(this, observer);

        mProgressBar.setVisibility(View.VISIBLE);

    }


    public void listaViaggiListenerProva(String utenteId) {

        ValueEventListener listaViaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    Viaggio viaggio = postSnapshot.getValue(Viaggio.class);

                    list.add(viaggio);

                }
                //Ordinare lista viaggi
                /*
                Collections.sort(list, new Comparator<Viaggio>() {
                    @Override
                    public int compare(Viaggio o1, Viaggio o2) {
                        return o1.getDataAndata().compareTo(o2.getDataAndata());
                    }
                });*/

                Collections.sort(list, new Comparator<Viaggio>() {
                    @Override
                    public int compare(Viaggio o1, Viaggio o2) {
                        return o1.getDataAndata().compareTo(o2.getDataAndata());
                    }
                });
                

                myAdapter = new MyAdapter(TravelList.this,list);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference().child("utenti").child(utenteId).child("listaviaggi").addValueEventListener(listaViaggiListener);
    }




    // Oprions Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutItemMenu:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Logout effettuato", Toast.LENGTH_SHORT).show();

                    SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
                    sharedPreferencesProvider.setSharedUserId(null);

                    mTravelListViewModel.delateAll();

                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    Toast.makeText(this, "Nessun utente loggato", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.changePwItemMenu:
                EditText newPassword = new EditText(this);
                AlertDialog.Builder changePwDialog = new AlertDialog.Builder(this);
                changePwDialog.setTitle("Cambia password?");
                changePwDialog.setMessage("Inserisci la tua nuova password.");
                changePwDialog.setView(newPassword);

                changePwDialog.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newPw = newPassword.getText().toString();
                        mAuth.getCurrentUser().updatePassword(newPw).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(TravelList.this, "La password ?? stata cambiata", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TravelList.this, "Errore! Password non cambiata", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                changePwDialog.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close Dialog
                    }
                });
                changePwDialog.create().show();

                break;

            case R.id.chUsernamePwItemMenu:
                EditText newUsername = new EditText(this);
                AlertDialog.Builder changeUnDialog = new AlertDialog.Builder(this);
                changeUnDialog.setTitle("Cambia username?");
                changeUnDialog.setMessage("Inserisci il tuo nuovo username.");
                changeUnDialog.setView(newUsername);

                changeUnDialog.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUn = newUsername.getText().toString();

                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newUn)
                                .build();
                        assert user != null;
                        user.updateProfile((profileUpdates));

                        Utente u = new Utente(user.getUid(), user.getEmail(), newUn);
                        ITravelRepository travelRepository = new TravelRepository(getApplication());
                        travelRepository.pushNuovoUtente(u);
                    }
                });
                changeUnDialog.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close Dialog
                    }
                });
                changeUnDialog.create().show();

                break;
        }
        return true;
    }

}


