package it.unimib.travelnotes.ui.newactivityevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.unimib.travelnotes.Activity_travel_view;
import it.unimib.travelnotes.MainActivity;
import it.unimib.travelnotes.Model.Attivita;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;
import it.unimib.travelnotes.roomdb.TravelDatabase;

public class NewActivityEvent extends AppCompatActivity {

    private String AUTOCOMPLETE_API_KEY = "AIzaSyAmaveq8N5RXhsJhELqQWYP-coB78I89NQ";

    private FirebaseAuth mAuth;

    private ITravelRepository mITravelRepository;

    private Button dataInizioAttivitaButton;
    private Button dataFineAttivitaButton;
    private Button oraInizioAttivitaButton;
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
        mITravelRepository = new TravelRepository(getApplication());

        dataInizioAttivitaButton = findViewById(R.id.dataInizioNuovaAttivita);
        dataFineAttivitaButton = findViewById(R.id.dataFineNuovaAttivita);
        oraInizioAttivitaButton = findViewById(R.id.oraInizioNuovaAttivita);
        oraFineAttivitaButton = findViewById(R.id.oraFineNuovaAttivita);

        campoNome = findViewById(R.id.nomeAttivitaInput);
        campoPosizione = findViewById(R.id.posizionePartenzaNuovaAttivita);
        campoDescrizione = findViewById(R.id.descrizioneNuovaAttivita);
        buttonSalva = findViewById(R.id.salvaBottoneNuovaAttivita);

        ImageButton backButtonNuovaAttivita = (ImageButton) findViewById(R.id.backButtonNuovaAttivita);


        // Autocomplete Place API
        // Initialize the SDK
        Places.initialize(getApplicationContext(), AUTOCOMPLETE_API_KEY);

        campoPosizione.setFocusable(false);
        campoPosizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS/*, Place.Type.TRAIN_STATION, Place.Type.AIRPORT, Place.Field.NAME*/);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(NewActivityEvent.this);
                startActivityForResult(intent, 100);

            }
        });

        dataInizioAttivitaButton.setOnClickListener(v -> {
            showDatePickerDialog("StartDate");
        });

        dataFineAttivitaButton.setOnClickListener(v -> {
            showDatePickerDialog("EndDate");
        });

        oraInizioAttivitaButton.setOnClickListener(v -> {
            showTimePickerDialog("StartTime");
        });

        oraFineAttivitaButton.setOnClickListener(v -> {
            showTimePickerDialog("EndTime");
        });

        backButtonNuovaAttivita.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_travel_view.class);
            startActivity(intent);
        });

        buttonSalva.setOnClickListener(c -> {

            /*if (TextUtils.isEmpty(campoNome.getText().toString())) {
                Toast.makeText(this, "Devi inserire un nome attività", Toast.LENGTH_SHORT).show();
            } else {
                salvaButtonNuovaAttivita();
            }*/
            if (!checkCampi()) {
                Toast.makeText(this, "Devi compilare tutti i campi", Toast.LENGTH_SHORT).show();
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
            SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
            viaggioId = sharedPreferencesProvider.getSelectedViaggioId();
        } catch (Exception e) {
            viaggioId = null;
        }
        if (viaggioId == null) {
            //startActivity(new Intent(getApplicationContext(), TravelList.class));
            //fittizio
            viaggioId = "-MtA7mKtdZODJR98_3hH";
        }

        if (attivitaId != null) {
            caricaDatiAttivita(attivitaId);
        }

        /*Button buttonMaps = findViewById(R.id.buttonMaps);
        buttonMaps.setOnClickListener(v -> {
            apriMappe(campoPosizione.getText().toString());
        });*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            campoPosizione.setText(place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
                    (dataInizioAttivitaButton.getText().toString() +
                            " " + (oraInizioAttivitaButton.getText().toString())));

            dataFine = formatter.parse(
                    (dataFineAttivitaButton.getText().toString() +
                            " " + (oraFineAttivitaButton.getText().toString())));
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

    private boolean checkCampi() {
        boolean compilato = true;

        if (TextUtils.isEmpty(campoNome.getText().toString())) {
            //error
            compilato = false;
        }
        if (TextUtils.isEmpty(dataInizioAttivitaButton.getText().toString())) {
            //error
            compilato = false;
        }
        if (TextUtils.isEmpty(oraInizioAttivitaButton.getText().toString())) {
            //error
            compilato = false;
        }
        if (TextUtils.isEmpty(dataFineAttivitaButton.getText().toString())) {
            //error
            compilato = false;
        }
        if (TextUtils.isEmpty(oraFineAttivitaButton.getText().toString())) {
            //error
            compilato = false;
        }

        return compilato;
    }


    private void showDatePickerDialog(String dateButtonString) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");

                if (dateButtonString == "StartDate") /*set inizio attivita date*/ {
                    if (dataFineAttivitaButton.getText().toString() == null) {
                        dataInizioAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                        dataFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                    } else {
                        Date dateI = calendar.getTime();
                        Date dateF = new Date();
                        try {
                            dateF = simpleDateFormat.parse(dataFineAttivitaButton.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (dateI.after(dateF)) {
                            dataInizioAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                            dataFineAttivitaButton.setText(null);
                            oraFineAttivitaButton.setText(null);
                        } else {
                            if (dateI.equals(dateF) && oraInizioAttivitaButton.getText().toString() != null && oraFineAttivitaButton.getText().toString() != null) {
                                Date timeI = new Date();
                                Date timeF = new Date();
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                try {
                                    timeI = timeFormat.parse(oraInizioAttivitaButton.getText().toString());
                                    timeF = timeFormat.parse(oraFineAttivitaButton.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (timeI.after(timeF)) {
                                    oraFineAttivitaButton.setText(null);
                                } else {
                                    dataInizioAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                                }
                            } else {
                                dataInizioAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }
                    }

                } else /*set fine attivita date*/ {
                    if (dataInizioAttivitaButton.getText().toString() == null) {
                        dataFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                    } else {
                        Date dateI = new Date();
                        try {
                            dateI = simpleDateFormat.parse(dataInizioAttivitaButton.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date dateF = calendar.getTime();
                        if (dateI.after(dateF)) {
                            dataFineAttivitaButton.setText(null);
                            Toast.makeText(NewActivityEvent.this, "Data di fine dopo l'inizio", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dateI.equals(dateF) && oraInizioAttivitaButton.getText().toString() != null && oraFineAttivitaButton.getText().toString() != null) {
                                Date timeI = new Date();
                                Date timeF = new Date();
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                try {
                                    timeI = timeFormat.parse(oraInizioAttivitaButton.getText().toString());
                                    timeF = timeFormat.parse(oraFineAttivitaButton.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (timeI.after(timeF)) {
                                    oraFineAttivitaButton.setText(null);
                                } else {
                                    dataFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                                }
                            } else {
                                dataFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }
                    }
                }
            }
        };

        new DatePickerDialog(NewActivityEvent.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimePickerDialog(String sceltaTimeString) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                if (sceltaTimeString == "StartTime") /*set inizio attivita hour*/ {
                    if (oraFineAttivitaButton.getText().toString() == null || dataFineAttivitaButton.getText().toString() == null || dataInizioAttivitaButton.getText().toString() == null) {
                        oraInizioAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                    } else {
                        if (dataInizioAttivitaButton.getText().toString() == dataFineAttivitaButton.getText().toString()) {
                            Date timeI = calendar.getTime();
                            Date timeF = new Date();
                            try {
                                timeF = simpleDateFormat.parse(oraInizioAttivitaButton.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (timeI.after(timeF)) {
                                oraFineAttivitaButton.setText(null);
                                Toast.makeText(NewActivityEvent.this, "Ora di fine dopo l'inizio", Toast.LENGTH_SHORT).show();
                            } else {
                                oraFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        } else {
                            oraInizioAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                } else /*set fine attivita hour*/ {
                    if (oraInizioAttivitaButton.getText().toString() == null || dataFineAttivitaButton.getText().toString() == null || dataInizioAttivitaButton.getText().toString() == null) {
                        oraFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                    } else {
                        if (dataInizioAttivitaButton.getText().toString() == dataFineAttivitaButton.getText().toString()) {
                            Date timeI = new Date();
                            try {
                                timeI = simpleDateFormat.parse(oraInizioAttivitaButton.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date timeF = calendar.getTime();
                            if (timeI.after(timeF)) {
                                oraFineAttivitaButton.setText(null);
                                Toast.makeText(NewActivityEvent.this, "Ora di fine dopo l'inizio", Toast.LENGTH_SHORT).show();
                            } else {
                                oraFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }
                    }
                }

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