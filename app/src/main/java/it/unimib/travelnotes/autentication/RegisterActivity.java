package it.unimib.travelnotes.autentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.travelnotes.MainActivity;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.TravelList;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final ITravelRepository mITravelRepository = new TravelRepository(getApplication());

    private EditText email;
    private EditText password;
    private EditText password2;
    private Button register;
    private Button cancelButtonRegister;
    private TextView giaRegistrato;

    private TextInputLayout pw1TextInputLayout;
    private TextInputLayout pw2TextInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_register_edit_text);
        password = findViewById(R.id.password_register_edit_text);
        password2 = findViewById(R.id.password2_register_edit_text);
        register = findViewById(R.id.registerButton);
        cancelButtonRegister = findViewById(R.id.cancel_button_register);

        pw1TextInputLayout = (TextInputLayout) findViewById(R.id.password_register_text_input);
        pw2TextInputLayout = (TextInputLayout) findViewById(R.id.password2_register_text_input);

        giaRegistrato = findViewById(R.id.giaRegistratoTv);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString().trim();
                String txtPassword = password.getText().toString().trim();
                String txtPassword2 = password2.getText().toString().trim();

                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty((txtPassword))) {
                    Toast.makeText(RegisterActivity.this, "Riempi i campi obbligatori", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password troppo corta", Toast.LENGTH_SHORT).show();
                    pw1TextInputLayout.setError(getString(R.string.pwCortaError));
                } else if (!txtPassword.equals(txtPassword2)) {
                    Toast.makeText(RegisterActivity.this, "Le password non coincidono", Toast.LENGTH_SHORT).show();
                    pw2TextInputLayout.setError(getString(R.string.pwNonCoincidonoError));
                } else {
                    registerUser(txtEmail, txtPassword);
                }
            }
        });

        cancelButtonRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });

        giaRegistrato.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show();

                            String userId = mAuth.getCurrentUser().getUid();
                            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.shared_userid_key), userId);
                            editor.apply();

                            /*String defaultValue = getResources().getString(R.string.shared_userid_key);
                            String highScore = sharedPref.getString(getString(R.string.shared_userid_key), defaultValue);*/

                            Utente u = new Utente();
                            u.setUtenteId(userId);
                            u.setEmail(email);

                            mITravelRepository.pushNuovoUtente(u);

                            startActivity(new Intent(RegisterActivity.this, TravelList.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registrazione fallita", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}