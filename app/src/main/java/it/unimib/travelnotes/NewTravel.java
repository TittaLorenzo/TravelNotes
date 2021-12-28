package it.unimib.travelnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.unimib.travelnotes.Model.Viaggio;

public class NewTravel extends AppCompatActivity {

    ImageButton invio;
    Button partenzaAndataButton;
    Button arrivoAndataButton;
    Button partenzaRitornoButton;
    Button arrivoRitornoButton;
    EditText andataVDa;
    EditText andataVA;
    //EditText ritornoVDa;
    //EditText ritornoVA;
    CheckBox checkAR;
    String s1, s2;
    Date data1, data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);
        findViewById(R.id.NT_layoutRitorno).setVisibility(View.INVISIBLE);
        partenzaAndataButton = findViewById(R.id.NT_sceltaAndataP);
        arrivoAndataButton = findViewById(R.id.NT_sceltaAndataR);
        partenzaRitornoButton = findViewById(R.id.NT_sceltaRitornoP);
        arrivoRitornoButton = findViewById(R.id.NT_sceltaRitornoA);
        invio = findViewById(R.id.NT_newTravelButton);
        checkAR = findViewById(R.id.NT_checkAR);
        andataVDa = findViewById(R.id.NT_andataDa);
        andataVA = findViewById(R.id.NT_andataA);

        partenzaAndataButton.setOnClickListener(v -> {
            showDateTimeDialog(partenzaAndataButton);
        });

        arrivoAndataButton.setOnClickListener(v -> {
            showDateTimeDialog(arrivoAndataButton);
        });

        partenzaRitornoButton.setOnClickListener(v -> {
            showDateTimeDialog(partenzaRitornoButton);
        });

        arrivoRitornoButton.setOnClickListener(v -> {
            showDateTimeDialog(arrivoRitornoButton);
        });

        checkAR.setOnClickListener(v -> {
            if (checkAR.isChecked()){
                findViewById(R.id.NT_layoutRitorno).setVisibility(View.VISIBLE);
            } else{
                findViewById(R.id.NT_layoutRitorno).setVisibility(View.INVISIBLE);
            }
        });

        invio.setOnClickListener(v -> {
            String dav = andataVDa.getText().toString();
            String av = andataVA.getText().toString();

            Log.i("Prova_invio", "viaggio.toString()");
        });
    }


    private void showDateTimeDialog(final Button sceltaDateTime) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");

                        sceltaDateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(NewTravel.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(NewTravel.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
}