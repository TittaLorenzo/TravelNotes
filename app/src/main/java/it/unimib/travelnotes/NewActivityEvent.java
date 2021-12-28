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
}