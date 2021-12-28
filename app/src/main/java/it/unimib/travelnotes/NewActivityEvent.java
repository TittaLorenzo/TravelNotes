package it.unimib.travelnotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.roomdb.TravelDatabase;

public class NewActivityEvent extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button dataInizioAttivitaButton;
    private Button dataFineAttivitaButton;
    private Button oraInizioNuovaAttivita;
    private Button oraFineAttivitaButton;

    private EditText campoNome;
    private EditText campoPosizione;
    private EditText campoDescrizione;
    private Button buttonSalva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mAuth = FirebaseAuth.getInstance();

        dataInizioAttivitaButton = findViewById(R.id.dataInizioNuovaAttivita);
        dataFineAttivitaButton = findViewById(R.id.dataFineNuovaAttivita);
        oraInizioNuovaAttivita = findViewById(R.id.oraInizioNuovaAttivita);
        oraFineAttivitaButton = findViewById(R.id.oraFineNuovaAttivita);

        campoNome = findViewById(R.id.nomeAttivitaInput);
        campoPosizione = findViewById(R.id.posizionePartenzaNuovaAttivita);
        campoDescrizione = findViewById(R.id.descrizioneNuovaAttivita);
        buttonSalva = findViewById(R.id.salvaBottoneNuovaAttivita);

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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(campoNome.getText().toString())) {
            Toast.makeText(this, "Devi inserire un nome attività", Toast.LENGTH_SHORT).show();
        } else {
            salvaButtonNuovaAttivita();
        }
    }

    //menu logout
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Logout effettuato", Toast.LENGTH_SHORT).show();
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
        }
        return true;
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

    private void caricaDatiAttivita(Long idAttivitaI) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                Attivita attivitaSelezionata = new Attivita("Milano", "Parigi", 10, 10, 90, 90);
                try {
                    attivitaSelezionata = TravelDatabase.getDatabase(getApplicationContext()).getAttivitaDao().findAttivitaById(idAttivitaI);
                } catch (Exception e) {
                    Log.e("personal_error_load", e.toString());
                }

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

                return null;
            }
        }.execute();


    }

    public void salvaButtonNuovaAttivita() {
        buttonSalva.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

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
                            Log.v("----------------", "parsing date fallito");
                        }

                        Attivita a = new Attivita("Milano", "Parigi", 10, 10, 90, 90);
                        a.setNome(campoNome.getText().toString());
                        a.setPosizione(campoPosizione.getText().toString());
                        a.setDescrizione(campoDescrizione.getText().toString());
                        a.setDataInizio(dataInizio);
                        a.setDataFine(dataFine);

                        try {
                            Long idRow = TravelDatabase.getDatabase(getApplicationContext()).getAttivitaDao().nuovaAttivita(a);
                        } catch (Exception e) {
                            Log.e("personal_error_save", e.toString());
                        }

                        return null;
                    }
                }.execute();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}