package it.unimib.travelnotes.Model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity (tableName = "elenco_utenti", indices = {@Index({"nome", "cognome"})})
public class Utente {

    @PrimaryKey
    @NonNull
    private String email;

    private String nome;
    private String cognome;
    private Date nascita;

    //liste
    @Ignore
    private ArrayList<Viaggio> listaViaggi = new ArrayList<Viaggio>();


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

    public ArrayList<Viaggio> getListaViaggi() {
        return listaViaggi;
    }

    public void addListaViaggi(Viaggio nuovoViaggio) {
        this.listaViaggi.add(nuovoViaggio);
    }

    public void setViaggioIndex(int index, Viaggio modificaViaggio) {
        this.listaViaggi.set(index, modificaViaggio);
    }

    public void removeViaggio(int index) {
        this.listaViaggi.remove(index);
    }

    public void clearViaggio() {
        this.listaViaggi.clear();
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
