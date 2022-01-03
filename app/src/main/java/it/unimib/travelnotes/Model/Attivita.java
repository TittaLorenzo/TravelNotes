package it.unimib.travelnotes.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "elenco_attivita")
public class Attivita {

    @PrimaryKey(autoGenerate = true)
    private long attivitaId;

    private long viaggioId;

    private Date dataInizio;
    private Date dataFine;
    private String posizione;
    private String nome;
    private String descrizione;

    public long getAttivitaId() {
        return attivitaId;
    }

    public void setAttivitaId(long attivitaId) {
        this.attivitaId = attivitaId;
    }

    public long getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(long viaggioId) {
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

    public Attivita(String nomeI, String descrizioneI,String posizione, Date di, Date df, long IDu){
        this.nome= nomeI;
        this.descrizione = descrizioneI;
        this.posizione = posizione;
        this.dataInizio = di;
        this.dataFine = df;
        this.viaggioId = IDu;
    }
}
