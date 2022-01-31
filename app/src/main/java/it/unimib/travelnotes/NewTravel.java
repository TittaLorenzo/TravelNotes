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
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;
import it.unimib.travelnotes.roomdb.TravelDatabase;

public class NewTravel extends AppCompatActivity {

    Button partenzaAndataButton;
    Button arrivoAndataButton;
    Button partenzaRitornoButton;
    Button arrivoRitornoButton;
    TextInputEditText andataVDa;
    TextInputEditText andataVA;
    TextInputEditText ritornoVDa;
    TextInputEditText ritornoVA;
    SwitchMaterial checkAR;
    FloatingActionButton salvaVolo;
    String s1, s2;
    Date data1, data2;

    private ITravelRepository mITravelRepository;

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
        checkAR = findViewById(R.id.NT_checkAR);
        andataVDa = findViewById(R.id.NT_andataDa);
        andataVA = findViewById(R.id.NT_andataA);
        ritornoVDa = findViewById(R.id.NT_ritornoDa);
        ritornoVA = findViewById(R.id.NT_ritornoA);
        mITravelRepository = new TravelRepository(getApplication());
        salvaVolo = findViewById(R.id.NT_floatingButton);

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

        salvaVolo.setOnClickListener(v -> {
            String daA = andataVDa.getText().toString();
            String aA = andataVA.getText().toString();
            String daR = ritornoVDa.getText().toString();
            String aR = ritornoVA.getText().toString();
            //uso un if per verificare quale delle

            if (checkAR.isChecked()){
                Log.i("Prova_invioAR", "viagg.toString()");
                try {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                    Date andataD = simpleDateFormat.parse(partenzaAndataButton.getText().toString());
                    Date arrivoD = simpleDateFormat.parse(arrivoAndataButton.getText().toString());
                    Date andataR = simpleDateFormat.parse(partenzaRitornoButton.getText().toString());
                    Date arrivoR = simpleDateFormat.parse(arrivoRitornoButton.getText().toString());
                    long diff = (arrivoD.getTime() - andataD.getTime());
                    long diffMinutesD = diff / (60 * 1000);
                    diff = (arrivoR.getTime() - andataR.getTime());
                    long diffMinutesR = diff / (60 * 1000);
                    Viaggio viagg = new Viaggio(andataD, andataR, daA, aA, daR, aR, Double.longBitsToDouble(diffMinutesD), Double.longBitsToDouble(diffMinutesR));
                    if(diffMinutesD <= 0 || diffMinutesR <= 0 || andataR.compareTo(andataD) <= 0 || arrivoR.compareTo(arrivoD) <= 0 || andataR.compareTo(arrivoD) <= 0){
                        Toast.makeText(this, "Correggere le date", Toast.LENGTH_SHORT).show();
                    }
                    else if(andataVDa.getText().toString().matches("") || andataVA.getText().toString().matches("") || ritornoVDa.getText().toString().matches("") || ritornoVA.getText().toString().matches("")){
                        Toast.makeText(this, "Inserire città valide", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Viaggio creato", Toast.LENGTH_SHORT).show();
                        //mITravelRepository.pushNuovoViaggio(viagg, false);
                    }
                    /*new AsyncTask<Void, Void, Void>() {
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
                    }.execute();*/

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else{
                Log.i("Prova_invioAR", "viagg.toString()");
                try {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                    Date andataD = simpleDateFormat.parse(partenzaAndataButton.getText().toString());
                    Date arrivoD = simpleDateFormat.parse(arrivoAndataButton.getText().toString());
                    long diff = (arrivoD.getTime() - andataD.getTime());
                    long diffMinutes = diff / (60 * 1000);
                    Viaggio viagg = new Viaggio(andataD, daA, aA, Double.longBitsToDouble(diffMinutes));
                    if(diffMinutes <= 0){
                        Toast.makeText(this, "Correggere le date", Toast.LENGTH_SHORT).show();
                    }
                    else if(andataVDa.getText().toString().matches("") || andataVA.getText().toString().matches("")){
                        Toast.makeText(this, "Inserire città valide", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Viaggio creato", Toast.LENGTH_SHORT).show();
                        //mITravelRepository.pushNuovoViaggio(viagg, false);
                    }
                    /*new AsyncTask<Void, Void, Void>() {
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
                    }.execute();*/

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
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