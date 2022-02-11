package it.unimib.travelnotes.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.travelnotes.Activity_travel_view;
import it.unimib.travelnotes.MainActivity;
import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.response.AttivitaResponse;
import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.Model.response.ListaAttivitaResponse;
import it.unimib.travelnotes.Model.response.ListaUtentiResponse;
import it.unimib.travelnotes.Model.response.ListaViaggiResponse;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.roomdb.TravelDatabase;
import it.unimib.travelnotes.roomdb.relations.UtenteConViaggi;
import it.unimib.travelnotes.roomdb.relations.ViaggioConAttivita;
import it.unimib.travelnotes.roomdb.relations.ViaggioConUtenti;
import it.unimib.travelnotes.roomdb.relations.ViaggioUtenteCrossRef;


public class TravelRepository implements ITravelRepository {

    private static final String REALTIME_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";

    private final Application mApplication;
    private final DatabaseReference mRtDatabase;
    private final TravelDatabase mLocalDatabase;
    private final SharedPreferencesProvider mSharedPreferencesProvider;

    private final MutableLiveData<ListaAttivitaResponse> mListaAttivitaLiveData;
    private final MutableLiveData<ListaUtentiResponse> mListaUtentiLiveData;
    private final MutableLiveData<ListaViaggiResponse> mListaViaggiLiveData;
    private final MutableLiveData<ViaggioResponse> mViaggioLiveData;
    private final MutableLiveData<AttivitaResponse> mAttivitaLiveData;

    public TravelRepository(Application application) {
        this.mApplication = application;
        mRtDatabase = FirebaseDatabase.getInstance(REALTIME_URL).getReference();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mLocalDatabase = TravelDatabase.getDatabase(mApplication.getApplicationContext());
        this.mListaAttivitaLiveData = new MutableLiveData<>();
        this.mListaUtentiLiveData = new MutableLiveData<>();
        this.mListaViaggiLiveData = new MutableLiveData<>();
        this.mViaggioLiveData = new MutableLiveData<>();
        mSharedPreferencesProvider = new SharedPreferencesProvider(mApplication);
        this.mAttivitaLiveData = new MutableLiveData<>();
    }


    // richiedi elenco attività con live data
    @Override
    public MutableLiveData<ListaViaggiResponse> fetchListaViaggi(String utenteId) {

        if (isOnline()) {
            addListaViaggiListener(utenteId);
        } else {
            getElencoViaggiFromDatabase(utenteId);
        }

        return mListaViaggiLiveData;
    }

    @Override
    public MutableLiveData<ListaAttivitaResponse> fetchListaAttivita(String viaggioId) {

        if (isOnline()) {
            addListaAttivitaListener(viaggioId);
        } else {
            getElencoAttivitaFromDatabase(viaggioId);
        }

        return mListaAttivitaLiveData;
    }

    @Override
    public MutableLiveData<ListaUtentiResponse> fetchGruppoViaggio(String viaggioId) {

        /*long currentTime = System.currentTimeMillis();
        long lastUpdate = mSharedPreferencesProvider.getLastUpdateGruppoViaggio();*/

        if (isOnline() /*&& currentTime - lastUpdate > FRESH_TIMEOUT*/) {
            addListaUtentiListener(viaggioId);
        } else {
            getElencoUtentiFromDatabase(viaggioId);
        }

        return mListaUtentiLiveData;
    }

    @Override
    public MutableLiveData<ViaggioResponse> fetchViaggio(String viaggioId) {

        if (isOnline()) {
            addViaggioListener(viaggioId);
        } else {
            getViaggioFromDatabase(viaggioId);
        }

        return mViaggioLiveData;
    }

    @Override
    public MutableLiveData<AttivitaResponse> fetchAttivita(String attivitaId, String viaggioId) {

        /*if (isOnline()) {
            // getAttivitaSingleCall(attivitaId, viaggioId);
        } else {
            getAttivitaFromDatabase(attivitaId);
        }*/
        getAttivitaFromDatabase(attivitaId);

        return mAttivitaLiveData;
    }

    @Override
    public void loadUtente(String utenteId) {

        mRtDatabase.child("utenti").child(utenteId).child("datiutente").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {

                    Utente utente = task.getResult().getValue(Utente.class);

                    Runnable runnable = () -> {
                        mLocalDatabase.getUtenteDao().nuovoUtente(utente);
                    };
                    new Thread(runnable).start();
                }
            }
        });

    }



    // scritture su DB local/cloud
    @Override
    public void pushNuovoViaggio(Viaggio viaggio, boolean esiste) {

        //scrittura su cloud
        String parentViaggio = viaggio.getViaggioId();
        if (!esiste || parentViaggio == null || parentViaggio.equals("")) {
            parentViaggio = mRtDatabase.child("viaggi").push().getKey();
            viaggio.setViaggioId(parentViaggio);
        }

        mRtDatabase.child("viaggi").child(parentViaggio).child("dettagliviaggio").setValue(viaggio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(mApplication.getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mApplication.getApplicationContext(), "Faliure!!", Toast.LENGTH_SHORT).show();
                    }
                });

        mRtDatabase.child("utenti").child(mSharedPreferencesProvider.getSharedUserId()).child("listaviaggi").child(parentViaggio).setValue(viaggio);

        //scrittura su db locale
        String finalParentViaggio = parentViaggio;
        Runnable runnable = () -> {
            Utente utente = mLocalDatabase.getUtenteDao()
                    .findUtenteById(mSharedPreferencesProvider.getSharedUserId());

            mRtDatabase.child("viaggi").child(finalParentViaggio).child("listautenti").child(utente.getUtenteId()).setValue(utente);


            if(esiste) {
                mLocalDatabase.getViaggioDao().aggiornaViaggio(viaggio);
            } else {
                mLocalDatabase.getViaggioDao().nuovoViaggio(viaggio);
            }

            ViaggioUtenteCrossRef crossRef = new ViaggioUtenteCrossRef();
            crossRef.utenteId = mSharedPreferencesProvider.getSharedUserId();
            crossRef.viaggioId = viaggio.getViaggioId();
            mLocalDatabase.getViaggioDao().insertViaggioUtenteCrossRef(crossRef);
        };
        new Thread(runnable).start();

    }

    @Override
    public void pushNuovaAttivita(Attivita attivita, boolean esiste) {

        //scrittura su cloud
        String parentViaggio = attivita.getViaggioId();
        String parentAttivita = attivita.getAttivitaId();
        if (!esiste) {
            /* Fittizio  parentViaggio = mDatabase.child("viaggi").push().getKey();*/
            /* Fittizio  attivita.setViaggioId(parentViaggio);*/

            parentAttivita = mRtDatabase.child("viaggi").child(parentViaggio).child("listaattivita").push().getKey();
            attivita.setAttivitaId(parentAttivita);
        }

        mRtDatabase.child("viaggi").child(parentViaggio).child("listaattivita").child(parentAttivita).setValue(attivita)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(mApplication.getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mApplication.getApplicationContext(), "Faliure!!", Toast.LENGTH_SHORT).show();
                    }
                });

        //scrittura su db locale
        Runnable runnable = () -> {

            if (esiste) {
                mLocalDatabase.getAttivitaDao().aggiornaAttivita(attivita);
            } else {
                mLocalDatabase.getAttivitaDao().nuovaAttivita(attivita);
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void pushAggiungiAlGruppo(String email, String viaggioId) {
        //email-userid
        Runnable runnable = () -> {

            Viaggio viaggio = mLocalDatabase.getViaggioDao().findViaggioById(viaggioId);

            ValueEventListener findUtentiListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String userIdAdd = "errore";
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                        userIdAdd = postSnapshot.getKey();
                    }

                    if (userIdAdd != null) {
                        mRtDatabase.child("utenti").child(userIdAdd).child("datiutente").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {

                                    Utente utente = task.getResult().getValue(Utente.class);

                                    assert utente != null;
                                    mRtDatabase.child("utenti").child(utente.getUtenteId()).child("listaviaggi").child(viaggio.getViaggioId()).setValue(viaggio)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    //Toast.makeText(mApplication.getApplicationContext(), "Aggiunto al gruppo", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(mApplication.getApplicationContext(), "Faliure!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    mRtDatabase.child("viaggi").child(viaggio.getViaggioId()).child("listautenti").child(utente.getUtenteId()).setValue(utente)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(mApplication.getApplicationContext(), "Aggiunto al gruppo", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(mApplication.getApplicationContext(), "Faliure!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mRtDatabase.child("utenti").orderByChild("datiutente/email").equalTo(email).limitToFirst(1)
                    .addValueEventListener(findUtentiListener);
            mRtDatabase.child("utenti").orderByChild("datiutente/email").equalTo(email).limitToFirst(1)
                    .removeEventListener(findUtentiListener);
        };
        new Thread(runnable).start();
    }

    @Override
    public void pushNuovoUtente(Utente utente)  {

        mSharedPreferencesProvider.setSharedUserId(utente.getUtenteId());

        //scrittura su cloud
        mRtDatabase.child("utenti").child(utente.getUtenteId()).child("datiutente").setValue(utente)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(mApplication.getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mApplication.getApplicationContext(), "Faliure!!", Toast.LENGTH_SHORT).show();
                    }
                });

        //scrittura su db locale
        Runnable runnable = () -> {
            mLocalDatabase.getUtenteDao().nuovoUtente(utente);
        };
        new Thread(runnable).start();

    }


    // Get From Database
    private void getElencoAttivitaFromDatabase(String viaggioId) {

        Runnable runnable = () -> {
            ListaAttivitaResponse listaAttivitaResponse = mListaAttivitaLiveData.getValue();

            if (listaAttivitaResponse == null) {
                listaAttivitaResponse = new ListaAttivitaResponse();
            }

            ViaggioConAttivita elencoAttivita = mLocalDatabase.getAttivitaDao().getViaggioConAttivita(viaggioId);

            //listaAttivitaResponse.setElencoAttivita(elencoAttivita.activities);
            listaAttivitaResponse.setViaggio(elencoAttivita.viaggio);
            //listaAttivitaResponse....

            List<Attivita> la = elencoAttivita.activities;
            //if (errorMessage != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                la.sort((d1,d2) -> d1.getDataInizio().compareTo(d2.getDataInizio()));
            }
            listaAttivitaResponse.setElencoAttivita(la);

            mListaAttivitaLiveData.postValue(listaAttivitaResponse);
        };
        new Thread(runnable).start();
    }

    private void getElencoUtentiFromDatabase(String viaggioId) {

        Runnable runnable = () -> {

            ListaUtentiResponse listaUtentiResponse = mListaUtentiLiveData.getValue();

            if (listaUtentiResponse == null) {
                listaUtentiResponse = new ListaUtentiResponse();
            }

            ViaggioConUtenti elencoUtenti = mLocalDatabase.getViaggioDao().getViaggioConUtenti(viaggioId);

            //listaUtentiResponse.setElencoUtenti(elencoUtenti.gruppoViaggio);
            listaUtentiResponse.setViaggio(elencoUtenti.viaggio);
            //listaUtentiResponse...

            List<Utente> lu = elencoUtenti.gruppoViaggio;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lu.sort((d1,d2) -> d1.getEmail().compareTo(d2.getEmail()));
            }
            listaUtentiResponse.setElencoUtenti(lu);

            //if (errorMessage != null)

            mListaUtentiLiveData.postValue(listaUtentiResponse);
        };
        new Thread(runnable).start();
    }

    private void getElencoViaggiFromDatabase(String utenteId) {
        Runnable runnable = () -> {

            ListaViaggiResponse listaViaggiResponse = mListaViaggiLiveData.getValue();

            if (listaViaggiResponse == null) {
                listaViaggiResponse = new ListaViaggiResponse();
            }

            UtenteConViaggi elencoViaggi = mLocalDatabase.getUtenteDao().getUtenteConViaggi(utenteId);

            //listaViaggiResponse.setElencoViaggi(elencoViaggi.viaggi);
            listaViaggiResponse.setUtente(elencoViaggi.utente);
            //listaViaggiResponse...

            List<Viaggio> lv = elencoViaggi.viaggi;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lv.sort((d1,d2) -> d1.getDataAndata().compareTo(d2.getDataAndata()));
            }
            listaViaggiResponse.setElencoViaggi(lv);

            //if (errorMessage != null)

            mListaViaggiLiveData.postValue(listaViaggiResponse);
        };
        new Thread(runnable).start();
    }

    private void getViaggioFromDatabase(String viaggioId) {

        Runnable runnable = () -> {

            ViaggioResponse viaggioResponse = mViaggioLiveData.getValue();

            if (viaggioResponse == null) {
                viaggioResponse = new ViaggioResponse();
            }

            Viaggio viaggio = mLocalDatabase.getViaggioDao().findViaggioById(viaggioId);

            viaggioResponse.setViaggio(viaggio);
            //viaggioResponse...

            //if (errorMessage != null)

            mViaggioLiveData.postValue(viaggioResponse);
        };
        new Thread(runnable).start();

    }

    private void getAttivitaFromDatabase(String attivitaId) {

        Runnable runnable = () -> {

            AttivitaResponse attivitaResponse = mAttivitaLiveData.getValue();

            if (attivitaResponse == null) {
                attivitaResponse = new AttivitaResponse();
            }

            Attivita attivita = mLocalDatabase.getAttivitaDao().findAttivitaById(attivitaId);

            attivitaResponse.setAttivita(attivita);
            //attivitaResponse...

            //if (errorMessage != null)

            mAttivitaLiveData.postValue(attivitaResponse);
        };
        new Thread(runnable).start();

    }



    // listener Realime Database
    public void addListaViaggiListener(String utenteId) {

        ValueEventListener listaViaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaViaggiResponse listaViaggiResponse = new ListaViaggiResponse();
                List<Viaggio> listaViaggi = new ArrayList<Viaggio>(){};

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    Viaggio viaggio = postSnapshot.getValue(Viaggio.class);

                    listaViaggi.add(viaggio);
                }
                listaViaggiResponse.setElencoViaggi(listaViaggi);

                mListaViaggiLiveData.postValue(listaViaggiResponse);

                Runnable runnable = () -> {
                    mLocalDatabase.getViaggioDao().addAllViaggi(listaViaggi);
                };
                new Thread(runnable).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyLog", "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        mRtDatabase.child("utenti").child(utenteId).child("listaviaggi").orderByChild("dataAndata/time").addValueEventListener(listaViaggiListener);

    }

    public void addListaAttivitaListener(String viaggioId) {

        ValueEventListener listaAttivitaListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaAttivitaResponse listaAttivitaResponse = new ListaAttivitaResponse();
                List<Attivita> listaAttivita = new ArrayList<Attivita>(){};

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    Attivita attivita = postSnapshot.getValue(Attivita.class);

                    listaAttivita.add(attivita);
                }
                listaAttivitaResponse.setElencoAttivita(listaAttivita);

                mListaAttivitaLiveData.postValue(listaAttivitaResponse);

                Runnable runnable = () -> {
                    mLocalDatabase.getAttivitaDao().addAllAttivita(listaAttivita);
                };
                new Thread(runnable).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyLog", "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        Query orderAttivitaByDate = mRtDatabase.child("viaggi").child(viaggioId).child("listaattivita").orderByChild("dataInizio/time");
        orderAttivitaByDate.addValueEventListener(listaAttivitaListener);
    }

    public void addListaUtentiListener(String viaggioId) {

        ValueEventListener listaUtentiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaUtentiResponse listaUtentiResponse = new ListaUtentiResponse();
                List<Utente> listaUtenti = new ArrayList<Utente>(){};

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    Utente utente = postSnapshot.getValue(Utente.class);

                    listaUtenti.add(utente);
                }
                listaUtentiResponse.setElencoUtenti(listaUtenti);

                mListaUtentiLiveData.postValue(listaUtentiResponse);

                Runnable runnable = () -> {
                    mLocalDatabase.getUtenteDao().addAllUtenti(listaUtenti);
                };
                new Thread(runnable).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyLog", "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        Query orderUtenteByDate = mRtDatabase.child("viaggi").child(viaggioId).child("listautenti").orderByChild("email");
        orderUtenteByDate.addValueEventListener(listaUtentiListener);
    }

    public void addViaggioListener(String viaggioId) {

        ValueEventListener viaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ViaggioResponse viaggioResponse = new ViaggioResponse();

                Viaggio viaggio = snapshot.getValue(Viaggio.class);

                viaggioResponse.setViaggio(viaggio);

                mViaggioLiveData.postValue(viaggioResponse);

                Runnable runnable = () -> {
                    mLocalDatabase.getViaggioDao().nuovoViaggio(viaggio);
                };
                new Thread(runnable).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyLog", "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        mRtDatabase.child("viaggi").child(viaggioId).child("dettagliviaggio").addValueEventListener(viaggiListener);
    }



    // delete
    @Override
    public void deleteViaggio(String viaggioId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Utente> listaUtenti = mLocalDatabase.getViaggioDao().getViaggioConUtenti(viaggioId).gruppoViaggio;

                for (Utente utente : listaUtenti) {
                    mRtDatabase.child("utenti").child(utente.getUtenteId()).child("listaviaggi").child(viaggioId).removeValue();
                }
            }
        };
        new Thread(runnable).start();

        mRtDatabase.child("viaggi").child(viaggioId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        mLocalDatabase.getViaggioDao().deleteViaggioById(viaggioId);
                    }
                };
                new Thread(runnable).start();
            }
        });
    }

    @Override
    public void deleteAttivita(String attivitaId, String viaggioId) {

        mRtDatabase.child("viaggi").child(viaggioId).child("listaattivita").child(attivitaId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Runnable runnable = () -> {
                    mLocalDatabase.getAttivitaDao().deleteAttivitaById(attivitaId);
                };
                new Thread(runnable).start();
            }
        });

    }

    @Override
    public void deleteUtente(String utenteId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Viaggio> listaViaggi = mLocalDatabase.getUtenteDao().getUtenteConViaggi(utenteId).viaggi;

                for (Viaggio viaggio : listaViaggi) {
                    mRtDatabase.child("viaggi").child(viaggio.getViaggioId()).child("listautenti").child(utenteId).removeValue();
                }
            }
        };
        new Thread(runnable).start();

        mRtDatabase.child("utenti").child(utenteId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            mApplication.startActivity(new Intent(mApplication.getApplicationContext(), MainActivity.class));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void rimuoviDalGruppo(String viaggioId, String utenteId) {
        mRtDatabase.child("utenti").child(utenteId).child("listaviaggi").child(viaggioId).removeValue();

        mRtDatabase.child("viaggi").child(viaggioId).child("listautenti").child(utenteId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Runnable runnable = () -> {
                            mLocalDatabase.getViaggioDao().togliDalGruppo(viaggioId, utenteId);
                        };
                        new Thread(runnable).start();
                    }
                });

    }

    @Override
    public void deleteAllLocal() {
        //Clear all tables (logout)
        Runnable runnable = () -> {
            mLocalDatabase.clearAllTables();
        };
        new Thread(runnable).start();
    }



    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
