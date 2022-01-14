package it.unimib.travelnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.roomdb.TravelDatabase;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            String daA = andataVDa.getText().toString();
            String aA = andataVA.getText().toString();
            String daR = andataVDa.getText().toString();
            String aR = andataVA.getText().toString();
            //uso un if per verificare quale delle
            if (checkAR.isChecked()){
                Log.i("Prova_invioAR", "viagg.toString()");
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                try {
                    Date andataD = simpleDateFormat.parse(partenzaAndataButton.getText().toString());
                    Date ritornoD = simpleDateFormat.parse(partenzaAndataButton.getText().toString());
                    Viaggio viagg = new Viaggio(andataD, ritornoD, daA, aA, daR, aR, 1, 1);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            try {
                                Log.i("StampaPreLog", "background");
                                Long idRow = TravelDatabase.getDatabase(getApplicationContext()).getViaggioDao().nuovoViaggio(viagg);
                            } catch (Exception e) {
                                Log.e("personal_error_save", e.toString());
                            }

                            return null;
                        }
                    }.execute();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else{
                Log.i("Prova_invioAR", "viagg.toString()");
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                try {
                    Date andataD = simpleDateFormat.parse(partenzaAndataButton.getText().toString());
                    Date ritornoD = simpleDateFormat.parse(partenzaAndataButton.getText().toString());
                    Viaggio viagg = new Viaggio(andataD, daA, aA, 1);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            try {
                                Log.i("StampaPreLog", "background");
                                Long idRow = TravelDatabase.getDatabase(getApplicationContext()).getViaggioDao().nuovoViaggio(viagg);
                            } catch (Exception e) {
                                Log.e("personal_error_save", e.toString());
                            }

                            return null;
                        }
                    }.execute();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            /* new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        Log.i("back", "background");
                        //Long idRow = TravelDatabase.getDatabase(getApplicationContext()).getViaggioDao().nuovoViaggio(viagg);
                    } catch (Exception e) {
                        Log.e("personal_error_save", e.toString());
                    }

                    return null;
                }
            }.execute();*/
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

    private void showDateTimeDialog2(final Button sceltaDateTime, Date date1) {
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
                        Date date2 = null;
                        try {
                            date2 = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(date1.compareTo(date2) < 0){
                            Log.i("piccola", "d1<d2");
                        }
                        else if(date1.compareTo(date2) > 0){
                            Log.i("grande", "d1>d2");
                        }
                        else{
                            Log.i("uguale", "d1=d2");
                        }

                        sceltaDateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(NewTravel.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(NewTravel.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}