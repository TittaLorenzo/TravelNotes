package it.unimib.travelnotes.autentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.travelnotes.MainActivity;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.SharedPreferencesProvider;
import it.unimib.travelnotes.TravelList;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ITravelRepository mITravelRepository;
    private SharedPreferencesProvider mSharedPreferencesProvider;

    private EditText emailField;
    private EditText passwordField;
    private Button login;
    private Button cancelButton;
    private TextView registrati;
    private TextView pwDimenticataLink;

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mITravelRepository = new TravelRepository(getApplication());
        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());

        emailField = findViewById(R.id.email_login_edit_text);
        passwordField = findViewById(R.id.password_login_edit_text);
        login = findViewById(R.id.loginButton);
        cancelButton = findViewById(R.id.cancel_button);

        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailLoginTextInputLayout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.password_login_text_input);
        mProgressBar = (ProgressBar) findViewById(R.id.login_progress_i);

        registrati = findViewById(R.id.registratiTv);
        pwDimenticataLink = findViewById(R.id.pwLostLink);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = emailField.getText().toString().trim();
                String txtPassword = passwordField.getText().toString().trim();

                login.setEnabled(false);
                login.setBackgroundColor(getResources().getColor(R.color.primaryLightColor));
                emailField.setFocusable(false);
                passwordField.setFocusable(false);
                registrati.setLinksClickable(false);
                pwDimenticataLink.setLinksClickable(false);
                mProgressBar.setVisibility(View.VISIBLE);

                loginUser(txtEmail, txtPassword);
            }
        });

        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

        registrati.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });


        pwDimenticataLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder pwLostDialog = new AlertDialog.Builder(v.getContext());
                pwLostDialog.setTitle("Reset Password?");
                pwLostDialog.setMessage("Inserisci la tua Email per ricevere il link di reset.");
                pwLostDialog.setView(resetEmail);

                pwLostDialog.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String resEmail = resetEmail.getText().toString().trim();
                        //TODO verifica se l'email è valida

                        mAuth.sendPasswordResetEmail(resEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(LoginActivity.this, "Email inviata", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Errore! Email non inviata", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                pwLostDialog.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close Dialog
                    }
                });

                pwLostDialog.create().show();
            }
        });
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login effettuato", Toast.LENGTH_SHORT).show();

                            String userId = mAuth.getCurrentUser().getUid();

                            mSharedPreferencesProvider.setSharedUserId(userId);
                            mSharedPreferencesProvider.setSharedUserEmail(email);

                            mITravelRepository.loadUtente(userId);

                            mProgressBar.setVisibility(View.GONE);

                            startActivity(new Intent(LoginActivity.this, TravelList.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login fallito", Toast.LENGTH_SHORT).show();
                            emailTextInputLayout.setError(getString(R.string.erroreLogin));
                            passwordTextInputLayout.setError(getString(R.string.erroreLogin));

                            mProgressBar.setVisibility(View.GONE);
                            login.setEnabled(true);
                            login.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                            emailField.setFocusable(true);
                            passwordField.setFocusable(true);
                            registrati.setLinksClickable(true);
                            pwDimenticataLink.setLinksClickable(true);
                        }
                    }
                });
    }

}