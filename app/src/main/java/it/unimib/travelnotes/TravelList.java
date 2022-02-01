package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.unimib.travelnotes.Model.Viaggio;

public class TravelList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TravelListViewModel mTravelListViewModel;
    private String utenteId;

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Viaggio> list;
    public static final String FIREBASE_DATABASE_URL = "https://travelnotes-334817-default-rtdb.europe-west1.firebasedatabase.app/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);

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
        database = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("travel");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        final ImageButton mButtonNext = findViewById(R.id.new_travel);
        mButtonNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTravel.class);
            startActivity(intent);
        });

        database.addValueEventListener(new ValueEventListener() {
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
        });


    }

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

}


