package it.unimib.travelnotes.ui.newactivityevent;

import static it.unimib.travelnotes.roomdb.TravelDatabase.getDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import it.unimib.travelnotes.Activity_travel_view;
import it.unimib.travelnotes.MainActivity;
import it.unimib.travelnotes.Model.Attivita;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;
import it.unimib.travelnotes.roomdb.TravelDatabase;
import it.unimib.travelnotes.roomdb.relations.ViaggioConAttivita;

public class NewActivityEvent extends AppCompatActivity {

    private static final String REALTIME_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private NewActivityEventViewModel mNewActivityEventViewModel;
    private ITravelRepository mITravelRepository;
    private Attivita attivita;

    private Button dataInizioAttivitaButton;
    private Button dataFineAttivitaButton;
    private Button oraInizioNuovaAttivita;
    private Button oraFineAttivitaButton;
    private Button buttonSalva;

    private EditText campoNome;
    private EditText campoPosizione;
    private EditText campoDescrizione;

    private String attivitaId;
    private String viaggioId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(REALTIME_URL).getReference();
        mITravelRepository = new TravelRepository(getApplication());

        dataInizioAttivitaButton = findViewById(R.id.dataInizioNuovaAttivita);
        dataFineAttivitaButton = findViewById(R.id.dataFineNuovaAttivita);
        oraInizioNuovaAttivita = findViewById(R.id.oraInizioNuovaAttivita);
        oraFineAttivitaButton = findViewById(R.id.oraFineNuovaAttivita);

        campoNome = findViewById(R.id.nomeAttivitaInput);
        campoPosizione = findViewById(R.id.posizionePartenzaNuovaAttivita);
        campoDescrizione = findViewById(R.id.descrizioneNuovaAttivita);
        buttonSalva = findViewById(R.id.salvaBottoneNuovaAttivita);

        Button dataInizioAttivitaButton = findViewById(R.id.dataInizioNuovaAttivita);
        Button dataFineAttivitaButton = findViewById(R.id.dataFineNuovaAttivita);
        Button oraInizioNuovaAttivita = findViewById(R.id.oraInizioNuovaAttivita);
        Button oraFineAttivitaButton = findViewById(R.id.oraFineNuovaAttivita);
        ImageButton backButtonNuovaAttivita = (ImageButton) findViewById(R.id.backButtonNuovaAttivita);

        dataInizioAttivitaButton.setOnClickListener(v -> {
            showDatePickerDialog(dataInizioAttivitaButton);
        });

        dataFineAttivitaButton.setOnClickListener(v -> {
            showDatePickerDialog(dataFineAttivitaButton);
        });

        oraInizioNuovaAttivita.setOnClickListener(v -> {
            showTimePickerDialog(oraInizioNuovaAttivita);
        });

        oraFineAttivitaButton.setOnClickListener(v -> {
            showTimePickerDialog(oraFineAttivitaButton);
        });

        backButtonNuovaAttivita.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_travel_view.class);
            startActivity(intent);
        });

        buttonSalva.setOnClickListener(c -> {

            if (TextUtils.isEmpty(campoNome.getText().toString())) {
                Toast.makeText(this, "Devi inserire un nome attività", Toast.LENGTH_SHORT).show();
            } else {
                salvaButtonNuovaAttivita();
            }

        });


        // Leggo valori passati come intent extra: se ci sono valori non nulli allora è una modifica a una attività, quindi bisogna
        try {
            attivitaId = (String) getIntent().getExtras().get("attivitaId");
        } catch (Exception e) {
            attivitaId = null;
        }
        try {
            viaggioId = (String) getIntent().getExtras().get("viaggioId");
        } catch (Exception e) {
            viaggioId = null;
        }
        //fittizio
        viaggioId = "-MtA7mKtdZODJR98_3hH";

        if (attivitaId != null) {
            caricaDatiAttivita(attivitaId);
        }

        /*Button buttonMaps = findViewById(R.id.buttonMaps);
        buttonMaps.setOnClickListener(v -> {
            apriMappe(campoPosizione.getText().toString());
        });*/

        mNewActivityEventViewModel = new ViewModelProvider(this).get(NewActivityEventViewModel.class);

        if (attivitaId != null) {
            mNewActivityEventViewModel.setAttivitaId(attivitaId);
        }
        if (viaggioId != null) {
            mNewActivityEventViewModel.setViaggioId(viaggioId);
        }

        if (attivita == null) {
            attivita = new Attivita();
        }


    }

    private void caricaDatiAttivita(String idAttivitaI) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Attivita attivitaSelezionata = TravelDatabase.getDatabase(getApplicationContext()).getAttivitaDao().findAttivitaById(idAttivitaI);

                TextView titoloNuovaAttivita = findViewById(R.id.titloloNuovaAttivitaId);
                titoloNuovaAttivita.setText(R.string.titleModificaAttivita);

                EditText nomeAttivitaInput = (EditText) findViewById(R.id.nomeAttivitaInput);
                nomeAttivitaInput.setText(attivitaSelezionata.getNome());

                EditText posizionePartenzaNuovaAttivita = (EditText) findViewById(R.id.posizionePartenzaNuovaAttivita);
                posizionePartenzaNuovaAttivita.setText(attivitaSelezionata.getPosizione());

                EditText descrizioneNuovaAttivita = (EditText) findViewById(R.id.descrizioneNuovaAttivita);
                descrizioneNuovaAttivita.setText(attivitaSelezionata.getDescrizione());


                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleHourFormat=new SimpleDateFormat("HH:mm");

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(attivitaSelezionata.getDataInizio());

                Button dataInizioNuovaAttivita = (Button) findViewById(R.id.dataInizioNuovaAttivita);
                dataInizioNuovaAttivita.setText(simpleDateFormat.format(calendar.getTime()));

                Button oraInizioNuovaAttivita = (Button) findViewById(R.id.oraInizioNuovaAttivita);
                oraInizioNuovaAttivita.setText(simpleHourFormat.format(calendar.getTime()));


                calendar.setTime(attivitaSelezionata.getDataFine());

                Button dataFineNuovaAttivita = (Button) findViewById(R.id.dataFineNuovaAttivita);
                dataFineNuovaAttivita.setText(simpleDateFormat.format(calendar.getTime()));

                Button oraFineNuovaAttivita = (Button) findViewById(R.id.oraFineNuovaAttivita);
                oraFineNuovaAttivita.setText(simpleHourFormat.format(calendar.getTime()));

            }
        };
        new Thread(runnable).start();
    }

    public void salvaButtonNuovaAttivita() {

        Date dataInizio = new Date();
        Date dataFine = new Date();
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            dataInizio = formatter.parse(
                    ((Button) findViewById(R.id.dataInizioNuovaAttivita)).getText().toString() +
                            " " + ((Button) findViewById(R.id.oraInizioNuovaAttivita)).getText().toString());

            dataFine = formatter.parse(
                    ((Button) findViewById(R.id.dataFineNuovaAttivita)).getText().toString() +
                            " " + ((Button) findViewById(R.id.oraFineNuovaAttivita)).getText().toString());
        } catch (Exception e) {
            Log.v("MyLog", "parsing date fallito");
        }

        Attivita a = new Attivita();
        a.setNome(campoNome.getText().toString());
        a.setViaggioId(viaggioId);
        a.setPosizione(campoPosizione.getText().toString());
        a.setDescrizione(campoDescrizione.getText().toString());
        a.setDataInizio(dataInizio);
        a.setDataFine(dataFine);

        if (attivitaId != null) {
            a.setAttivitaId(attivitaId);
            a.setViaggioId(viaggioId);

            mITravelRepository.pushNuovaAttivita(a, true);

        } else {

            mITravelRepository.pushNuovaAttivita(a, false);
        }


        Intent i = new Intent(getApplicationContext(), Activity_travel_view.class);
        startActivity(i);

    }


    private void showDatePickerDialog(final Button sceltaDateTime) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd");

                sceltaDateTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(NewActivityEvent.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimePickerDialog(final Button sceltaDateTime) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");

                sceltaDateTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(NewActivityEvent.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
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

                    //delateAll RoomDb

                    startActivity(new Intent(this, MainActivity.class));
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
                                Toast.makeText(NewActivityEvent.this, "La password è stata cambiata", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewActivityEvent.this, "Errore! Password non cambiata", Toast.LENGTH_SHORT).show();
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

            case R.id.RefreshItemMenu:
                //refresh method
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
                        mITravelRepository.pushNuovoUtente(u);
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

    public void apriMappe (String posizione) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("geo:0,0?q=" + Uri.encode(posizione)));

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        }
    }

}