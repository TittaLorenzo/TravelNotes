package it.unimib.travelnotes.Model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "elenco_utenti")
public class Utente {

    @PrimaryKey
    @NonNull
    private String utenteId;

    private String email;
    private String username;
    private String nome;
    private String cognome;
    private Date nascita;

    @NonNull
    public String getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(@NonNull String utenteId) {
        this.utenteId = utenteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailU) {
        this.email = emailU;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getNascita() {
        return nascita;
    }

    public void setNascita(Date nascita) {
        this.nascita = nascita;
    }

    public Utente() {
    }

    public Utente(@NonNull String utenteId, String email, String username){
        this.utenteId = utenteId;
        this.email = email;
        this.username = username;
    }
    public Utente(String username, String email){
        this.username = username;
        this.email = email;
    }
}
