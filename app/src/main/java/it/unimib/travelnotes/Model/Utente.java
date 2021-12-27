package it.unimib.travelnotes.Model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity (tableName = "elenco_utenti")
public class Utente {

    @PrimaryKey
    @NonNull
    private String utenteId;

    private String email;
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

    public Date getNascita() {
        return nascita;
    }

    public void setNascita(Date nascita) {
        this.nascita = nascita;
    }

    public Utente() {
    }

    public Utente(String nomeI, String cognomeI, Date nascitaI, String emailU){
        this.nome= nomeI;
        this.cognome = cognomeI;
        this.nascita = nascitaI;
        this.email = emailU;
    }
}
