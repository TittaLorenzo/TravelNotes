package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.unimib.travelnotes.Model.Viaggio;

public class TravelList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Viaggio> list;

    public static final String FIREBASE_DATABASE_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";
    private String utenteId;

    public void listaViaggiListenerProva(String utenteId) {

        ValueEventListener listaViaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    Viaggio viaggio = postSnapshot.getValue(Viaggio.class);

                    list.add(viaggio);
                }

                myAdapter = new MyAdapter(TravelList.this,list);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference().child("utenti").child(utenteId).child("listaviaggi").addValueEventListener(listaViaggiListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);
        list = new ArrayList<>();
       // database = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("travel");

        utenteId = "gvHD5XtoQWTOVecJ8mGohU1qbBy2";

        listaViaggiListenerProva(utenteId);




        /*ValueEventListener listaViaggiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    Viaggio viaggio = postSnapshot.getValue(Viaggio.class);

                    list.add(viaggio);
                }

                myAdapter = new MyAdapter(TravelList.this,list);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference().child("utenti").child("JTvnws61akaZyQYfzeubdzXgx2H3").child("listaviaggi").addValueEventListener(listaViaggiListener);
        */



        recyclerView = findViewById(R.id.recyclerView);
        /*
        database.child("viaggi").child("-MtA7mKtdZODJR98_3hH").child("dettagliviaggio").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                /*int i = 0;
                for (DataSnapshot parentIdSnapshot: task.getResult().getChildren()) {
                    try {
                        list.set(i, parentIdSnapshot.getValue(Viaggio.class));
                    } catch (Exception e) {
                        Log.e("MyLog", String.valueOf(task.getResult()));
                    }
                    i++;
                }

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Viaggio viaggio = task.getResult().getValue(Viaggio.class);
                }
            }

        });*/

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        final ImageButton mButtonNext = findViewById(R.id.new_travel);
        mButtonNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTravel.class);
            startActivity(intent);
        });

        /*database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Viaggio viaggio = dataSnapshot.getValue(Viaggio.class);
                    list.add(viaggio);


                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }

}


