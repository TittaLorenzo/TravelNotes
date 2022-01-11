package it.unimib.travelnotes.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import it.unimib.travelnotes.roomdb.TravelDatabase;
import it.unimib.travelnotes.roomdb.relations.UtenteConViaggi;
import it.unimib.travelnotes.roomdb.relations.ViaggioConAttivita;
import it.unimib.travelnotes.roomdb.relations.ViaggioConUtenti;
import it.unimib.travelnotes.roomdb.relations.ViaggioUtenteCrossRef;


public class TravelRepository implements ITravelRepository {

    private static final String REALTIME_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";

    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;

    private final Application mApplication;
    private final MutableLiveData<ListaAttivitaResponse> mListaAttivitaLiveData;
    private final MutableLiveData<ListaUtentiResponse> mListaUtentiLiveData;
    private final MutableLiveData<ListaViaggiResponse> mListaViaggiLiveData;
    private final MutableLiveData<ViaggioResponse> mViaggioLiveData;
    private final MutableLiveData<AttivitaResponse> mAttivitaLiveData;

    public TravelRepository(Application application) {
        this.mApplication = application;
        mDatabase = FirebaseDatabase.getInstance(REALTIME_URL).getReference();
        this.mListaAttivitaLiveData = new MutableLiveData<>();
        this.mListaUtentiLiveData = new MutableLiveData<>();
        this.mListaViaggiLiveData = new MutableLiveData<>();
        this.mViaggioLiveData = new MutableLiveData<>();
        this.mAttivitaLiveData = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
    }


    // richiedi elenco attività con live data
    @Override
    public MutableLiveData<ListaAttivitaResponse> fetchListaAttivita(long viaggioId, boolean refresh) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (refresh) {
                    String viaggioOnlineId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId).getViaggioOnlineId();
                    getListaAttivitaSingleCall(viaggioOnlineId);
                    addListaAttivitaListener(viaggioOnlineId);
                } else {
                    getElencoAttivitaFromDatabase(viaggioId);
                }
            }
        };
        new Thread(runnable).start();

        return mListaAttivitaLiveData;
    }

    private void getElencoAttivitaFromDatabase(long viaggioId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ListaAttivitaResponse listaAttivitaResponse = mListaAttivitaLiveData.getValue();

                if (listaAttivitaResponse == null) {
                    listaAttivitaResponse = new ListaAttivitaResponse();
                }

                if (viaggioId != 0) {
                    ViaggioConAttivita elencoAttivita = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().getViaggioConAttivita(viaggioId);

                    listaAttivitaResponse.setElencoAttivita(elencoAttivita.activities);
                    listaAttivitaResponse.setViaggio(elencoAttivita.viaggio);
                    //listaAttivitaResponse....
                }

                //if (errorMessage != null)

                mListaAttivitaLiveData.postValue(listaAttivitaResponse);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public MutableLiveData<ListaUtentiResponse> fetchGruppoViaggio(long viaggioId, boolean refresh) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (refresh) {
                    String viaggioOnlineId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId).getViaggioOnlineId();
                    getListaUtentiSingleCall(viaggioOnlineId);
                    addListaUtentiListener(viaggioOnlineId);
                } else {
                    getElencoUtentiFromDatabase(viaggioId);
                }
            }
        };
        new Thread(runnable).start();

        return mListaUtentiLiveData;
    }

    private void getElencoUtentiFromDatabase(long viaggioId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                ListaUtentiResponse listaUtentiResponse = mListaUtentiLiveData.getValue();

                if (listaUtentiResponse == null) {
                    listaUtentiResponse = new ListaUtentiResponse();
                }

                if (viaggioId != 0) {
                    ViaggioConUtenti elencoUtenti = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().getViaggioConUtenti(viaggioId);

                    listaUtentiResponse.setElencoUtenti(elencoUtenti.gruppoViaggio);
                    listaUtentiResponse.setViaggio(elencoUtenti.viaggio);
                    //listaUtentiResponse...
                }

                //if (errorMessage != null)

                mListaUtentiLiveData.postValue(listaUtentiResponse);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public MutableLiveData<ListaViaggiResponse> fetchListaViaggi(String utenteId, boolean refresh) {

        if (refresh) {
            getListaViaggiSingleCall(utenteId);
            addListaViaggiListener(utenteId);
        } else {
            getElencoViaggiFromDatabase(utenteId);
        }

        return mListaViaggiLiveData;
    }

    private void getElencoViaggiFromDatabase(String utenteId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

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
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public MutableLiveData<ViaggioResponse> fetchViaggio(long viaggioId, boolean refresh) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (refresh) {
                    String viaggioOnlineId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId).getViaggioOnlineId();
                    getViaggioSingleCall(viaggioOnlineId);
                    addViaggioListener(viaggioOnlineId);
                } else {
                    getViaggioFromDatabase(viaggioId);
                }


            }
        };
        new Thread(runnable).start();

        return mViaggioLiveData;
    }

    private void getViaggioFromDatabase(long viaggioId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                ViaggioResponse viaggioResponse = mViaggioLiveData.getValue();

                if (viaggioResponse == null) {
                    viaggioResponse = new ViaggioResponse();
                }

                Viaggio viaggio = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId);

                viaggioResponse.setViaggio(viaggio);
                //viaggioResponse...

                //if (errorMessage != null)

                mViaggioLiveData.postValue(viaggioResponse);
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public MutableLiveData<AttivitaResponse> fetchAttivita(long attivitaId, boolean refresh) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (refresh) {
                    Attivita a = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().findAttivitaById(attivitaId);
                    String attivitaOnlineId = a.getAttivitaOnlineId();
                    String viaggioOnlineId = a.getViaggioOnlineId();
                    getAttivitaSingleCall(attivitaOnlineId, viaggioOnlineId);
                } else {
                    getAttivitaFromDatabase(attivitaId);
                }
            }
        };
        new Thread(runnable).start();

        return mAttivitaLiveData;
    }

    private void getAttivitaFromDatabase(long attivitaId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                AttivitaResponse attivitaResponse = mAttivitaLiveData.getValue();

                if (attivitaResponse == null) {
                    attivitaResponse = new AttivitaResponse();
                }

                Attivita attivita = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().findAttivitaById(attivitaId);

                attivitaResponse.setAttivita(attivita);
                //attivitaResponse...

                //if (errorMessage != null)

                mAttivitaLiveData.postValue(attivitaResponse);
            }
        };
        new Thread(runnable).start();

    }



    // scritture su DB local/cloud
    @Override
    public void pushNuovoViaggio(Viaggio viaggio, boolean esiste) {

        //scrittura su cloud
        String parentViaggio = viaggio.getViaggioOnlineId();
        if (!esiste) {
            parentViaggio = mDatabase.child("viaggi").push().getKey();
            viaggio.setViaggioOnlineId(parentViaggio);
        }
        if (parentViaggio != null) {

        }
        mDatabase.child("viaggi").child(parentViaggio).child("dettagliviaggio").setValue(viaggio)
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

        mDatabase.child("utenti").child(mAuth.getUid()).child("listaviaggi").child(parentViaggio).setValue(viaggio)
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
        String finalParentViaggio = parentViaggio;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(esiste) {
                    TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().aggiornaViaggio(viaggio);
                } else {
                    long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().nuovoViaggio(viaggio);
                }

                long viaggioId;
                if (esiste) {
                    Viaggio v = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioByOnlineId(finalParentViaggio);
                    viaggioId = v.getViaggioId();
                } else {
                    viaggioId = viaggio.getViaggioId();
                }
                ViaggioUtenteCrossRef crossRef = new ViaggioUtenteCrossRef();
                crossRef.utenteId = mAuth.getUid();
                crossRef.viaggioId = viaggioId;
                TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().insertViaggioUtenteCrossRef(crossRef);
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void pushNuovaAttivita(Attivita attivita, boolean esiste) {

        //scrittura su db locale
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                //scrittura su cloud
                String parentViaggio = attivita.getAttivitaOnlineId();
                String parentAttivita = attivita.getAttivitaOnlineId();
                if (!esiste) {
                    // Serve l'OnlineId
                    //parentViaggio = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(attivita.getViaggioId()).getViaggioOnlineId();
                    /* Fittizio */ parentViaggio = mDatabase.child("viaggi").push().getKey();
                    attivita.setViaggioOnlineId(parentViaggio);

                    parentAttivita = mDatabase.child("viaggi").child(parentViaggio).child("listaattivita").push().getKey();
                    attivita.setAttivitaOnlineId(parentAttivita);
                }

                mDatabase.child("viaggi").child(parentViaggio).child("listaattivita").child(parentAttivita).setValue(attivita)
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



                if (esiste) {
                    TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().aggiornaAttivita(attivita);
                } else {
                    long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().nuovaAttivita(attivita);
                }
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void pushAggiungiAlGruppo(String email, long viaggioId) {
        //email-userid
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Viaggio viaggio = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId);

                mDatabase.child("email-utente").child(email.trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {

                            Utente utente = (Utente) task.getResult().getValue();

                            mDatabase.child("utenti").child(utente.getUtenteId()).child("listaviaggi").child(viaggio.getViaggioOnlineId()).setValue(viaggio)
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

                            mDatabase.child("viaggi").child(viaggio.getViaggioOnlineId()).child("listautenti").child(utente.getUtenteId()).setValue(utente)
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


            }
        };
        new Thread(runnable).start();


    }

    @Override
    public void pushNuovoUtente(Utente utente)  {

        //scrittura su cloud
        mDatabase.child("utenti").child(utente.getUtenteId()).child("datiutente").setValue(utente)
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
        mDatabase.child("email-utente").child(utente.getEmail()).setValue(utente)
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().nuovoUtente(utente);
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void loadUtente(String utenteId) {

        mDatabase.child("utenti").child(String.valueOf(utenteId)).child("datiutente").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {

                    Utente utente = task.getResult().getValue(Utente.class);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().nuovoUtente(utente);
                        }
                    };
                    new Thread(runnable).start();
                }
            }
        });

    }



    // single call Realtime Database
    private void getViaggioSingleCall(String viaggioOnlineId) {

        mDatabase.child("viaggi").child(viaggioOnlineId).child("dettagliviaggio").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Viaggio viaggio = task.getResult().getValue(Viaggio.class);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().nuovoViaggio(viaggio);
                        }
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
                }
            }
        });
    }

    private void getAttivitaSingleCall(String attivitaOnlineId, String viaggioOnlineId) {

        mDatabase.child("viaggi").child(viaggioOnlineId).child("listaattivita").child(attivitaOnlineId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Attivita attivita = task.getResult().getValue(Attivita.class);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().nuovaAttivita(attivita);
                        }
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
        mDatabase.child("utenti").child(utenteId).child("listaviaggi").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().addAllViaggi(listaViaggi);
                        }
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
                }
            }
        });
    }

    private void getListaAttivitaSingleCall(String viaggioOnlineId) {

        mDatabase.child("viaggi").child(viaggioOnlineId).child("listaattivita").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().addAllAttivita(listaAttivita);
                        }
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
                }
            }
        });
    }

    private void getListaUtentiSingleCall(String viaggioOnlineId) {

        mDatabase.child("viaggi").child(viaggioOnlineId).child("listautenti").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().addAllUtenti(listaUtenti);
                        }
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
                }
            }
        });
    }



    // listener Realime Database
    private void addListaViaggiListener(String utenteId) {

        ValueEventListener listaViaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaViaggiResponse listaViaggiResponse = new ListaViaggiResponse();
                listaViaggiResponse = snapshot.getValue(ListaViaggiResponse.class);
                if (listaViaggiResponse != null) {
                    mListaViaggiLiveData.postValue(listaViaggiResponse);
                } else {
                    // Viaggio cancellato
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "utenti/" + utenteId + "/listaviaggi").getReference().addValueEventListener(listaViaggiListener);
    }

    private void addListaAttivitaListener(String viaggioOnlineId) {

        ValueEventListener listaAttivitaListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaAttivitaResponse listaAttivitaResponse = new ListaAttivitaResponse();
                listaAttivitaResponse = snapshot.getValue(ListaAttivitaResponse.class);
                if (listaAttivitaResponse != null) {
                    mListaAttivitaLiveData.postValue(listaAttivitaResponse);
                } else {
                    // Attivita cancellata
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "viaggii/" + viaggioOnlineId + "/listaattivita").getReference().addValueEventListener(listaAttivitaListener);
    }

    private void addListaUtentiListener(String viaggioOnlineId) {

        ValueEventListener listaUtentiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ListaUtentiResponse listaUtentiResponse = new ListaUtentiResponse();
                listaUtentiResponse = snapshot.getValue(ListaUtentiResponse.class);
                if (listaUtentiResponse != null) {
                    mListaUtentiLiveData.postValue(listaUtentiResponse);
                } else {
                    // Lista Utente cancellata
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "viaggii/" + viaggioOnlineId + "/listautenti").getReference().addValueEventListener(listaUtentiListener);
    }

    private void addViaggioListener(String viaggioOnlineId) {

        ValueEventListener viaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ViaggioResponse viaggioResponse = new ViaggioResponse();
                viaggioResponse = snapshot.getValue(ViaggioResponse.class);
                if (viaggioResponse != null) {
                    mViaggioLiveData.postValue(viaggioResponse);
                } else {
                    // Viaggio cancellato
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled"/*, databaseError.toException()*/);
            }
        };
        FirebaseDatabase.getInstance(REALTIME_URL + "viaggii/" + viaggioOnlineId + "/dettagliviaggio").getReference().addValueEventListener(viaggiListener);
    }


}
