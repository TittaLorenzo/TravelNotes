package it.unimib.travelnotes.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


public class TravelRepository implements ITravelRepository {

    private static final String REALTIME_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";

    private final DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

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
    public MutableLiveData<ListaAttivitaResponse> fetchListaAttivita(long viaggioId) {

        /*if (lastUpdate - getTime() > TIMEREFRESH) {
            getListaAttivitaSingleCall(viaggioId);
        } else {*/
        getElencoAttivitaFromDatabase(viaggioId);
        //}

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
    public MutableLiveData<ListaUtentiResponse> fetchGruppoViaggio(long viaggioId) {

        /*if (lastUpdate - getTime() > TIMEREFRESH) {
            getListaUtentiSingleCall(viaggioId);
        } else {*/
        getElencoUtentiFromDatabase(viaggioId);
        //}

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
    public MutableLiveData<ListaViaggiResponse> fetchListaViaggi(String utenteId) {

        /*if (lastUpdate - getTime() > TIMEREFRESH) {
            getListaUtentiSingleCall(utenteId);
        } else {*/
            getElencoViaggiFromDatabase(utenteId);
        //}

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
    public MutableLiveData<ViaggioResponse> fetchViaggio(long viaggioId) {

        /*if (lastUpdate - getTime() > TIMEREFRESH) {
            getViaggioSingleCall(viaggioId);
        } else {*/
            getViaggioFromDatabase(viaggioId);
        //}

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
    public MutableLiveData<AttivitaResponse> fetchAttivita(long attivitaId) {

        /*if (lastUpdate - getTime() > TIMEREFRESH) {
            getAttivitaSingleCall(attivitaId);
        } else {*/
            getAttivitaFromDatabase(attivitaId);
        //}

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
        /*mDatabase.child("viaggio").child(viaggio.getViaggioId()).setValue(utente)
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
                });*/

        //scrittura su db locale
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(esiste) {
                    TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().aggiornaViaggio(viaggio);
                } else {
                    long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().nuovoViaggio(viaggio);
                }

            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void pushNuovaAttivita(Attivita attivita, boolean esiste) {

        //scrittura su cloud
        /*mDatabase.child("attivita").child(attivita.getAttivitaId()).setValue(attivita)
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
                });*/

        //scrittura su db locale
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
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
    public void pushNuovoUtente(Utente utente) {

        //scrittura su cloud
        /*mDatabase.child("utenti").child(utente.getUtenteId()).setValue(utente)
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
                });*/

        //scrittura su db locale
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long rowId = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().nuovoUtente(utente);
            }
        };
        new Thread(runnable).start();

    }



    // single call Realtime Database
    @Override
    public void getViaggioSingleCall(long viaggioId) {

        mDatabase.child("viaggio").child(String.valueOf(viaggioId)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Viaggio viaggio = (Viaggio) task.getResult().getValue();

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

    @Override
    public void getAttivitaSingleCall(long attivitaId) {

        mDatabase.child("attivita").child(String.valueOf(attivitaId)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Attivita attivita = (Attivita) task.getResult().getValue();

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

    @Override
    public void loadUtente(String utenteId) {

        mDatabase.child("utente").child(String.valueOf(utenteId)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    Utente utente = (Utente) task.getResult().getValue();

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



    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }*/


}
