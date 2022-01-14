package it.unimib.travelnotes.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "elenco_attivita")
public class Attivita {

    @PrimaryKey
    @NonNull
    private String attivitaId;

    private String viaggioId;

    private Date dataInizio;
    private Date dataFine;
    private String posizione;
    private String nome;
    private String descrizione;

    @NonNull
    public String getAttivitaId() {
        return attivitaId;
    }

    public void setAttivitaId(@NonNull String attivitaId) {
        this.attivitaId = attivitaId;
    }

    public String getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(String viaggioId) {
        this.viaggioId = viaggioId;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataI) {
        this.dataInizio = dataI;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataF) {
        this.dataFine = dataF;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Attivita() {}

    public Attivita(String nome, String descrizione,String posizione, Date dataInizio, Date dataFine, String viaggioId){
        this.nome= nome;
        this.descrizione = descrizione;
        this.posizione = posizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.viaggioId = viaggioId;
    }

    public Attivita(String nomeI, String descrizioneI){
        this.nome= nomeI;
        this.descrizione = descrizioneI;

    }
}