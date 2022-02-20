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
import android.widget.ProgressBar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import it.unimib.travelnotes.TravelList;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;
import it.unimib.travelnotes.roomdb.TravelDatabase;

public class NewActivityEvent extends AppCompatActivity {

    private static final String AUTOCOMPLETE_API_KEY = "AIzaSyAmaveq8N5RXhsJhELqQWYP-coB78I89NQ";

    private FirebaseAuth mAuth;

    private ITravelRepository mITravelRepository;

    private Button dataInizioAttivitaButton;
    private Button dataFineAttivitaButton;
    private Button oraInizioAttivitaButton;
    private Button oraFineAttivitaButton;
    private FloatingActionButton buttonSalva;

    private EditText campoNome;
    private EditText campoPosizione;
    private EditText campoDescrizione;
    private ProgressBar mProgressBar;

    private String attivitaId;
    private String viaggioId;
    private boolean a_r;




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
        mProgressBar = (ProgressBar) findViewById(R.id.newactivity_progress_i);

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

        buttonSalva.setOnClickListener(c -> {
            if (!checkCampi()) {
                Toast.makeText(this, "Devi compilare tutti i campi", Toast.LENGTH_SHORT).show();
            } else {
                buttonSalva.setEnabled(false);
                buttonSalva.setBackgroundColor(getResources().getColor(R.color.primaryLightColor));
                mProgressBar.setVisibility(View.VISIBLE);

                salvaButtonNuovaAttivita();
            }
        });


        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        try {
            attivitaId = (String) getIntent().getExtras().get("idAttivita");
        } catch (Exception e) {
            attivitaId = null;
        }
        try {
            viaggioId = sharedPreferencesProvider.getSelectedViaggioId();
        } catch (Exception e) {
            viaggioId = null;
        }
        if (viaggioId == null) {
            startActivity(new Intent(getApplicationContext(), TravelList.class));
        }
        try {
            a_r = sharedPreferencesProvider.getViaggioA_R();
        } catch (Exception e) {
            a_r = true;
        }

        if (attivitaId != null) {
            buttonSalva.setEnabled(false);
            buttonSalva.setBackgroundColor(getResources().getColor(R.color.primaryLightColor));
            mProgressBar.setVisibility(View.VISIBLE);

            caricaDatiAttivita(attivitaId);
        }
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonSalva.setImageResource(R.drawable.ic_baseline_update_24);

                        TextView titoloNuovaAttivita = findViewById(R.id.titloloNuovaAttivitaId);
                        titoloNuovaAttivita.setText(R.string.titleModificaAttivita);

                        if (attivitaSelezionata.getNome() != null) {
                            EditText nomeAttivitaInput = (EditText) findViewById(R.id.nomeAttivitaInput);
                            nomeAttivitaInput.setText(attivitaSelezionata.getNome());
                        }

                        if (attivitaSelezionata.getDataInizio() != null) {
                            EditText posizionePartenzaNuovaAttivita = (EditText) findViewById(R.id.posizionePartenzaNuovaAttivita);
                            posizionePartenzaNuovaAttivita.setText(attivitaSelezionata.getPosizione());
                        }

                        if (attivitaSelezionata.getDataInizio() != null) {
                            EditText descrizioneNuovaAttivita = (EditText) findViewById(R.id.descrizioneNuovaAttivita);
                            descrizioneNuovaAttivita.setText(attivitaSelezionata.getDescrizione());
                        }


                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat simpleHourFormat=new SimpleDateFormat("HH:mm");

                        Calendar calendar = Calendar.getInstance();
                        if (attivitaSelezionata.getDataInizio() != null) {
                            calendar.setTime(attivitaSelezionata.getDataInizio());

                            Button dataInizioNuovaAttivita = (Button) findViewById(R.id.dataInizioNuovaAttivita);
                            dataInizioNuovaAttivita.setText(simpleDateFormat.format(calendar.getTime()));

                            Button oraInizioNuovaAttivita = (Button) findViewById(R.id.oraInizioNuovaAttivita);
                            oraInizioNuovaAttivita.setText(simpleHourFormat.format(calendar.getTime()));
                        }

                        if (attivitaSelezionata.getDataInizio() != null) {
                            calendar.setTime(attivitaSelezionata.getDataFine());

                            Button dataFineNuovaAttivita = (Button) findViewById(R.id.dataFineNuovaAttivita);
                            dataFineNuovaAttivita.setText(simpleDateFormat.format(calendar.getTime()));

                            Button oraFineNuovaAttivita = (Button) findViewById(R.id.oraFineNuovaAttivita);
                            oraFineNuovaAttivita.setText(simpleHourFormat.format(calendar.getTime()));
                        }

                        buttonSalva.setEnabled(true);
                        buttonSalva.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
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
        i.putExtra("viaggioId", viaggioId);
        i.putExtra("a_r", a_r);
        startActivity(i);
        finish();
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

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                if (dateButtonString.equals("StartDate")) /*set inizio attivita date*/ {
                    if (dataFineAttivitaButton.getText().toString().equals("")) {
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
                            if (dateI.equals(dateF) && oraInizioAttivitaButton.getText().toString().equals("") && oraFineAttivitaButton.getText().toString().equals("")) {
                                Date timeI = new Date();
                                Date timeF = new Date();
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                try {
                                    timeI = timeFormat.parse(oraInizioAttivitaButton.getText().toString());
                                    timeF = timeFormat.parse(oraFineAttivitaButton.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (timeI != null && timeF != null && timeI.getHours() > timeF.getHours() && timeI.getMinutes() > timeF.getMinutes()) {
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
                    if (dataInizioAttivitaButton.getText().toString().equals("")) {
                        dataFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                    } else {
                        Date dateI = new Date();
                        try {
                            dateI = simpleDateFormat.parse(dataInizioAttivitaButton.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date dateF = calendar.getTime();
                        if (dateI != null && dateI.getYear() > dateF.getYear() && dateI.getMonth() > dateF.getMonth() && dateI.getDay() > dateF.getDay()) {
                            dataFineAttivitaButton.setText(null);
                            Toast.makeText(NewActivityEvent.this, "Data di fine dopo l'inizio", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dateI.equals(dateF) && !oraInizioAttivitaButton.getText().toString().equals("") && !oraFineAttivitaButton.getText().toString().equals("")) {
                                Date timeI = new Date();
                                Date timeF = new Date();
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                try {
                                    timeI = timeFormat.parse(oraInizioAttivitaButton.getText().toString());
                                    timeF = timeFormat.parse(oraFineAttivitaButton.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (timeI != null && timeF != null && timeI.getHours() > timeF.getHours() && timeI.getMinutes() > timeF.getMinutes()) {
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

                if (sceltaTimeString.equals("StartTime")) /*set inizio attivita hour*/ {
                    if (oraFineAttivitaButton.getText().toString().equals("") || dataFineAttivitaButton.getText().toString().equals("") || dataInizioAttivitaButton.getText().toString().equals("")) {
                        oraInizioAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                    } else {
                        if (dataInizioAttivitaButton.getText().toString().equals(dataFineAttivitaButton.getText().toString())) {
                            Date timeI = calendar.getTime();
                            Date timeF = new Date();
                            try {
                                timeF = simpleDateFormat.parse(oraInizioAttivitaButton.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //if (timeI.after(timeF)) {
                            if (timeF != null && timeI.getHours() > timeF.getHours() && timeI.getMinutes() > timeF.getMinutes()) {
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
                    if (oraInizioAttivitaButton.getText().toString().equals("") || dataFineAttivitaButton.getText().toString().equals("") || dataInizioAttivitaButton.getText().toString().equals("")) {
                        oraFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                    } else {
                        if (dataInizioAttivitaButton.getText().toString().equals(dataFineAttivitaButton.getText().toString())) {
                            Date timeI = new Date();
                            try {
                                timeI = simpleDateFormat.parse(oraInizioAttivitaButton.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date timeF = calendar.getTime();
                            if (timeI != null && timeI.getHours() > timeF.getHours() && timeI.getMinutes() > timeF.getMinutes()) {
                                oraFineAttivitaButton.setText(null);
                                Toast.makeText(NewActivityEvent.this, "Ora di inizio dopo la fine", Toast.LENGTH_SHORT).show();
                            } else {
                                oraFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        } else {
                            oraFineAttivitaButton.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                }

            }
        };
        new TimePickerDialog(NewActivityEvent.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }
}