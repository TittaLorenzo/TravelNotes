package it.unimib.travelnotes.autentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.travelnotes.MainActivity;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ITravelRepository mITravelRepository;

    private EditText email;
    private EditText password;
    private Button login;
    private Button cancelButton;
    private TextView registrati;
    private TextView pwDimenticataLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mITravelRepository = new TravelRepository(getApplication());

        email = findViewById(R.id.email_register_edit_text);
        password = findViewById(R.id.password_register_edit_text);
        login = findViewById(R.id.loginButton);
        cancelButton = findViewById(R.id.cancel_button);

        registrati = findViewById(R.id.registratiTv);
        pwDimenticataLink = findViewById(R.id.pwLostLink);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

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

                        String resEmail = resetEmail.getText().toString();
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
                            mITravelRepository.loadUtente(userId);

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login fallito", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*private void addUser(String email, String userId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Utente u = new Utente();
                u.setEmail(mAuth.getCurrentUser().getEmail());
                u.setUtenteId(mAuth.getUid());

                try {
                    long idRow = TravelDatabase.getDatabase(getApplicationContext()).getUtenteDao().nuovoUtente(u);
                } catch (Exception e) {
                    Log.e("personal_error_save", e.toString());
                }
            }
        };
        new Thread(runnable).start();
    }*/
}