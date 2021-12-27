package it.unimib.travelnotes.repository;

import android.app.Application;
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


import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.roomdb.TravelDatabase;


public class TravelRepository implements ITravelRepository {

    public static final int FRESH_TIMEOUT = 60*1000;
    private static final String REALTIME_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";

    private DatabaseReference mDatabase;

    private final Application mApplication;
    private final ResponseCallback responseCallback;
    private final AResponseCallback aResponseCallback;
    private final UResponseCallback uResponseCallback;

    public TravelRepository(Application application, ResponseCallback responseCallback, AResponseCallback aResponseCallback, UResponseCallback uResponseCallback) {
        this.mApplication = application;
        this.responseCallback = responseCallback;
        this.aResponseCallback = aResponseCallback;
        this.uResponseCallback = uResponseCallback;
        mDatabase = FirebaseDatabase.getInstance(REALTIME_URL).getReference();
    }

    @Override
    public void fetchViaggio(Long viaggioId, long lastUpdate) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            getViaggio(viaggioId, lastUpdate);
        } else {
            readViaggioFromDatabase(viaggioId, lastUpdate);
        }

        ValueEventListener viaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Viaggio viaggio = dataSnapshot.getValue(Viaggio.class);
                // TODO: updateUI
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // TODO: updateUI
            }
        };
        mDatabase.addValueEventListener(viaggiListener);

    }

    @Override
    public void pushViaggio(Viaggio viaggio) {

        //scrittura su cloud
        mDatabase.child("viaggi").child(viaggio.getViaggioId()).setValue(viaggio)
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().nuovoViaggio(viaggio);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void fetchAttivita(Long attivitaId, long lastUpdate) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            getAttivita(attivitaId, lastUpdate);
        } else {
            readAttivitaFromDatabase(attivitaId, lastUpdate);
        }

        ValueEventListener attivitaListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Attivita attivita = dataSnapshot.getValue(Attivita.class);
                // TODO: updateUI
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // TODO: updateUI
            }
        };
        mDatabase.addValueEventListener(attivitaListener);

    }

    @Override
    public void pushAttivita(Attivita attivita) {

        //scrittura su cloud
        mDatabase.child("attivita").child(attivita.getAttivitaId()).setValue(attivita)
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().nuovaAttivita(attivita);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void fetchUtente(Long utenteId, long lastUpdate) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            getUtente(utenteId, lastUpdate);
        } else {
            readUtenteFromDatabase(utenteId, lastUpdate);
        }

        ValueEventListener utenteListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Utente utente = dataSnapshot.getValue(Utente.class);
                // TODO: updateUI
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // TODO: updateUI
            }
        };
        mDatabase.addValueEventListener(utenteListener);

    }

    @Override
    public void pushUtente(Utente utente) {

        //scrittura su cloud
        mDatabase.child("utenti").child(utente.getUtenteId()).setValue(utente)
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().nuovoUtente(utente);
            }
        };
        new Thread(runnable).start();

    }

    private void getViaggio(Long viaggioId, long lastUpdate) {
        mDatabase.child("viaggio").child(viaggioId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Viaggio viaggio = (Viaggio) task.getResult().getValue();
                    if (viaggio != null) {
                        responseCallback.onResponse(viaggio, lastUpdate);
                    } else {
                        responseCallback.onFaliure("Lettura fallita");
                    }
                }
            }
        });
    }

    private void readViaggioFromDatabase(Long viaggioId, long lastUpdate) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Viaggio viaggio = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getViaggioDao().findViaggioById(viaggioId);

                if (viaggio != null) {
                    responseCallback.onResponse(viaggio, lastUpdate);
                } else {
                    responseCallback.onFaliure("Lettura fallita");
                }

            }
        };
        new Thread(runnable).start();

    }

    private void getAttivita(Long attivitaId, long lastUpdate) {
        mDatabase.child("attivita").child(attivitaId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Attivita attivita = (Attivita) task.getResult().getValue();
                    if (attivita != null) {
                        aResponseCallback.onResponse(attivita, lastUpdate);
                    } else {
                        aResponseCallback.onFaliure("Lettura fallita");
                    }
                }
            }
        });
    }

    private void readAttivitaFromDatabase(Long attivitaId, long lastUpdate) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Attivita attivita = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getAttivitaDao().findAttivitaById(attivitaId);

                if (attivita != null) {
                    aResponseCallback.onResponse(attivita, lastUpdate);
                } else {
                    aResponseCallback.onFaliure("Lettura fallita");
                }
            }
        };
        new Thread(runnable).start();

    }

    private void getUtente(Long utenteId, long lastUpdate) {
        mDatabase.child("utente").child(utenteId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Utente utente = (Utente) task.getResult().getValue();
                    if (utente != null) {
                        uResponseCallback.onResponse(utente, lastUpdate);
                    } else {
                        uResponseCallback.onFaliure("Lettura fallita");
                    }
                }
            }
        });
    }

    private void readUtenteFromDatabase(Long utenteId, long lastUpdate) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Utente utente = TravelDatabase.getDatabase(mApplication.getApplicationContext()).getUtenteDao().findUtenteById(utenteId);

                if (utente != null) {
                    uResponseCallback.onResponse(utente, lastUpdate);
                } else {
                    uResponseCallback.onFaliure("Lettura fallita");
                }
            }
        };
        new Thread(runnable).start();

    }



}
