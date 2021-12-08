package it.unimib.travelnotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.roomdb.TravelDatabase;

public class NewActivityEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


        Long idAttivitaI;
        try {
            idAttivitaI = Long.valueOf((int) getIntent().getExtras().get("idAttivita"));
        } catch (Exception e) {
            idAttivitaI = null;
        }
        if (idAttivitaI != null) {
            caricaDatiAttivita(idAttivitaI);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        configuraSalvaButtonNuovaAttivita();

    }

    private void showDatePickerDialog(final Button sceltaDateTime) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

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

                SimpleDateFormat simpleHourFormat=new SimpleDateFormat("HH:mm");

                sceltaDateTime.setText(simpleHourFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(NewActivityEvent.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    private void caricaDatiAttivita(Long idAttivitaI) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                Attivita attivitaSelezionata = TravelDatabase.getDatabase(getApplicationContext()).getAttivitaDao().findAttivitaById(idAttivitaI);

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
                /*calendar.get(Calendar.YEAR);
                calendar.get(Calendar.MONTH);
                calendar.get(Calendar.DAY_OF_MONTH);
                calendar.get(Calendar.HOUR_OF_DAY);
                calendar.get(Calendar.MINUTE);*/

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






    public void configuraSalvaButtonNuovaAttivita() {
        View buttonSalva = findViewById(R.id.salvaBottoneNuovaAttivita);
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

                        Attivita a = new Attivita();
                        a.setNome(((EditText) findViewById(R.id.nomeAttivitaInput)).getText().toString());
                        a.setPosizione(((EditText) findViewById(R.id.posizionePartenzaNuovaAttivita)).getText().toString());
                        a.setDescrizione(((EditText) findViewById(R.id.descrizioneNuovaAttivita)).getText().toString());
                        a.setDataInizio(dataInizio);
                        a.setDataFine(dataFine);


                        Long idRow = TravelDatabase.getDatabase(getApplicationContext()).getAttivitaDao().nuovaAttivita(a);

                        return null;
                    }
                }.execute();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

}