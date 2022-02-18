package it.unimib.travelnotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.autentication.RegisterActivity;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class MainActivity extends AppCompatActivity {

    SharedPreferencesProvider mSharedPreferencesProvider;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        userId = mSharedPreferencesProvider.getSharedUserId();

        getDynamicLink();

        if (userId != null) {
            startActivity(new Intent(this, TravelList.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        final Button loginPage = findViewById(R.id.loginPageButton);
        loginPage.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });

        final Button registerPage = findViewById(R.id.registerPageButton);
        registerPage.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }

    public void getDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;

                        if (pendingDynamicLinkData != null) {
                            if (userId != null) {
                                deepLink = pendingDynamicLinkData.getLink();

                                String viaggioId = deepLink.getQueryParameter("viaggioId");
                                String email = mSharedPreferencesProvider.getSharedUserEmail();

                                ITravelRepository mITravelRepository = new TravelRepository(getApplication());
                                mITravelRepository.pushAggiungiAlGruppo(email, viaggioId);
                            } else {
                                Toast.makeText(MainActivity.this, "Loggati o Registrati per aprire il link", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyTag", "getDynamicLink:onFailure", e);
                    }
                });
    }
}