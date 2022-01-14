package it.unimib.travelnotes.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.response.AttivitaResponse;
import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.Model.response.ListaAttivitaResponse;
import it.unimib.travelnotes.Model.response.ListaUtentiResponse;
import it.unimib.travelnotes.Model.response.ListaViaggiResponse;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.roomdb.TravelDatabase;
import it.unimib.travelnotes.roomdb.relations.UtenteConViaggi;
import it.unimib.travelnotes.roomdb.relations.ViaggioConAttivita;
import it.unimib.travelnotes.roomdb.relations.ViaggioConUtenti;
import it.unimib.travelnotes.roomdb.relations.ViaggioUtenteCrossRef;


public class TravelRepository implements ITravelRepository {

    private static final String REALTIME_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final int FRESH_TIMEOUT = 60*1000;

    private final Application mApplication;
    private final DatabaseReference mRtDatabase;
    private final SharedPreferencesProvider mSharedPreferencesProvider;

    private final MutableLiveData<ListaAttivitaResponse> mListaAttivitaLiveData;
    private final MutableLiveData<ListaUtentiResponse> mListaUtentiLiveData;
    private final MutableLiveData<ListaViaggiResponse> mListaViaggiLiveData;
    private final MutableLiveData<ViaggioResponse> mViaggioLiveData;
    private final MutableLiveData<AttivitaResponse> mAttivitaLiveData;

    public TravelRepository(Application application) {
        this.mApplication = application;
        mRtDatabase = FirebaseDatabase.getInstance(REALTIME_URL).getReference();
        this.mListaAttivitaLiveData = new MutableLiveData<>();
        this.mListaUtentiLiveData = new MutableLiveData<>();
        this.mListaViaggiLiveData = new MutableLiveData<>();
        this.mViaggioLiveData = new MutableLiveData<>();

        mSharedPreferencesProvider = new SharedPreferencesProvider(mApplication);
        this.mAttivitaLiveData = new MutableLiveData<>();
    }


    // richiedi elenco attività con live data
    @Override
    public MutableLiveData<ListaAttivitaResponse> fetchListaAttivita(String viaggioId) {

        long currentTime = System.currentTimeMillis();
        long lastUpdate = mSharedPreferencesProvider.getLastUpdateListaAttivita();

        if (isOnline() && currentTime - lastUpdate > FRESH_TIMEOUT) {
            getListaAttivitaSingleCall(viaggioId);
        } else {
            getElencoAttivitaFromDatabase(viaggioId);
        }

        return mListaAttivitaLiveData;
    }

    @Override
    public MutableLiveData<ListaUtentiResponse> fetchGruppoViaggio(String viaggioId) {

        long currentTime = System.currentTimeMillis();
        long lastUpdate = mSharedPreferencesProvider.getLastUpdateGruppoViaggio();

        if (isOnline() && currentTime - lastUpdate > FRESH_TIMEOUT) {
            getListaUtentiSingleCall(viaggioId);
        } else {
            getElencoUtentiFromDatabase(viaggioId);
        }

        return mListaUtentiLiveData;
    }

    @Override
    public MutableLiveData<ListaViaggiResponse> fetchListaViaggi(String utenteId) {

        long currentTime = System.currentTimeMillis();
        long lastUpdate = mSharedPreferencesProvider.getLastUpdateListaViaggi();

        if (isOnline() && currentTime - lastUpdate > FRESH_TIMEOUT) {
            getListaViaggiSingleCall(utenteId);
        } else {
            getElencoViaggiFromDatabase(utenteId);
        }

        return mListaViaggiLiveData;
    }

    @Override
    public MutableLiveData<ViaggioResponse> fetchViaggio(String viaggioId) {

        long currentTime = System.currentTimeMillis();
        long lastUpdate = mSharedPreferencesProvider.getLastUpdateViaggio();

        if (isOnline() && currentTime - lastUpdate > FRESH_TIMEOUT) {
            getViaggioSingleCall(viaggioId);
        } else {
            getViaggioFromDatabase(viaggioId);
        }

        return mViaggioLiveData;
    }

    @Override
    public MutableLiveData<AttivitaResponse> fetchAttivita(String attivitaId, String viaggioId) {

        if (isOnline()) {
            getAttivitaSingleCall(attivitaId, viaggioId);
        } else {
            getAttivitaFromDatabase(attivitaId);
        }

        return mAttivitaLiveData;
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

        mRtDatabase.child("utenti").child(mSharedPreferencesProvider.getSharedUserId()).child("listaviaggi").child(parentViaggio).setValue(viaggio)
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
            if(esiste) {
                TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().aggiornaViaggio(viaggio);
            } else {
                long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().nuovoViaggio(viaggio);
            }

            ViaggioUtenteCrossRef crossRef = new ViaggioUtenteCrossRef();
            crossRef.utenteId = mSharedPreferencesProvider.getSharedUserId();
            crossRef.viaggioId = viaggio.getViaggioId();
            TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().insertViaggioUtenteCrossRef(crossRef);
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
                TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().aggiornaAttivita(attivita);
            } else {
                long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().nuovaAttivita(attivita);
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void pushAggiungiAlGruppo(String email, String viaggioId) {
        //email-userid
        Runnable runnable = () -> {

            Viaggio viaggio = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId);

            mRtDatabase.child("email_utente").child(email.trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {

                        Utente utente = (Utente) task.getResult().getValue();

                        mRtDatabase.child("utenti").child(utente.getUtenteId()).child("listaviaggi").child(viaggio.getViaggioId()).setValue(viaggio)
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

                        mRtDatabase.child("viaggi").child(viaggio.getViaggioId()).child("listautenti").child(utente.getUtenteId()).setValue(utente)
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


                    }
                }
            });


        };
        new Thread(runnable).start();

        getListaUtentiSingleCall(viaggioId);

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

        // aggiunta a eleco email-userid
        mRtDatabase.child("email_utente").child(utente.getEmail()).setValue(utente)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(mApplication.getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(mApplication.getApplicationContext(), "Faliure!!", Toast.LENGTH_SHORT).show();
                    }
                });

        //scrittura su db locale
        Runnable runnable = () -> {
            long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().nuovoUtente(utente);
        };
        new Thread(runnable).start();

    }

    @Override
    public void loadUtente(String utenteId) {

        mSharedPreferencesProvider.setSharedUserId(utenteId);

        mRtDatabase.child("utenti").child(utenteId).child("datiutente").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {

                    Utente utente = task.getResult().getValue(Utente.class);

                    Runnable runnable = () -> {
                        long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().nuovoUtente(utente);
                    };
                    new Thread(runnable).start();
                }
            }
        });

    }



    // Get From Database
    private void getElencoAttivitaFromDatabase(String viaggioId) {

        Runnable runnable = () -> {
            ListaAttivitaResponse listaAttivitaResponse = mListaAttivitaLiveData.getValue();

            if (listaAttivitaResponse == null) {
                listaAttivitaResponse = new ListaAttivitaResponse();
            }

            ViaggioConAttivita elencoAttivita = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().getViaggioConAttivita(viaggioId);

            listaAttivitaResponse.setElencoAttivita(elencoAttivita.activities);
            listaAttivitaResponse.setViaggio(elencoAttivita.viaggio);
            //listaAttivitaResponse....

            //if (errorMessage != null)

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

            ViaggioConUtenti elencoUtenti = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().getViaggioConUtenti(viaggioId);

            listaUtentiResponse.setElencoUtenti(elencoUtenti.gruppoViaggio);
            listaUtentiResponse.setViaggio(elencoUtenti.viaggio);
            //listaUtentiResponse...

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

            UtenteConViaggi elencoViaggi = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().getUtenteConViaggi(utenteId);

            listaViaggiResponse.setElencoViaggi(elencoViaggi.viaggi);
            listaViaggiResponse.setUtente(elencoViaggi.utente);
            //listaViaggiResponse...

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

            Viaggio viaggio = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId);

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

            Attivita attivita = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().findAttivitaById(attivitaId);

            attivitaResponse.setAttivita(attivita);
            //attivitaResponse...

            //if (errorMessage != null)

            mAttivitaLiveData.postValue(attivitaResponse);
        };
        new Thread(runnable).start();

    }



    // single call Realtime Database
    private void getViaggioSingleCall(String viaggioId) {

        mRtDatabase.child("viaggi").child(viaggioId).child("dettagliviaggio").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    Viaggio viaggio = task.getResult().getValue(Viaggio.class);

                    Runnable runnable = () -> {
                        long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().nuovoViaggio(viaggio);
                    };
                    new Thread(runnable).start();

                    ViaggioResponse viaggioResponse = mViaggioLiveData.getValue();

                    if (viaggioResponse == null) {
                        viaggioResponse = new ViaggioResponse();
                    }
                    viaggioResponse.setViaggio(viaggio);
                    //viaggioResponse...
                    //if (errorMessage != null)
                    mViaggioLiveData.postValue(viaggioResponse);
                    mSharedPreferencesProvider.setLastUpdateViaggio(System.currentTimeMillis());
                }
            }
        });
    }

    private void getAttivitaSingleCall(String attivitaId, String viaggioId) {

        mRtDatabase.child("viaggi").child(viaggioId).child("listaattivita").child(attivitaId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Attivita attivita = task.getResult().getValue(Attivita.class);

                    Runnable runnable = () -> {
                        long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().nuovaAttivita(attivita);
                    };
                    new Thread(runnable).start();


                    AttivitaResponse attivitaResponse = mAttivitaLiveData.getValue();

                    if (attivitaResponse == null) {
                        attivitaResponse = new AttivitaResponse();
                    }
                    attivitaResponse.setAttivita(attivita);
                    //attivitaResponse...

                    //if (errorMessage != null)

                    mAttivitaLiveData.postValue(attivitaResponse);
                }
            }
        });

    }

    private void getListaViaggiSingleCall(String utenteId) {
        mRtDatabase.child("utenti").child(utenteId).child("listaviaggi").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    List<Viaggio> listaViaggi = new ArrayList<>();
                    int i = 0;
                    for (DataSnapshot parentIdSnapshot: task.getResult().getChildren()) {
                        try {
                            listaViaggi.set(i, parentIdSnapshot.getValue(Viaggio.class));
                        } catch (Exception e) {
                            Log.e("MyLog", String.valueOf(task.getResult()));
                        }
                        i++;
                    }

                    //List<Viaggio> listaViaggi = (List<Viaggio>) task.getResult().getValue();

                    Runnable runnable = () -> {
                        TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().addAllViaggi(listaViaggi);
                    };
                    new Thread(runnable).start();

                    ListaViaggiResponse listaViaggiResponse = mListaViaggiLiveData.getValue();

                    if (listaViaggiResponse == null) {
                        listaViaggiResponse = new ListaViaggiResponse();
                    }
                    listaViaggiResponse.setElencoViaggi(listaViaggi);
                    //viaggioResponse...
                    //if (errorMessage != null)
                    mListaViaggiLiveData.postValue(listaViaggiResponse);
                    mSharedPreferencesProvider.setLastUpdateListaViaggi(System.currentTimeMillis());
                    mSharedPreferencesProvider.setLastUpdateViaggio(System.currentTimeMillis());
                }
            }
        });
    }

    private void getListaAttivitaSingleCall(String viaggioId) {

        mRtDatabase.child("viaggi").child(viaggioId).child("listaattivita").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    List<Attivita> listaAttivita = new ArrayList<>();
                    int i = 0;
                    for (DataSnapshot parentIdSnapshot: task.getResult().getChildren()) {
                        try {
                            listaAttivita.set(i, parentIdSnapshot.getValue(Attivita.class));
                        } catch (Exception e) {
                            Log.e("MyLog", String.valueOf(task.getResult()));
                        }
                        i++;
                    }

                    //List<Attivita> listaAttivita = (List<Attivita>) task.getResult().getValue();

                    Runnable runnable = () -> {
                            TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().addAllAttivita(listaAttivita);
                    };
                    new Thread(runnable).start();

                    ListaAttivitaResponse listaViaggiResponse = mListaAttivitaLiveData.getValue();

                    if (listaViaggiResponse == null) {
                        listaViaggiResponse = new ListaAttivitaResponse();
                    }
                    listaViaggiResponse.setElencoAttivita(listaAttivita);
                    //viaggioResponse...
                    //if (errorMessage != null)
                    mListaAttivitaLiveData.postValue(listaViaggiResponse);
                    mSharedPreferencesProvider.setLastUpdateListaAttivita(System.currentTimeMillis());
                }
            }
        });
    }

    private void getListaUtentiSingleCall(String viaggioId) {

        mRtDatabase.child("viaggi").child(viaggioId).child("listautenti").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    List<Utente> listaUtenti = new ArrayList<>();
                    int i = 0;
                    for (DataSnapshot parentIdSnapshot: task.getResult().getChildren()) {
                        try {
                            listaUtenti.set(i, parentIdSnapshot.getValue(Utente.class));
                        } catch (Exception e) {
                            Log.e("MyLog", String.valueOf(task.getResult()));
                        }
                        i++;
                    }

                    //List<Utente> listaUtenti = (List<Utente>) task.getResult().getValue();

                    Runnable runnable = () -> {
                            TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().addAllUtenti(listaUtenti);
                    };
                    new Thread(runnable).start();

                    ListaUtentiResponse listaAttivitaResponse = mListaUtentiLiveData.getValue();

                    if (listaAttivitaResponse == null) {
                        listaAttivitaResponse = new ListaUtentiResponse();
                    }
                    listaAttivitaResponse.setElencoUtenti(listaUtenti);
                    //viaggioResponse...
                    //if (errorMessage != null)
                    mListaUtentiLiveData.postValue(listaAttivitaResponse);
                    mSharedPreferencesProvider.setLastUpdateGruppoViaggio(System.currentTimeMillis());
                }
            }
        });
    }



    // listener Realime Database
    @Override
    public void addListaViaggiListener(String utenteId) {

        ValueEventListener listaViaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaViaggiResponse listaViaggiResponse = snapshot.getValue(ListaViaggiResponse.class);
                if (listaViaggiResponse != null) {
                    mListaViaggiLiveData.postValue(listaViaggiResponse);
                } else {
                    // Viaggio cancellato
                }
                mSharedPreferencesProvider.setLastUpdateListaViaggi(System.currentTimeMillis());
                mSharedPreferencesProvider.setLastUpdateViaggio(System.currentTimeMillis());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "utenti/" + utenteId + "/listaviaggi").getReference().addValueEventListener(listaViaggiListener);
    }

    @Override
    public void addListaAttivitaListener(String viaggioId) {

        ValueEventListener listaAttivitaListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaAttivitaResponse listaAttivitaResponse = snapshot.getValue(ListaAttivitaResponse.class);
                if (listaAttivitaResponse != null) {
                    mListaAttivitaLiveData.postValue(listaAttivitaResponse);
                } else {
                    // Attivita cancellata
                }
                mSharedPreferencesProvider.setLastUpdateListaAttivita(System.currentTimeMillis());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "viaggi/" + viaggioId + "/listaattivita").getReference().addValueEventListener(listaAttivitaListener);
    }

    @Override
    public void addListaUtentiListener(String viaggioId) {

        ValueEventListener listaUtentiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaUtentiResponse listaUtentiResponse = snapshot.getValue(ListaUtentiResponse.class);
                if (listaUtentiResponse != null) {
                    mListaUtentiLiveData.postValue(listaUtentiResponse);
                } else {
                    // Lista Utente cancellata
                }
                mSharedPreferencesProvider.setLastUpdateGruppoViaggio(System.currentTimeMillis());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "viaggi/" + viaggioId + "/listautenti").getReference().addValueEventListener(listaUtentiListener);
    }

    @Override
    public void addViaggioListener(String viaggioId) {

        ValueEventListener viaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ViaggioResponse viaggioResponse = snapshot.getValue(ViaggioResponse.class);
                if (viaggioResponse != null) {
                    mViaggioLiveData.postValue(viaggioResponse);
                } else {
                    // Viaggio cancellato
                }
                mSharedPreferencesProvider.setLastUpdateViaggio(System.currentTimeMillis());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "viaggi/" + viaggioId + "/dettagliviaggio").getReference().addValueEventListener(viaggiListener);
    }



    // delete
    @Override
    public void deleteViaggio(String viaggioId) {

    }

    @Override
    public void deleteAttivita(String attivitaId) {

    }

    @Override
    public void deleteUtente(String utenteId) {

    }

    @Override
    public void rimuoviDalGruppo(String viaggioId, String utenteId) {

    }

    @Override
    public void deleteAllLocal() {
        //Clear all tables (logout)
    }



    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
